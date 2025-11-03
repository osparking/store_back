package com.bumsoap.store.controller;

import com.bumsoap.store.dto.CheckAmountResult;
import com.bumsoap.store.dto.OrderInfo;
import com.bumsoap.store.request.ConfirmPaymentReq;
import com.bumsoap.store.request.SaveAmountReq;
import com.bumsoap.store.service.PaymentService;
import com.bumsoap.store.util.UrlMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping(UrlMap.PAYMENTS)
public class PaymentCon {
    private static final Logger logger =
            LoggerFactory.getLogger(PaymentCon.class);

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/saveAmount")
    public ResponseEntity<?> saveAmountTemporarily(
            HttpSession session, @RequestBody SaveAmountReq request) {
        session.setAttribute(request.getOrderId(),
                new OrderInfo(request.getAmount(), request.getOrderName()));
        return ResponseEntity.ok("<주문 ID, 결제액> 항목 세션 저장.");
    }

    @PostMapping("/checkAmount")
    public ResponseEntity<CheckAmountResult> checkIfAmountMatches(
            HttpSession session, @RequestBody SaveAmountReq req) {

        var order = (OrderInfo) session.getAttribute(req.getOrderId());

        if (order==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new CheckAmountResult(false, "결제 정보 부재"));
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new CheckAmountResult(req.getAmount().equals(
                            order.getAmount()), order.getOrderName()));
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmPayment(
            @RequestBody @NonNull ConfirmPaymentReq confirmPaymentReq)
            throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String request = objectMapper.writeValueAsString(confirmPaymentReq);

        if (request==null) {
            throw new Exception("결제 요청 객체 널 오류 발생");
        }

        JSONObject response = sendRequest(request,
                System.getenv("WIDGET_SECRET_KEY"),
                "https://api.tosspayments.com/v1/payments/confirm");

        paymentService.createPayment(response);

//        var recentSome = paymentService.getRecentSome(1);
        int statusCode = response.containsKey("error") ? 400:200;

        return ResponseEntity.status(statusCode).body("결제 최종 확인 완료.");
    }

    private JSONObject sendRequest(String confirmStr, String secretKey,
                                   String urlString) throws IOException {
        HttpURLConnection connection = createConnection(secretKey, urlString);
        try (OutputStream os = connection.getOutputStream()) {
            os.write(confirmStr.getBytes(StandardCharsets.UTF_8));
        }

        try (InputStream responseStream = connection.getResponseCode()==200 ?
                connection.getInputStream():
                connection.getErrorStream();
             Reader reader = new InputStreamReader(responseStream,
                     StandardCharsets.UTF_8)) {
            return (JSONObject) new JSONParser().parse(reader);
        } catch (Exception e) {
            logger.error("Error reading response", e);
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("error", "Error reading response");
            return errorResponse;
        }
    }

    private HttpURLConnection createConnection(
            String secretKey, String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "Basic " +
                Base64.getEncoder().encodeToString(
                        (secretKey + ":").getBytes(StandardCharsets.UTF_8)));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        return connection;
    }
}
