package com.bumsoap.store.service.order;

import com.bumsoap.store.dto.MyOrderDto;
import com.bumsoap.store.dto.SearchResult;
import com.bumsoap.store.model.BsOrder;

import java.math.BigDecimal;
import java.util.Optional;

public interface OrderServI {
    SearchResult<MyOrderDto> serviceMyOrders(
            long userId, Optional<Integer> currentPage, Optional<Integer> pageSize);

    BsOrder saveOrder(BsOrder order);

    BigDecimal findDeliveryFee(BigDecimal grandTotal, String zipcode);

    BsOrder findOrderById(Long id);

    BsOrder updateOrder(BsOrder order);

    void deleteById(Long id);

    int deleteOrdersByUserIdWithoutPayments(String email);
}
