package com.bumsoap.store.controller;

import com.bumsoap.store.repository.StoreIngreRepoI;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping(UrlMap.STORE_INGRED)
@RequiredArgsConstructor
public class StoreIngreCon {
  private final StoreIngreRepoI storeIngreRepo;

  @GetMapping(UrlMap.GET_ALL)
  public ResponseEntity<ApiResp> getAllStoredIngred() {
    try {
      return ResponseEntity.ok().body(
          new ApiResp("입고 재료 전체 목록", storeIngreRepo.findAll()));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResp(e.getMessage(), null));
    }
  }
}
