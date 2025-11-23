package com.bumsoap.store.repository;

import com.bumsoap.store.dto.MyOrderDto;
import com.bumsoap.store.dto.OrderField;
import com.bumsoap.store.dto.OrderPageRow;
import com.bumsoap.store.model.BsOrder;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<BsOrder, Long> {
    Optional<BsOrder> findByOrderId(String orderId);

    @Query(value = """
            select bo.id, bo.order_id, bo.order_time,
                    bo.order_status, bo.order_name,
                    bu.full_name as customer,
                    r.full_name as recipient,
                    bo.user_id, bo.payment,
                    ab.zipcode, ab.road_address,
                    r.address_detail, r.mb_phone
            from bs_order bo
            join bs_user bu on bu.id = bo.user_id
            join recipient r on r.id = bo.recipient_id
            join address_basis ab on ab.id = r.addr_basis_id
            where bo.id = :id;
            """, nativeQuery = true)
    Optional<OrderField> findOrderDetail(@Param("id") Long id);

    @Query(value = """
            select bo.id, bo.order_id, bo.order_time,
            	bo.order_status, bo.order_name,
            	bu.full_name as customer,
            	r.full_name as recipient,
            	bo.user_id, bo.payment
            from bs_order bo
            join bs_user bu on bu.id = bo.user_id
            join recipient r on r.id = bo.recipient_id
            where bo.order_status != 0
            order by bo.order_time desc
            """, nativeQuery = true)
    Page<OrderPageRow> findOrderPage(Pageable pageable);

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

    @Query("""
            SELECT bo
            FROM BsOrder bo
            LEFT JOIN bo.tossPayment tp
            JOIN bo.user bu
            WHERE bu.email = :email
                AND tp IS NULL
            """)
    List<BsOrder> findOrdersByUserEmailWithoutPayments(String email);

    @Transactional
    default int deleteOrdersByUserEmailWithoutPayments(String email) {
        List<BsOrder> ordersToDelete =
                findOrdersByUserEmailWithoutPayments(email);
        deleteAll(ordersToDelete);
        return ordersToDelete.size();
    }
}
