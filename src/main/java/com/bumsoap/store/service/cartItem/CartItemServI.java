package com.bumsoap.store.service.cartItem;

import com.bumsoap.store.dto.CartItemDto;
import com.bumsoap.store.exception.InventoryException;
import com.bumsoap.store.exception.UnauthorizedException;
import com.bumsoap.store.model.CartItem;
import com.bumsoap.store.request.CartUpdateReq;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartItemServI {
  @Transactional(rollbackOn =
      {InventoryException.class, UnauthorizedException.class})
  List<CartItemDto> updateUserCart(CartUpdateReq request);

  @Transactional(rollbackOn =
      {InventoryException.class, UnauthorizedException.class})
  List<CartItemDto> updateUserCart(CartUpdateReq request);

  CartItem saveItem(CartItem item);

  List<CartItemDto> readUserCartItems(Long uid);

  CartItem findById(Long itemId);

  CartItem updateShapeCount(Long itemId, int count);

  void deleteCartItem(Long itemId);
}
