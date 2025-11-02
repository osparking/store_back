package com.bumsoap.store.controller;

import com.bumsoap.store.dto.CheckAmountResult;
import com.bumsoap.store.dto.OrderInfo;
import com.bumsoap.store.request.SaveAmountReq;
import com.bumsoap.store.util.UrlMap;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UrlMap.PAYMENTS)
public class PaymentCon {

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
    public ResponseEntity<?> confirmPayment() {
        return ResponseEntity.ok("결제 최종 승인됨.");
    }
}
