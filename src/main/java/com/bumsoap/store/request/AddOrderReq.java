package com.bumsoap.store.request;

import com.bumsoap.store.model.Recipient;
import com.bumsoap.store.util.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AddOrderReq {
  private Long id;
  private Long userId;
  private List<AddItemReq> items; // 예, 백설공주 2개
  private Recipient recipient;
  private String orderStatus;

  public OrderStatus getOrderStatus() {
    return OrderStatus.valueOfLabel(orderStatus);
  }
}
