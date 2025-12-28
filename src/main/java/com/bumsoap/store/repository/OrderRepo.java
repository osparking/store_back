package com.bumsoap.store.repository;

import com.bumsoap.store.dto.*;
import com.bumsoap.store.model.BsOrder;
import com.bumsoap.store.util.OrderStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<BsOrder, Long> {
    Optional<BsOrder> findByOrderId(String orderId);

    @Modifying
    @Query(value = """
            UPDATE BsOrder o
            SET o.review = :review, o.reviewTime = CURRENT_TIMESTAMP,
                o.stars = :stars
            WHERE o.id = :id
            """)
    int updateReviewById(@Param("id") Long id,
                         @Param("review") String review,
                         @Param("stars") byte stars);

    @Modifying
    @Query(value = """
            UPDATE BsOrder o SET o.orderStatus = :status
            WHERE o.id = :id
            """)
    int updateOrderStatusByOrderId(@Param("id") Long id,
                                   @Param("status") OrderStatus status);

    @Modifying
    @Query(value = """
            UPDATE BsOrder o SET o.waybillNo = :waybillNo
            WHERE o.id = :id
            """)
    int updateWaybillNoById(@Param("id") Long id,
                            @Param("waybillNo") String waybillNo);

    @Query(value = """
            select
                bo.order_name, 
                bo.order_time,
                IF(
                    CHAR_LENGTH(REGEXP_REPLACE(bo.review, '<[^>]*>', '')) > 15,
                    CONCAT(SUBSTRING(REGEXP_REPLACE(bo.review, '<[^>]*>', ''), 1, 12), '...'),
                    REGEXP_REPLACE(bo.review, '<[^>]*>', '')
                ) as review_preview,
                CHAR_LENGTH(REGEXP_REPLACE(bo.review, '<[^>]*>', '')) as review_length,
                bo.review_time, 
                bo.id
            from bs_order bo
            where bo.user_id = :uid and bo.order_status = 9
            order by bo.order_time desc
            """,
            countQuery = """
                    select count(*)
                    from bs_order bo
                    where bo.user_id = :uid and bo.order_status = 9
                    """,
            nativeQuery = true)
    Page<MyReviewRow> myReviews(@Param("uid") Long uid, Pageable pageable);

    @Query(value = """
            select bo.order_name, bo.order_time,
                IF(
                    CHAR_LENGTH(REGEXP_REPLACE(bo.review, '<[^>]*>', '')) > 23,
                    CONCAT(SUBSTRING(REGEXP_REPLACE(bo.review, '<[^>]*>', ''),
                                     1, 20), '...'),
                    REGEXP_REPLACE(bo.review, '<[^>]*>', '')
                ) as review_preview,
                bo.review_time, bo.id,
                CONCAT(SUBSTRING(bu.full_name, 1, 1), '**') as customer_name,
                GROUP_CONCAT(oi.shape) as shapes_list,
            	bo.review LIKE '%youtube.com/%'
            	   OR bo.review LIKE '%youtu.be/%'
            	   as has_video,
            	bo.review like '%<img src=%' as has_image
            from bs_order bo
            join order_item oi on oi.order_id = bo.id
            join bs_user bu on bu.id = bo.user_id
            WHERE bo.order_status = 9
            group by bo.id
            order by bo.order_time desc;
            """,
            nativeQuery = true)
    Page<ReviewRow> getReviewPage(Pageable pageable);

    @Query(value = """
            select bo.order_name, bo.review, bo.user_id
            from bs_order bo
            where bo.id = :oId
            """, nativeQuery = true)
    Optional<ReviewInfo> findReviewInfo(@Param("oId") Long oId);

    @Query(value = """
            select bo.id, bo.order_id, bo.order_time,
                    bo.order_status, bo.order_name,
                    bo.review, bo.stars, bu.full_name as customer,
                    r.full_name as recipient,
                    bo.user_id, bo.payment, bo.waybill_no,
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
                bo.order_status,
                r.full_name as recipientName,
                bo.payment as paymentAmount,
                tp.receipt_url as receiptUrl, bo.id
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
