package com.bumsoap.store.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
/*
 배송비 계산 요청 바디 자료
 우편번호와 총 금액에 의하여 배송비는 결정된다.
 예) {"zipcode":"12915","grandTotal":3900 }
 */
public class DeliveryFeeReq {
  private String zipcode;
  private BigDecimal grandTotal;
}
