package com.bumsoap.store.repository;

import com.bumsoap.store.model.BsOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepo extends JpaRepository<BsOrder, Long> {
    Optional<BsOrder> findByOrderId(String orderId);
}
