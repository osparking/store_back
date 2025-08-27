package com.bumsoap.store.dto;

import lombok.Data;

@Data
public class CartItemCount {
  private long id;
  private int count;

  @Override
  public String toString() {
    return "카트 항목 : {" +
        "ID=" + id +
        ", 수량=" + count +
        '}';
  }
}