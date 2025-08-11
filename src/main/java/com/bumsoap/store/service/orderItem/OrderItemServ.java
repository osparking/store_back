package com.bumsoap.store.service.orderItem;

import com.bumsoap.store.model.OrderItem;
import com.bumsoap.store.repository.OrderItemRepo;
import com.bumsoap.store.util.SubTotaler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServ implements OrderItemServI {
  private final OrderItemRepo orderItemRepo;
  private final SubTotaler subTotaler;

  @Override
  public OrderItem save(OrderItem item) {
    item.setSubTotal(subTotaler.getSubtotal(item));

    return orderItemRepo.save(item);
  }

  @Override
  public List<OrderItem> saveOrderItems(List<OrderItem> items) {
    items.forEach(item ->
        item.setSubTotal(subTotaler.getSubtotal(item)));

    return orderItemRepo.saveAll(items);
  }
}
