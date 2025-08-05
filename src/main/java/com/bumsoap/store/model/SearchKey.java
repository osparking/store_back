package com.bumsoap.store.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 주소 검색 키 단어
 */
@Data
@AllArgsConstructor
public class SearchKey {
  @NotEmpty
  @Size(min = 3, max = 20)
  private String addrKey;
}