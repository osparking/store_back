package com.bumsoap.store.service.cartItem;

import com.bumsoap.store.model.CartItem;

import java.util.List;

public interface CartItemServI {
  CartItem saveItem(CartItem item);

  List<CartItem> readUserCartItems(Long uid);
}
