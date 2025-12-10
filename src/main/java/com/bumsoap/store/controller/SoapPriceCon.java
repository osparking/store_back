package com.bumsoap.store.controller;

import com.bumsoap.store.request.SoapPriceReq;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.order.OrderServI;
import com.bumsoap.store.service.soap.PriceServI;
import com.bumsoap.store.util.BsShape;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping(UrlMap.SOAP)
@RequiredArgsConstructor
public class SoapPriceCon {
  private final PriceServI priceServ;
  private final OrderServI orderServ;

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
}
