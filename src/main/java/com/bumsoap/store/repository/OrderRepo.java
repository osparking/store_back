package com.bumsoap.store.repository;

import com.bumsoap.store.model.BsOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<BsOrder, Long> {
}
