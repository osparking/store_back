package com.bumsoap.store.controller;

import com.bumsoap.store.dto.CartItemDto;
import com.bumsoap.store.dto.ObjMapper;
import com.bumsoap.store.exception.InventoryException;
import com.bumsoap.store.exception.UnauthorizedException;
import com.bumsoap.store.model.CartItem;
import com.bumsoap.store.request.AddCartItemReq;
import com.bumsoap.store.request.CartUpdateReq;
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
import java.util.function.Predicate;

import static org.springframework.http.HttpStatus.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(UrlMap.CART)
public class CartItemCon {
  private final CartItemServI cartItemServ;
  private final ObjMapper objMapper;
  private final UserServInt userServ;

  @PutMapping(UrlMap.UPDATE2)
  public ResponseEntity<ApiResp> updateUserCart(
      @RequestBody CartUpdateReq request) {
    try {
      var updatedList = cartItemServ.updateUserCart(request);
      return ResponseEntity.ok(new ApiResp(Feedback.CART_FIXED, updatedList));
    } catch (InventoryException e) {
      return ResponseEntity.status(BAD_REQUEST).body(
          new ApiResp(e.getMessage(), null));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResp(e.getMessage(), null));
    }
  }

  @DeleteMapping(UrlMap.DELETE_BY_ID)
  public ResponseEntity<ApiResp> deleteCartItem(@PathVariable Long id) {
    try {
      cartItemServ.deleteCartItem(id);
      return ResponseEntity.ok(new ApiResp(Feedback.CART_ITEM_DELETED, null));
    } catch (UnauthorizedException e) {
      return ResponseEntity.status(UNAUTHORIZED).body(
          new ApiResp(e.getMessage() + id, null));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResp(e.getMessage(), null));
    }
  }

  @PatchMapping(UrlMap.CART_ITEM_COUNT)
  public ResponseEntity<ApiResp> updateShapeCount(
      @PathVariable Long itemId, @PathVariable int count) {
    try {
      CartItem fixedItem = cartItemServ.updateShapeCount(itemId, count);
      return ResponseEntity.ok(new ApiResp(Feedback.CART_FIXED, fixedItem));
    } catch (InventoryException e) {
      return ResponseEntity.status(BAD_REQUEST).body(
          new ApiResp(e.getMessage(), null));
    } catch (UnauthorizedException e) {
      return ResponseEntity.status(UNAUTHORIZED).body(
          new ApiResp(e.getMessage() + itemId, null));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResp(e.getMessage(), null));
    }
  }

  @GetMapping(UrlMap.GET_BY_USERID)
  public ResponseEntity<ApiResp> getCartByUserId(@PathVariable Long uid) {
    try {
      if (BsUtils.isQualified(uid, false, null)) {
        // 그 유저 카트 항목 모두 읽기
        List<CartItemDto> items = cartItemServ.readUserCartItems(uid);
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
      /** 이미 유저 카트에 같은 외형이 들어있으면,
       *  그 수량을 증가하여 저장한다. 단 증가 후 수량이 재고를 초과하면 거부한다.
       */
      var items = cartItemServ.readUserCartItems(addCartItemReq.getUserId());
      Predicate<CartItemDto> shapeIsInDbCart =
          dbCartItem -> dbCartItem.getShape() == addCartItemReq.getShape();
      var itemInDB = items.stream().filter(shapeIsInDbCart).findFirst();
      CartItem savedItem = null;
      String result = null;

      if (itemInDB.isPresent()) {
        var itemDto = itemInDB.get();
        int count = itemDto.getCount() + addCartItemReq.getCount();
        savedItem = cartItemServ.updateShapeCount(itemDto.getId(), count);
        result = "범이비누 외형 수량 갱신";
      } else {
        savedItem = cartItemServ.saveItem(item);
        result = "범이비누 외형 항목 추가";
      }
      return ResponseEntity.ok(
          new ApiResp(result, savedItem));
    } catch (InventoryException e) {
      return ResponseEntity.ok(
          new ApiResp("갱신/추가 거부-재고 초과", null));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResp(e.getMessage(), null));
    }
  }
}
