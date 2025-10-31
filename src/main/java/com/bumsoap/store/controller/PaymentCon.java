package com.bumsoap.store.controller;

import com.bumsoap.store.dto.CheckAmountResult;
import com.bumsoap.store.request.SaveAmountReq;
import com.bumsoap.store.util.UrlMap;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(UrlMap.PAYMENTS)
public class PaymentCon {

    @PostMapping("/checkAmount")
    public ResponseEntity<CheckAmountResult> checkIfAmountMatches(
            HttpSession session,
            @RequestBody SaveAmountReq orderAmount) {
        var savedAmount = (BigDecimal) session
                .getAttribute(orderAmount.getOrderId());
        if (savedAmount==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new CheckAmountResult(false));
        } else
            return ResponseEntity.status(HttpStatus.OK).body(
                    new CheckAmountResult(orderAmount.getAmount()
                            .equals(savedAmount)));
    }
}
