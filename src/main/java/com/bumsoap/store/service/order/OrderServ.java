package com.bumsoap.store.service.order;

import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.exception.InventoryException;
import com.bumsoap.store.model.BsOrder;
import com.bumsoap.store.model.OrderItem;
import com.bumsoap.store.repository.OrderItemRepo;
import com.bumsoap.store.repository.OrderRepo;
import com.bumsoap.store.service.orderItem.OrderItemServI;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.SubTotaler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServ implements OrderServI {
  private final OrderRepo orderRepo;
  private final OrderItemServI orderItemServ;
  private final OrderItemRepo orderItemRepo;
  private final SubTotaler subTotaler;

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

  @Override
  @Transactional(rollbackOn = InventoryException.class)
  public BsOrder updateOrder(BsOrder order) {
    var readOrder = findOrderById(order.getId());
    var idsToKeep = order.getItems().stream()
        .map(OrderItem::getId)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());

    // 제거, 새 주문 목록에 없는 기존 목록 항목 제거
    readOrder.getItems().removeIf(
        item -> {
          var itemId = item.getId();
          if (idsToKeep.contains(itemId)) {
            return false;
          } else {
            orderItemRepo.deleteById(itemId);
            return true;
          }
        });

    // 추가, 새 목록 중, ID가 null 인 항목 수집
    var newItems = order.getItems().stream()
        .filter(item -> item.getId() == null)
        .toList();

    newItems.forEach(item -> {
      item.setOrder(readOrder);
      item.setSubTotal(subTotaler.getSubtotal(item));
    });
    newItems.forEach(item -> readOrder.getItems().add(item));

    // 갱신, 새 목록 중, ID 가 있는 항목에 대해 읽은 주문의 항목에 성질값 복사
    order.getItems().stream()
      .filter(item -> item.getId() != null)
      .forEach(item ->
        readOrder.getItems().stream()
          .filter(readItem -> item.getId().equals(readItem.getId()))
          .findFirst()
          .ifPresent(readItem -> {
            readItem.assign(item);
            readItem.setSubTotal(subTotaler.getSubtotal(readItem));
          })
      );

    BsOrder savedOrder = orderRepo.save(readOrder);
    return savedOrder;
  }

  @Override
  public void deleteById(Long id) {
    orderRepo.deleteById(id);
  }
}
