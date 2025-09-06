package com.bumsoap.store.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryFeeReq {
  private String zipcode;
  private BigDecimal grandTotal;
}
