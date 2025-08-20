package com.bumsoap.store.controller;

import com.bumsoap.store.dto.ShapeSelItem;
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

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping(UrlMap.SOAP)
@RequiredArgsConstructor
public class SoapInvenCon {
  private final InvenServI invenServ;

  @GetMapping(UrlMap.SOAP_SHAPES)
  public ResponseEntity<ApiResp> getShapeSelectionData() {
    try {
      List<ShapeSelItem> shapeSelItems = invenServ.getShapeSelItems();

      return ResponseEntity.ok(
          new ApiResp(Feedback.ORDER_FOUND, shapeSelItems));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResp(e.getMessage(), null));
    }
  }

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
