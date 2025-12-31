package com.bumsoap.store.controller;

import com.bumsoap.store.dto.ShapeSelItem;
import com.bumsoap.store.model.SoapInven;
import com.bumsoap.store.request.InvenUpdateReq;
import com.bumsoap.store.request.SoapPriceReq;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.order.OrderServI;
import com.bumsoap.store.service.soap.InvenServI;
import com.bumsoap.store.service.soap.PriceServI;
import com.bumsoap.store.util.BsShape;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping(UrlMap.SOAP)
@RequiredArgsConstructor
public class SoapCon {
  private final InvenServI inventoryServ;
  private final PriceServI priceServ;
  private final OrderServI orderServ;

  @GetMapping(UrlMap.SOAP_SHAPES)
  public ResponseEntity<ApiResp> getShapeSelectionData() {
    try {
      List<ShapeSelItem> shapeSelItems = inventoryServ.getShapeSelItems();

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
      SoapInven soapInven = inventoryServ.getByBsShape(request.getBsShape());
      soapInven.setStockLevel(
              request.getStockDiff() + soapInven.getStockLevel());
      SoapInven updatedSoap = inventoryServ.add(soapInven);
      return ResponseEntity.ok(
              new ApiResp(Feedback.INVEN_UPDATE_SUCC, updatedSoap));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
              new ApiResp(e.getMessage(), null));
    }
  }

  @GetMapping(UrlMap.SOAP_PRICE)
  public ResponseEntity<ApiResp> getSoapPrice(@RequestBody SoapPriceReq request) {
    try {
      var price = priceServ.findSoapPrice(request.getBsShape());
      if (price == null) {
        String msg = "존재하지 않는 비누 외형: " + request.getBsShape().toString();
        return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND)
            .body(new ApiResp(msg, null));
      } else {
        return ResponseEntity.ok(new ApiResp("비누 가격", price.toBigInteger()));
      }
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResp(e.getMessage(), null));
    }
  }

  @GetMapping(UrlMap.SHAPE_PRICE)
  public ResponseEntity<ApiResp> getSoapPrice(@PathVariable String shape) {
    try {
      BsShape bsShape = BsShape.valueOf(shape.toUpperCase());
      var price = priceServ.findSoapPrice(bsShape);
      if (price == null) {
        String msg = "존재하지 않는 비누 외형: " + shape;
        return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND)
            .body(new ApiResp(msg, null));
      } else {
        return ResponseEntity.ok(new ApiResp("비누 가격", price.toBigInteger()));
      }
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResp(e.getMessage(), null));
    }
  }

  @GetMapping(UrlMap.REVIEW_PAGE)
  public ResponseEntity<ApiResp> getReviewPage(
          @RequestParam("page") Integer page,
          @RequestParam("size") Integer size) {

    try {
      var result = orderServ.serviceReviewPage(page, size);
      return ResponseEntity.ok(
              new ApiResp(Feedback.REVIEW_PAGE_FOUND, result));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(
              new ApiResp(Feedback.REVIEW_PAGE_FAILURE, e.getMessage()));
    }
  }

  @GetMapping(UrlMap.AVERAGE_STARS)
  public ResponseEntity<ApiResp> getAverageStars() {
    Float averageStars = orderServ.getAverageStars();
    return ResponseEntity.ok(
            new ApiResp(Feedback.AVERAGE_STARS_FOUND, averageStars));
  }

  @GetMapping(UrlMap.GET_REVIEW_INFO)
  public ResponseEntity<ApiResp> getReviewInfo(
          @PathVariable("oId") Long oId) {
    try {
      // 주문 id 로 주문 후기를 읽음
      var orderDetailDto = orderServ.serviceReviewInfo(oId);
      return ResponseEntity.ok(
              new ApiResp(Feedback.ORDER_FOUND, orderDetailDto));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
              .body(new ApiResp(e.getMessage(), null));
    }
  }
}
