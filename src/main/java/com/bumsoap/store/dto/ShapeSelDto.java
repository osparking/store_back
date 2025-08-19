package com.bumsoap.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ShapeSelDto {
  private List<ShapeSelItem> shapeLabelList;
  private String defaultShape;
}
