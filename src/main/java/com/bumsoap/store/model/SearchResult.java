package com.bumsoap.store.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResult {
  private Page<AddressDJ> addressPage;
  private int currentPage;
  private int totalPages;
  private List<Integer> pageNumbers;
}
