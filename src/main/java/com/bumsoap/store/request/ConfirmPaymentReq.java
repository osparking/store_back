package com.bumsoap.store.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmPaymentReq {
    private String orderId;
    private BigDecimal amount;
    private String paymentKey;
}