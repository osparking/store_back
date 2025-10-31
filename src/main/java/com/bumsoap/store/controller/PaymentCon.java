package com.bumsoap.store.controller;

import com.bumsoap.store.dto.CheckAmountResult;
import com.bumsoap.store.util.UrlMap;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping(UrlMap.PAYMENTS)
public class PaymentCon {

    @PostMapping("/checkAmount")
    public ResponseEntity<CheckAmountResult> checkIfAmountMatches(
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
}
