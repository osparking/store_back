package com.bumsoap.store.repository;

import com.bumsoap.store.dto.ItemsField;
import com.bumsoap.store.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {
    @Query(value = """
            select oi.shape, oi.count
            from order_item oi
            where oi.order_id = :orderId;
            """, nativeQuery = true)
    List<ItemsField> findByOrderId(@Param("orderId") Long orderId);
}
