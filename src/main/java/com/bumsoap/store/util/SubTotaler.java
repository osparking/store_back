package com.bumsoap.store.util;

import com.bumsoap.store.dto.CartItemDto;
import com.bumsoap.store.exception.InventoryException;
import com.bumsoap.store.service.soap.InvenServI;
import com.bumsoap.store.service.soap.PriceServI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class SubTotaler {
  private final InvenServI invenServ;
  private final PriceServI priceServ;

  public BigDecimal getSubtotal(CartItemDto item) {
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
}
