package com.bumsoap.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ShapeSelItem {
  private String shapeLabel;
  private int count;
  private BigDecimal price;
}