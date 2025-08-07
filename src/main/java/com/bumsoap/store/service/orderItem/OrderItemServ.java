package com.bumsoap.store.service.orderItem;

import com.bumsoap.store.model.OrderItem;
import com.bumsoap.store.service.soap.PriceServI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class OrderItemServ implements OrderItemServI {
  private final PriceServI priceServ;

  @Override
  public OrderItem save(OrderItem item) {
    var subTotal = priceServ.findSoapPrice(item.getShape())
        .multiply(BigDecimal.valueOf(item.getCount()));
    item.setSubTotal(subTotal.setScale(0, RoundingMode.HALF_UP));
    return null;
  }
}
