package com.bumsoap.store.controller;

import com.bumsoap.store.dto.AddressBasisDto;
import com.bumsoap.store.model.SearchKey;
import com.bumsoap.store.model.SearchResult;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.address.AddressServI;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(UrlMap.ORDER)
public class AddressCon {

  @Autowired
  private AddressServI addressServ;

  @GetMapping(UrlMap.ADDRESS_SEARCH)
  public ResponseEntity<ApiResp> addressSearch(
      @Valid @RequestParam("searchKey") SearchKey searchKey,
      @RequestParam("page") Optional<Integer> page,
      @RequestParam("size") Optional<Integer> size) {

    try {
      int currentPage = page.orElse(1);
      int pageSize = size.orElse(10);
      Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
      Page<AddressBasisDto> addressPage = addressServ.findPaginated(searchKey,
          pageable);

      int totalPages = addressPage.getTotalPages();

      List<Integer> pageNumbers = null;
      if (totalPages > 0) {
        pageNumbers = IntStream.rangeClosed(1, totalPages)
            .boxed()
            .collect(Collectors.toList());
      }

      var result = new SearchResult(addressPage,
          addressPage.getNumber() + 1,
          totalPages,
          pageNumbers
      );
      return ResponseEntity.ok(new ApiResp(Feedback.ADDRESS_FOUND, result));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(
          new ApiResp(Feedback.DEPTS_READ_FAILURE, null ));
    }
  }
}