package com.bumsoap.store.service.order;

import com.bumsoap.store.dto.MyOrderDto;
import com.bumsoap.store.model.BsOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface OrderServI {
    Page<MyOrderDto> serviceMyOrders(long userId, Pageable pageable);

    BsOrder saveOrder(BsOrder order);

    BigDecimal findDeliveryFee(BigDecimal grandTotal, String zipcode);

    BsOrder findOrderById(Long id);

    BsOrder updateOrder(BsOrder order);

    void deleteById(Long id);
}
