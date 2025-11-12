package com.bumsoap.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResult<T> {
    private Page<T> pageContent;
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private List<Integer> pageNumbers;
}
