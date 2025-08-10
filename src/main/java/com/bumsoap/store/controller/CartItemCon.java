package com.bumsoap.store.controller;

import com.bumsoap.store.exception.InventoryException;
import com.bumsoap.store.model.CartItem;
import com.bumsoap.store.request.AddCartItemReq;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Controller
@RequiredArgsConstructor
@RequestMapping(UrlMap.CART)
public class CartItemCon {

  @PostMapping(UrlMap.ADD_CART_ITEM)
  public ResponseEntity<ApiResp> addItem(
      @RequestBody AddCartItemReq addCartItemReq) {
    try {
      // convert req to item
      // save item into DB by calling service method, get result
      CartItem cartItem = null; // replace null with the result

      return ResponseEntity.ok(
          new ApiResp(Feedback.CART_ITEM_SAVED, cartItem));
    } catch (InventoryException e) {
      return ResponseEntity.status(BAD_REQUEST)
          .body(new ApiResp(e.getMessage(), null));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResp(e.getMessage(), null));
    }
  }
}
