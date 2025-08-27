package com.bumsoap.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto extends ShapeCount{
  private long id;
  private LocalDateTime addTime; // 주문일시
  private BigDecimal subTotal;
  private String shapeLabel;

  public String getShapeLabel() {
    return super.getShape().toString();
  }
}
