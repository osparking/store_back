package com.bumsoap.store.repository;

import com.bumsoap.store.dto.MyOrderDto;
import com.bumsoap.store.model.BsOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<BsOrder, Long> {
    Optional<BsOrder> findByOrderId(String orderId);

    @Query(value = "select tp.approved_at as paymentTime, " +
            "bo.order_id, bo.order_name, " +
            "r.full_name as receipientName, " +
            "bo.payment as paymentAmount," +
            "tp.receipt_url  " +
            "from bs_order bo, toss_payment tp, recipient r " +
            "where bo.order_id = tp.order_id " +
            "and bo.recipient_id = r.id " +
            "order by approved_at desc ")
    List<MyOrderDto> getRecentSome(@Param("count") int count);
}
