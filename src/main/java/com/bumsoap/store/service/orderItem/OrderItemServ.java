package com.bumsoap.store.service.orderItem;

import com.bumsoap.store.exception.InventoryException;
import com.bumsoap.store.model.OrderItem;
import com.bumsoap.store.repository.OrderItemRepo;
import com.bumsoap.store.service.soap.InvenServI;
import com.bumsoap.store.service.soap.PriceServI;
import com.bumsoap.store.util.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServ implements OrderItemServI {
  private final PriceServI priceServ;
  private final OrderItemRepo orderItemRepo;
  private final InvenServI invenServ;

  private BigDecimal getSubTotal(OrderItem item) {
    /**
     * 재고 검사 및 예외 발생
     */
    int level = invenServ.getByBsShape(item.getShape()).getStockLevel();
    if (level < item.getCount()) {
      StringBuffer sb = new StringBuffer(Feedback.SHORT_INVENTORY);
      sb.append(item.getShape().label);
      sb.append(" - ");
      sb.append(level);
      throw new InventoryException(sb.toString());
    }
    var subTotal = priceServ.findSoapPrice(item.getShape())
        .multiply(BigDecimal.valueOf(item.getCount()));
    return subTotal.setScale(0, RoundingMode.HALF_UP);
  }

  @Override
  public OrderItem save(OrderItem item) {
    item.setSubTotal(getSubTotal(item));

    return orderItemRepo.save(item);
  }

  @Override
  public List<OrderItem> saveOrderItems(List<OrderItem> items) {
    items.forEach(item -> item.setSubTotal(getSubTotal(item)));

    return orderItemRepo.saveAll(items);
  }
}
