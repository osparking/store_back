package com.bumsoap.store.service.order;

import com.bumsoap.store.exception.IdNotFoundEx;
import com.bumsoap.store.exception.InventoryException;
import com.bumsoap.store.exception.OrderIdNotFoundEx;
import com.bumsoap.store.model.BsOrder;
import com.bumsoap.store.model.FeeEtc;
import com.bumsoap.store.model.OrderItem;
import com.bumsoap.store.repository.OrderItemRepo;
import com.bumsoap.store.repository.OrderRepo;
import com.bumsoap.store.service.address.AddressBasisServI;
import com.bumsoap.store.service.orderItem.OrderItemServI;
import com.bumsoap.store.service.recipient.RecipientServI;
import com.bumsoap.store.service.soap.FeeEtcServI;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.OrderIdGenerator;
import com.bumsoap.store.util.SubTotaler;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServ implements OrderServI {
  private final OrderRepo orderRepo;
  private final OrderItemServI orderItemServ;
  private final OrderItemRepo orderItemRepo;
  private final SubTotaler subTotaler;
  private final RecipientServI recipientServ;
  private final AddressBasisServI addressBasisServ;
  private final FeeEtcServI feeEtcServ;

  @Autowired
  private OrderIdGenerator orderIdGenerator;

  @PersistenceContext
  private EntityManager entityManager;

  public BsOrder getOrderByOrderId(String orderId)
          throws OrderIdNotFoundEx {
    var optionalOrder = orderRepo.findByOrderId(orderId);

    if (optionalOrder.isPresent()) {
      return optionalOrder.get();
    } else {
      throw new OrderIdNotFoundEx(Feedback.ORDER_ID_NOT_FOUND + orderId);
    }
  }

  @Override
  @Transactional(rollbackOn = InventoryException.class)
  public BsOrder saveOrder(BsOrder order) {
    // recipient 저장 및 order 속성 변경
    // 상향식 순서로 하위 요소들을 저장하고, 저장 결과로 기존 멤버를 대체!
    var recipient = order.getRecipient();
    var basis = addressBasisServ.addGetAddrBasis(recipient.getAddressBasis());
    recipient.setAddressBasis(basis);

    var recipientSaved = recipientServ.save(recipient);
    order.setRecipient(recipientSaved);
    order.getItems().forEach(item ->
        item.setSubTotal(subTotaler.getSubtotal(item)));
    order.setPayment(calculatePayment(order));

    order.setOrderId("");
    entityManager.persist(order);
    entityManager.flush();

    String orderId = orderIdGenerator.generateOrderId(order);

    order.setOrderId(orderId);
    order.getItems().forEach(item -> item.setOrder(order));

    var savedItems = orderItemServ.saveOrderItems(order.getItems());
    order.setItems(savedItems);

    return order;
  }

  private BigDecimal calculatePayment(BsOrder order) {
    // 각 항목의 소계를 합한다.
    BigDecimal payment = new BigDecimal(0);
    for (var item : order.getItems()) {
      payment = payment.add(item.getSubTotal());
    }

    // 배송비 계산
    FeeEtc feeEtc = feeEtcServ.readLatest();
    BigDecimal delivery =
        payment.compareTo(feeEtc.getDeliFreeMin()) >= 0
            ? BigDecimal.ZERO : feeEtc.getDeliBasis();

    boolean isJeju = order.getRecipient().getAddressBasis()
        .getZipcode().startsWith("63");
    return payment.add(
        isJeju ? delivery.add(feeEtc.getDeliJeju()) : delivery);
  }

  @Override
  public BigDecimal findDeliveryFee(
      BigDecimal grandTotal, String zipcode) {

    FeeEtc feeEtc = feeEtcServ.readLatest();
    BigDecimal delivery =
        grandTotal.compareTo(feeEtc.getDeliFreeMin()) >= 0
            ? BigDecimal.ZERO : feeEtc.getDeliBasis();

    return zipcode.startsWith("63")
        ? delivery.add(feeEtc.getDeliJeju()) : delivery;
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
