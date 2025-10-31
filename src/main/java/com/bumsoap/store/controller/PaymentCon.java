package com.bumsoap.store.controller;

import com.bumsoap.store.dto.CheckAmountResult;
import com.bumsoap.store.service.payment.PaymentService;
import com.bumsoap.store.util.UrlMap;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping(UrlMap.PAYMENTS)
public class PaymentCon {
    private static final Logger logger = LoggerFactory.getLogger(PaymentCon.class);

//    @Autowired
//    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

//    @GetMapping("/getRecentSome")
//    public ResponseEntity<List<PaymentDto>> getRecentSome(
//            @RequestParam int count) {
//
//        var result = count==999
//                ? paymentService.getAllPayments()
//                :paymentService.getRecentSome(count);
//
//        return ResponseEntity.ok(result);
//    }

//    @GetMapping("/orderInfo")
//    public ResponseEntity<BsOrder> getOrderInfo() {
//        BsOrder order = new BsOrder();
//        order.setOrderId("");
//        order.setAmount(BigDecimal.valueOf(10700));
//        order.setPayWaitTime(LocalDateTime.now());
//        order.setOrderStatus(OrderStatus.PAY_WAIT);
//        order.setOrderName("백설공주 2개 등");
//
//        var orderSaved = orderService.createOrder(order);
//        return ResponseEntity.ok(orderSaved);
//    }

//    @PostMapping("/saveOrderInfo")
//    public ResponseEntity<?> saveAmountTemporarily(
//            HttpSession session, @RequestBody SaveOrderInfoReq request) {
//        session.setAttribute(request.getOrderId(), new OrderInfo(
//                request.getAmount(), request.getOrderName()));
//        return ResponseEntity.ok("세션에 <주문 ID, 상품 정보> 항목을 저장함.");
//    }

    @PostMapping("/checkAmount")
    public ResponseEntity<CheckAmountResult> getAmountFromSession(
            HttpSession session,
            @RequestParam String orderId,
            @RequestParam BigDecimal amount
    ) {
        var savedAmount = (BigDecimal) session.getAttribute(orderId);
        if (savedAmount==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new CheckAmountResult(false));
        } else
            return ResponseEntity.status(HttpStatus.OK).body(
                    new CheckAmountResult(amount.equals(savedAmount)));
    }

//    @PostMapping(value = {"/confirm"})
//    public ResponseEntity<List<PaymentDto>> confirmPayment(
//            HttpServletRequest request,
//            @RequestBody @NonNull ConfirmPaymentReq confirmPaymentReq)
//            throws Exception {
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String confirmRequest = objectMapper.writeValueAsString(confirmPaymentReq);
//
//        if (confirmRequest==null) {
//            throw new Exception("결제 요청 객체 널 오류 발생");
//        }
//        JSONObject response = sendRequest(
//                confirmRequest,
//                System.getenv("WIDGET_SECRET_KEY"),
//                "https://api.tosspayments.com/v1/payments/confirm");
//
//        paymentService.createPayment(response);
//
//        var recentSome = paymentService.getRecentSome(1);
//        int statusCode = response.containsKey("error") ? 400:200;
//
//        return ResponseEntity.status(statusCode).body(recentSome);
//    }

//    private JSONObject sendRequest(String confirmStr, String secretKey,
//                                   String urlString) throws IOException {
//        HttpURLConnection connection = createConnection(secretKey, urlString);
//        try (OutputStream os = connection.getOutputStream()) {
//            os.write(confirmStr.getBytes(StandardCharsets.UTF_8));
//        }
//
//        try (InputStream responseStream = connection.getResponseCode()==200 ?
//                connection.getInputStream():
//                connection.getErrorStream();
//             Reader reader = new InputStreamReader(responseStream,
//                     StandardCharsets.UTF_8)) {
//            return (JSONObject) new JSONParser().parse(reader);
//        } catch (Exception e) {
//            logger.error("Error reading response", e);
//            JSONObject errorResponse = new JSONObject();
//            errorResponse.put("error", "Error reading response");
//            return errorResponse;
//        }
//    }

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
