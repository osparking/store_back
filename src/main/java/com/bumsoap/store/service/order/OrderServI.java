package com.bumsoap.store.service.order;

import com.bumsoap.store.dto.*;
import com.bumsoap.store.model.BsOrder;
import com.bumsoap.store.request.ReviewUpdateReq;
import com.bumsoap.store.request.UpdateWaybillNoReq;
import com.bumsoap.store.util.OrderStatus;
import com.trackingmore.exception.TrackingMoreException;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

public interface OrderServI {
    SearchResult<OrderPageRow> serviceOrderPage(Integer page,
                                                Integer size);

    @Transactional
    boolean updateReview(ReviewUpdateReq reqeust, Long userId);

    boolean updateOrderStatus(Long id, OrderStatus status);

    @Transactional
    boolean updateWaybillNoOfId(UpdateWaybillNoReq req)
            throws TrackingMoreException, IOException;

    OrderDetailDto serviceOrderDetail(Long orderId);

    ReviewInfo serviceReviewInfo(Long orderId);

    SearchResult<MyOrderDto> serviceMyOrders(
            long userId, Optional<Integer> currentPage, Optional<Integer> pageSize);

    SearchResult<MyReviewRow> serviceMyReviewPage(
            Long uid, Integer page, Integer size);

    SearchResult<ReviewRow> serviceReviewPage(
            Integer page, Integer size);

    BsOrder saveOrder(BsOrder order);

    BigDecimal findDeliveryFee(BigDecimal grandTotal, String zipcode);

    BsOrder findOrderById(Long id);

    BsOrder updateOrder(BsOrder order);

    void deleteById(Long id);

    int deleteOrdersByUserIdWithoutPayments(String email);
}
