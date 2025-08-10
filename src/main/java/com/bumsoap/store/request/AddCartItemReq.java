package com.bumsoap.store.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddCartItemReq extends AddItemReq {
  private Long userId;
}
