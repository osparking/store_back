package com.bumsoap.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddressBasisDto {
  private String zipcode;
  private String roadAddress; // 도로명 주소
  private String zBunAddress; // 지번 주소
}