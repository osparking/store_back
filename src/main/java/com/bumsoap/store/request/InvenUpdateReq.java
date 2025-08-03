package com.bumsoap.store.request;

import com.bumsoap.store.util.BsShape;
import lombok.Data;

@Data
public class InvenUpdateReq {
  private BsShape bsShape;
  private int stockDiff;
  public void setBsShape(String bsShapelabel) {
    this.bsShape = BsShape.valueOfLabel(bsShapelabel);
  }
}
