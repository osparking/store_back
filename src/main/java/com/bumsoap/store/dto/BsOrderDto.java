package com.bumsoap.store.dto;

import com.bumsoap.store.model.Recipient;
import com.bumsoap.store.util.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class BsOrderDto {
  @Autowired
  private long id;
  private List<OrderItemDto> items;
  private Recipient recipient;

  private BigDecimal payment;
  private LocalDateTime orderTime;
  private OrderStatus orderStatus;
  public String getOrderStatus() {
    return orderStatus.label;
  }
}
