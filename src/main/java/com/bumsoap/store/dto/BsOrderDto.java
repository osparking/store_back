package com.bumsoap.store.dto;

import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.model.OrderItem;
import com.bumsoap.store.model.Recipient;
import com.bumsoap.store.util.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class BsOrderDto {
  @Autowired
  private ObjMapper objMapper;
  private long id;
  private UserDto user;
  private List<OrderItem> items;
  private Recipient recipient;

  private BigDecimal payAmount;
  private LocalDateTime orderTime;
  private OrderStatus orderStatus;

  public void serUserDto(BsUser user) {
    this.user = objMapper.mapToDto(user, UserDto.class);
  }
}
