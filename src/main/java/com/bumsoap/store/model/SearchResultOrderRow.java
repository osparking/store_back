package com.bumsoap.store.model;

import com.bumsoap.store.dto.MyOrderRow;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResultOrderRow {
  private Page<MyOrderRow> searchedPage;
  private int currentPage;
  private int totalPages;
  private List<Integer> pageNumbers;
}
