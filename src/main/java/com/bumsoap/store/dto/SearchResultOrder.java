package com.bumsoap.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResultOrder {
    private Page<MyOrderDto> addressPage;
    private int currentPage;
    private int totalPages;
    private List<Integer> pageNumbers;
}
