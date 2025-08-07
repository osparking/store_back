package com.bumsoap.store.request;

import com.bumsoap.store.util.BsShape;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddOrderItemReq {
  private String shape;
  private int count;

  public BsShape getShape() {
    return BsShape.valueOfLabel(shape);
  }
}
