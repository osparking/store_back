package com.bumsoap.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyOrderDto {
    private LocalDateTime paymentTime;
    private String orderId;
    private String orderName;
    private String recipientName;
    private BigDecimal paymentAmount;
    private String receiptUrl;
}
