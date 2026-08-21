package com.bumsoap.store.repository;

import com.bumsoap.store.model.CartItem;
import com.bumsoap.store.row.CartItemRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepo extends JpaRepository<CartItem, Long> {
    @Query(value = """
            SELECT
                ci.id,
                ci.add_time,
                ci.count,
                ci.shape,
                ci.user_id,
                (SELECT sp.unit_price
                 FROM soap_price sp
                 WHERE sp.bs_shape = ci.shape
                 ORDER BY sp.apply_time desc
                 LIMIT 1) AS unit_price
            FROM cart_item ci
            WHERE ci.user_id = :uid
            """, nativeQuery = true)
    List<CartItemRow> findByUserId(@Param("uid") Long uid);
}
