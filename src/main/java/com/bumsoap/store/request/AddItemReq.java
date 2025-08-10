package com.bumsoap.store.request;

import com.bumsoap.store.util.BsShape;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddItemReq {
  private String shape;
  private int count;

  public BsShape getShape() {
    return BsShape.valueOfLabel(shape);
  }
}
