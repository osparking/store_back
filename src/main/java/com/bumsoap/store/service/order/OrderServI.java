package com.bumsoap.store.service.order;

import com.bumsoap.store.model.BsOrder;

import java.math.BigDecimal;

public interface OrderServI {
  BsOrder saveOrder(BsOrder order);

  BigDecimal findDeliveryFee(BigDecimal grandTotal, String zipcode);

  BsOrder findOrderById(Long id);

  BsOrder updateOrder(BsOrder order);

  void deleteById(Long id);
}
