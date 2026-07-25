package com.bumsoap.store.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class FeeOtherAddReq {
  private BigDecimal deliFreeMin; // 무료 배송 최소 구매 금액
  private BigDecimal islandAdd; // 도서지역 배송비 할증액
}
