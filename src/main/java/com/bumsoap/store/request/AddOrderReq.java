package com.bumsoap.store.request;

import com.bumsoap.store.util.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AddOrderReq {
  private Long id;
  private Long userId;
  private String orderName;
  private List<AddItemReq> items; // 예, 백설공주 2개
  private RecipRegiReq recipRegiReq;
  private String orderStatus;
  private String defaultRecipientAction;

  public OrderStatus getOrderStatus() {
    return OrderStatus.valueOfLabel(orderStatus);
  }
}
