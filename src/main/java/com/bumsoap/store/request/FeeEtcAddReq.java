package com.bumsoap.store.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class FeeEtcAddReq {
  private BigDecimal deliBasis; // 기본 배송비
  private BigDecimal deliJeju; // 제주도
  private BigDecimal deliIsol; // 벽오지(제주 외 도서 산간)
  private BigDecimal deliFreeMin; // 무배 최소 주문액
}
