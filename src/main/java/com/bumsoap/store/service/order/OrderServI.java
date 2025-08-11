package com.bumsoap.store.service.order;

import com.bumsoap.store.model.BsOrder;

public interface OrderServI {
  BsOrder saveOrder(BsOrder order);

  BsOrder findOrderById(Long id);
}
