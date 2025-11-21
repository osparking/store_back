package com.bumsoap.store.dto;

import com.bumsoap.store.util.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class OrderTableRow {
  private String orderId;
  private LocalDateTime orderTime;
  private OrderStatus orderStatus;
  private String orderName;
  private String customer;
  private String recipient;
  private Long user_id;
  private BigDecimal payment;
  public String getOrderStatus() {
    return orderStatus.label;
  }
}
