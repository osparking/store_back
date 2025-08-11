package com.bumsoap.store.service.cartItem;

import com.bumsoap.store.dto.CartItemDto;
import com.bumsoap.store.model.CartItem;

import java.util.List;

public interface CartItemServI {
  CartItem saveItem(CartItem item);

  List<CartItemDto> readUserCartItems(Long uid);

  CartItem findById(Long itemId);

  CartItem updateShapeCount(Long itemId, int count);
}
