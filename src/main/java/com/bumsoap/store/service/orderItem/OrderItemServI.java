package com.bumsoap.store.service.orderItem;

import com.bumsoap.store.model.OrderItem;

import java.util.List;

public interface OrderItemServI {
  OrderItem save(OrderItem orderItem);

  List<OrderItem> saveOrderItems(List<OrderItem> items);
}
