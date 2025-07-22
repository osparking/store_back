package com.bumsoap.store.controller;

import com.bumsoap.store.dto.EntityConverter;
import com.bumsoap.store.dto.StoreIngreDto;
import com.bumsoap.store.model.StoreIngre;
import com.bumsoap.store.repository.StoreIngreRepoI;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping(UrlMap.STORE_INGRED)
@RequiredArgsConstructor
public class StoreIngreCon {
  private final StoreIngreRepoI storeIngreRepo;
  private final EntityConverter<StoreIngre, StoreIngreDto> stInConverter;

  @GetMapping(UrlMap.GET_ALL)
  public ResponseEntity<ApiResp> getAllStoredIngred() {
    try {
      var storedIngre = storeIngreRepo.findAll();
      List<StoreIngreDto> storeIngreDtos = storedIngre.stream()
          .map(entity -> stInConverter.mapEntityToDto
              (entity, StoreIngreDto.class))
          .collect(Collectors.toList());
      return ResponseEntity.ok().body(
          new ApiResp("입고 재료 전체 목록", storeIngreDtos));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResp(e.getMessage(), null));
    }
  }
}
