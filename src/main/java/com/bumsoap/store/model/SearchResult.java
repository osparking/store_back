package com.bumsoap.store.model;

import com.bumsoap.store.dto.AddressBasisDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResult {
  private Page<AddressBasisDto> addressPage;
  private int currentPage;
  private int totalPages;
  private List<Integer> pageNumbers;
}
