package com.bumsoap.store.controller;

import com.bumsoap.store.dto.CheckAmountResult;
import com.bumsoap.store.dto.OrderInfo;
import com.bumsoap.store.email.EmailManager;
import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.request.ConfirmPaymentReq;
import com.bumsoap.store.request.SaveAmountReq;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.PaymentService;
import com.bumsoap.store.service.order.OrderServI;
import com.bumsoap.store.service.user.UserServInt;
import com.bumsoap.store.util.BsUtils;
import com.bumsoap.store.util.OrderStatus;
import com.bumsoap.store.util.UrlMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Base64;

@RestController
@RequestMapping(UrlMap.PAYMENTS)
public class PaymentCon {
    private static final Logger logger =
            LoggerFactory.getLogger(PaymentCon.class);

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderServI orderServ;

    @Autowired
    private EmailManager emailManager;

    @Autowired
    private UserServInt userServ;

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
    public ResponseEntity<ApiResp> confirmPayment(
            @RequestBody @NonNull ConfirmPaymentReq confirmPaymentReq,
            Principal principal, Authentication authentication)
            throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String request = objectMapper.writeValueAsString(confirmPaymentReq);

        if (request==null) {
            throw new Exception("결제 요청 객체 널 오류 발생");
        }

        JSONObject response = sendRequest(request,
                System.getenv("WIDGET_SECRET_KEY"),
                "https://api.tosspayments.com/v1/payments/confirm");

        var payment = paymentService.createPayment(response);
        if (payment.getOrder().getOrderStatus() == OrderStatus.PAID) {
            // 직원에게 이메일(worker1@email.com) 전송
            sendOrderPaidEmail("jbpark103@hanmail.net",
                    payment.getOrder().getOrderName(),
                    BsUtils.getMoneyString(payment.getTotalAmount()));

        }

        int statusCode = response.containsKey("error") ? 400:200;

        if (statusCode==200) {
            String email = principal.getName();
            int del = orderServ.deleteOrdersByUserIdWithoutPayments(email);
            logger.info("'{}'가 결제하지 않아 삭제된 주문 수: {}",
                    email, del);

            return ResponseEntity.status(statusCode).body(
                    new ApiResp("나의 주문 페이지", null));
        } else {
            return ResponseEntity.status(statusCode).body(
                    new ApiResp("결제 확정 실패", null));
        }
    }

    private void sendOrderPaidEmail(String email,
                                    String orderName, String payment)
            throws MessagingException, UnsupportedEncodingException {
        BsUser user = userServ.getByEmail(email);
        String subject = "비누 주문/결제완료 안내";
        String senderName = "범이비누";
        String EMAIL_TEMPLATE = """
                <p>안녕하세요? '%s' 발주 담당 직원님</p>
                <p>범이비누 주문 건을 알려드립니다. 주문 내역:</p>
                <ul>
                    <li>주문명칭: %s</li>
                    <li>결제금액: %s원</li>
                </ul>
                <br>- 범이비누 주문/결제 체계""";

        String content = String.format(EMAIL_TEMPLATE,
                user.getFullName(), orderName, payment);

        emailManager.sendMail(user.getEmail(), subject, senderName,
                content);
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
