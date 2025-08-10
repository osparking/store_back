package com.bumsoap.store.service.cartItem;

import com.bumsoap.store.exception.InventoryException;
import com.bumsoap.store.model.CartItem;
import com.bumsoap.store.repository.CartItemRepo;
import com.bumsoap.store.service.soap.InvenServI;
import com.bumsoap.store.util.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemServ implements CartItemServI {
  private final CartItemRepo cartItemRepo;
  private final InvenServI invenServ;

  @Override
  public CartItem saveItem(CartItem item) {
    /**
     * 재고 검사 및 예외 발생
     */
    int level = invenServ.getByBsShape(item.getShape()).getStockLevel();

    if (level < item.getCount()) {
      String sb = Feedback.SHORT_INVENTORY + item.getShape().label +
          " - " +
          level;
      throw new InventoryException(sb);
    }

    return cartItemRepo.save(item);
  }

  @Override
  public List<CartItem> readUserCartItems(Long uid) {
    return cartItemRepo.findByUserId(uid);
  }
}
