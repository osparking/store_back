package com.bumsoap.store.util;

import com.bumsoap.store.dto.ObjMapper;
import com.bumsoap.store.dto.ShapeCount;
import com.bumsoap.store.exception.InventoryException;
import com.bumsoap.store.model.OrderItem;
import com.bumsoap.store.service.produce.ProduceServI;
import com.bumsoap.store.service.soap.PriceServI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class SubTotaler {
  private final PriceServI priceServ;
  private final ObjMapper objMapper;
  private final ProduceServI produceServ;

  public BigDecimal getSubtotal(ShapeCount item) {
    /**
     * 재고 검사 및 예외 발생
     */
    Long stock = produceServ.getStockByShape(item.getShape());
    
    if (stock < item.getCount()) {
      StringBuffer sb = new StringBuffer(Feedback.SHORT_INVENTORY);
      sb.append(item.getShape().label);
      sb.append(" - ");
      sb.append(stock);
      throw new InventoryException(sb.toString());
    }
    var subTotal = priceServ.findSoapPrice(item.getShape())
        .multiply(BigDecimal.valueOf(item.getCount()));
    return subTotal.setScale(0, RoundingMode.HALF_UP);
  }

  public BigDecimal getSubtotal(OrderItem item) {
    var shapeCount = objMapper.mapToDto(item, ShapeCount.class);
    return getSubtotal(shapeCount);
  }
}
