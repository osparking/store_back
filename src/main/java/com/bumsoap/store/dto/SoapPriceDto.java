package com.bumsoap.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SoapPriceDto {
  int shapeOrdinal;
  BigDecimal unitPrice;
}
