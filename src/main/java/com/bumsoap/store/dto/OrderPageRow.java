package com.bumsoap.store.dto;

import com.bumsoap.store.util.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class OrderPageRow {
  private String orderId;
  private LocalDateTime orderTime;
  private OrderStatus orderStatus;
  private String orderName;
  private String customer;
  private String recipient;
  private Long user_id;
  private BigDecimal payment;

  public OrderPageRow(String orderId, Timestamp orderTime,
                      int orderStatus, String orderName,
                      String customer, String recipient,
                      Long user_id, BigDecimal payment) {
    this.orderId = orderId;
    this.orderTime = orderTime.toLocalDateTime();
    this.orderStatus = OrderStatus.values()[orderStatus];
    this.orderName = orderName;
    this.customer = customer;
    this.recipient = recipient;
    this.user_id = user_id;
    this.payment = payment;
  }

  public String getOrderStatus() {
    return orderStatus.label;
  }
}
