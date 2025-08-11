package com.bumsoap.store.dto;

import com.bumsoap.store.util.BsShape;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShapeCount {
  private BsShape shape;
  private int count;
}
