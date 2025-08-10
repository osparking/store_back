package com.bumsoap.store.service.cartItem;

import com.bumsoap.store.dto.CartItemDto;
import com.bumsoap.store.dto.ObjMapper;
import com.bumsoap.store.exception.InventoryException;
import com.bumsoap.store.model.CartItem;
import com.bumsoap.store.repository.CartItemRepo;
import com.bumsoap.store.service.soap.InvenServI;
import com.bumsoap.store.service.soap.PriceServI;
import com.bumsoap.store.util.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemServ implements CartItemServI {
  private final CartItemRepo cartItemRepo;
  private final InvenServI invenServ;
  private final ObjMapper objMapper;
  private final PriceServI priceServ;

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
  public List<CartItemDto> readUserCartItems(Long uid) {
    return cartItemRepo.findByUserId(uid).stream()
        .map(item -> {
          CartItemDto dto = objMapper.mapToDto(item, CartItemDto.class);
          dto.setSubTotal(getSubtotal(dto));
          return dto;
        })
        .toList();
  }

  private BigDecimal getSubtotal(CartItemDto item) {
    /**
     * 재고 검사 및 예외 발생
     */
    int level = invenServ.getByBsShape(item.getShape()).getStockLevel();
    if (level < item.getCount()) {
      StringBuffer sb = new StringBuffer(Feedback.SHORT_INVENTORY);
      sb.append(item.getShape().label);
      sb.append(" - ");
      sb.append(level);
      throw new InventoryException(sb.toString());
    }
    var subTotal = priceServ.findSoapPrice(item.getShape())
        .multiply(BigDecimal.valueOf(item.getCount()));
    return subTotal.setScale(0, RoundingMode.HALF_UP);
  }
}
