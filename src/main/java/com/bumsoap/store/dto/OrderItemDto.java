package com.bumsoap.store.dto;

import com.bumsoap.store.util.BsShape;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
  private Long id;
  private String shape;
  private int count;
  private BigDecimal subTotal; // 일종의 정보 중복
  public void setShape(BsShape shape) {
    this.shape = shape.label;
  }
}
