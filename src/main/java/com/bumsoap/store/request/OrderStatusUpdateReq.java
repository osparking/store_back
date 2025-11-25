package com.bumsoap.store.request;

import com.bumsoap.store.util.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class OrderStatusUpdateReq {
    private Long id;
    private OrderStatus status;

    public OrderStatusUpdateReq(Long id, String status) {
        this.id = id;
        this.status = OrderStatus.valueOfLabel(status);
    }
}
