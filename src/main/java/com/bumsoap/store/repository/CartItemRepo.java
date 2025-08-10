package com.bumsoap.store.repository;

import com.bumsoap.store.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepo extends JpaRepository<CartItem, Long> {
  List<CartItem> findByUserId(Long uid);
}
