package com.bumsoap.store.repository;

import com.bumsoap.store.dto.MyOrderDto;
import com.bumsoap.store.model.BsOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepo extends JpaRepository<BsOrder, Long> {
    Optional<BsOrder> findByOrderId(String orderId);

    @Query(value = """
            SELECT
                tp.approved_at as paymentTime,
                bo.order_id as orderId,
                bo.order_name as orderName,
                r.full_name as recipientName,
                bo.payment as paymentAmount,
                tp.receipt_url as receiptUrl
            FROM bs_order bo
            INNER JOIN toss_payment tp ON bo.order_id = tp.order_id
            INNER JOIN recipient r ON bo.recipient_id = r.id
            where bo.user_id = :user_id
            ORDER BY tp.approved_at DESC
            """, nativeQuery = true)
    Page<MyOrderDto> findMyOrders(@Param("user_id") Long user_id,
                                  Pageable pageable);
}
