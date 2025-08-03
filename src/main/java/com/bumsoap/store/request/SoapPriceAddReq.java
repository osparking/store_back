package com.bumsoap.store.request;

import com.bumsoap.store.util.BsShape;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class SoapPriceAddReq {
  private BsShape bsShape;
  private BigDecimal unitPrice;

  public void setBsShape(String bsShapelabel) {
    this.bsShape = BsShape.valueOfLabel(bsShapelabel);
  }
  public void setUnitPrice(String priceStr) {
    this.unitPrice = new BigDecimal(priceStr);
  }
}
