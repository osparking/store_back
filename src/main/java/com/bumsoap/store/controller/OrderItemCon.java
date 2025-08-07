package com.bumsoap.store.controller;

import com.bumsoap.store.model.OrderItem;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Controller
@RequestMapping(UrlMap.ORDER)
@RequiredArgsConstructor
public class OrderItemCon {

  @PostMapping(UrlMap.ADD_ORDER_ITEM)
  public ResponseEntity<ApiResp> addOrderItem(
      @RequestBody OrderItem orderItem) {
    try {
      Object itemSaved = null;
      return ResponseEntity.ok(
          new ApiResp(Feedback.ORDER_ITEM_SAVED, itemSaved));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResp(e.getMessage(), null));
    }
  }
}
