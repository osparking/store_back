package com.bumsoap.store.repository;

import com.bumsoap.store.model.CartItem;
import com.bumsoap.store.row.CartItemRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepo extends JpaRepository<CartItem, Long> {
    @Query(value = """
            select ci.id,
                   ci.add_time,
                   ci.count,
                   ci.shape,          -- ordinal 숫자 → byte shapeOrd
                   ci.user_id,
                   sp.unit_price
            from cart_item ci
            join soap_price sp on sp.bs_shape = ci.shape
            where ci.user_id = :uid
            """, nativeQuery = true)
    List<CartItemRow> findByUserId(@Param("uid") Long uid);
}
