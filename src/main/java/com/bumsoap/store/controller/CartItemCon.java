package com.bumsoap.store.controller;

import com.bumsoap.store.dto.ObjMapper;
import com.bumsoap.store.exception.InventoryException;
import com.bumsoap.store.model.CartItem;
import com.bumsoap.store.request.AddCartItemReq;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.cartItem.CartItemServI;
import com.bumsoap.store.service.user.UserServInt;
import com.bumsoap.store.util.BsUtils;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(UrlMap.CART)
public class CartItemCon {
  private final CartItemServI cartItemServ;
  private final ObjMapper objMapper;
  private final UserServInt userServ;

  @GetMapping(UrlMap.GET_BY_USERID)
  public ResponseEntity<ApiResp> getCartByUserId(@PathVariable Long uid) {
    try {
      if (BsUtils.isQualified(uid, false, null)) {
        // 존재하는 uid 인지 확인. 없으면 예외 투척
        // 그 유저 카트 항목 모두 읽기
        List<CartItem> items = null;
        return ResponseEntity.ok(new ApiResp(Feedback.CART_FOUND, items));
      } else {
        return ResponseEntity.status(UNAUTHORIZED).body(
            new ApiResp(Feedback.NOT_QUALIFIED_FOR + uid, null));
      }
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResp(e.getMessage(), null));
    }
  }

  @PostMapping(UrlMap.ADD_CART_ITEM)
  public ResponseEntity<ApiResp> addItem(
      @RequestBody AddCartItemReq addCartItemReq) {
    try {
      var user = userServ.findById(addCartItemReq.getUserId());
      var item = objMapper.mapToDto(addCartItemReq, CartItem.class);

      item.setUser(user);

      CartItem savedItem = cartItemServ.saveItem(item);

      return ResponseEntity.ok(
          new ApiResp(Feedback.CART_ITEM_SAVED, savedItem));
    } catch (InventoryException e) {
      return ResponseEntity.status(BAD_REQUEST)
          .body(new ApiResp(e.getMessage(), null));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResp(e.getMessage(), null));
    }
  }
}
