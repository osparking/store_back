package com.bumsoap.store.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddrBasisAddReq {
  private String zipcode;
  private String roadAddress; // 도로명 주소
  private String zBunAddress; // 지번 주소
}
