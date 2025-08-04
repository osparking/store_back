package com.bumsoap.store.request;

import com.bumsoap.store.util.BsShape;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SoapPriceReq {
  private BsShape bsShape;

  public void setBsShape(String bsShapelabel) {
    this.bsShape = BsShape.valueOfLabel(bsShapelabel);
  }
}
