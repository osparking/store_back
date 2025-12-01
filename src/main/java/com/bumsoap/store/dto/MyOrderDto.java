package com.bumsoap.store.dto;

import com.bumsoap.store.util.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MyOrderDto {
    private LocalDateTime paymentTime;
    private String orderId;
    private String orderName;
    private OrderStatus orderStatus;
    private String recipientName;
    private BigDecimal paymentAmount;
    private String receiptUrl;
    private Long id;

    public MyOrderDto(Timestamp payTime, String orderId, String orderName,
                      int orderStatus, String recipientName,
                      BigDecimal paymentAmount, String receiptUrl, Long id) {
        this.paymentTime = payTime.toLocalDateTime();
        this.orderId = orderId;
        this.orderName = orderName;
        this.orderStatus = OrderStatus.values()[orderStatus];
        this.recipientName = recipientName;
        this.paymentAmount = paymentAmount;
        this.receiptUrl = receiptUrl;
        this.id = id;
    }

    public String getOrderStatus() {
        return orderStatus.label;
    }
}
