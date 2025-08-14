package com.bumsoap.store.controller;

import com.bumsoap.store.model.FeeEtc;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.soap.FeeEtcServI;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping(UrlMap.FEE_ETC)
@RequiredArgsConstructor
public class FeeEtcCon {
  private final FeeEtcServI feeEtcServ;

  @GetMapping(UrlMap.GET_LATEST)
  public ResponseEntity<ApiResp> getFeeEtc() {
    try {
      FeeEtc feeEtc = feeEtcServ.readLatest();
      return ResponseEntity.ok(new ApiResp("배송비 등 정보", feeEtc));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResp(e.getMessage(), null));
    }
  }
}
