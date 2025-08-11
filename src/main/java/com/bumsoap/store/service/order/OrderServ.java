package com.bumsoap.store.service.order;

import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.exception.InventoryException;
import com.bumsoap.store.model.BsOrder;
import com.bumsoap.store.model.OrderItem;
import com.bumsoap.store.repository.OrderRepo;
import com.bumsoap.store.service.orderItem.OrderItemServI;
import com.bumsoap.store.util.Feedback;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServ implements OrderServI {
  private final OrderRepo orderRepo;
  private final OrderItemServI orderItemServ;

  @Override
  @Transactional(rollbackOn = InventoryException.class)
  public BsOrder saveOrder(BsOrder order) {
    BsOrder savedOrder = orderRepo.save(order);
    order.getItems().forEach(item -> item.setOrder(savedOrder));

    List<OrderItem> savedItems = orderItemServ.saveOrderItems(order.getItems());
    order.setItems(savedItems);
    return savedOrder;
  }

  @Override
  public BsOrder findOrderById(Long id) {
    return orderRepo.findById(id).orElseThrow(
        () -> new IdNotFoundEx(Feedback.ORDER_ID_NOT_FOUND + id));
  }
}
