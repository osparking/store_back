package com.bumsoap.store.service.order;

import com.bumsoap.store.model.BsOrder;
import com.bumsoap.store.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServ implements OrderServI {
  private final OrderRepo orderRepo;

  @Override
  public BsOrder saveOrder(BsOrder order) {
    BsOrder savedOrder = orderRepo.save(order);
    return savedOrder;
  }
}

