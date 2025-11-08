package com.bumsoap.store.dto;

import com.bumsoap.store.util.BsUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class MyOrderDto {
    private String paymentTime;
    private String orderId;
    private String orderName;
    private String recipientName;
    private BigDecimal paymentAmount;
    private String receiptUrl;

    public MyOrderDto(Timestamp payTime, String orderId,
                      String orderName, String recipientName,
                      BigDecimal paymentAmount, String receiptUrl) {
        this.paymentTime = BsUtils.getLocalDateTimeStr(
                        payTime.toLocalDateTime());
        this.orderId = orderId;
        this.orderName = orderName;
        this.recipientName = recipientName;
        this.paymentAmount = paymentAmount;
        this.receiptUrl = receiptUrl;
    }
}
