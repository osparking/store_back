package com.bumsoap.store.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class FeeDeliveryAddReq {
  private int boxSize; // 기본 배송비
  private BigDecimal areaSame; // 같은 권역(수도권)
  private BigDecimal areaDiff; // 다른 권역(제주 제외)
  private BigDecimal areaJeju; // 제주 권역
}
