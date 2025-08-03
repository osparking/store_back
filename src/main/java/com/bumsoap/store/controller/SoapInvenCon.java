package com.bumsoap.store.controller;

import com.bumsoap.store.model.SoapInven;
import com.bumsoap.store.request.InvenUpdateReq;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.soap.InvenServI;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UrlMap.SOAP)
@RequiredArgsConstructor
public class SoapInvenCon {
  private final InvenServI invenServ;

  @PutMapping(UrlMap.UPDATE)
  public ResponseEntity<ApiResp> update(@RequestBody InvenUpdateReq request) {
    try {
      SoapInven soapInven = invenServ.getByBsShape(request.getBsShape());
      soapInven.setStockLevel(
          request.getStockDiff() + soapInven.getStockLevel());
      SoapInven updatedSoap = invenServ.add(soapInven);
      return ResponseEntity.ok(
          new ApiResp(Feedback.INVEN_UPDATE_SUCC, updatedSoap));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
          new ApiResp(e.getMessage(), null));
    }
  }
}
