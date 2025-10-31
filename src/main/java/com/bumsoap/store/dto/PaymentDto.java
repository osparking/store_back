package com.bumsoap.store.dto;

import com.bumsoap.store.util.TossPaymentMethod;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long id;
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    private BigDecimal totalAmount;
    private String method;
    private String receiptUrl;
    private String paymentKey;
    private String orderId;
    private String orderName;

    public PaymentDto(Long id, LocalDateTime approvedAt,
                      BigDecimal totalAmount, TossPaymentMethod method,
                      String receiptUrl, String paymentKey,
                      String orderId, String orderName) {
        this.id = id;
        this.approvedAt = approvedAt;
        this.totalAmount = totalAmount;
        this.method = method.toString();
        this.receiptUrl = receiptUrl;
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.orderName = orderName;
    }
}
