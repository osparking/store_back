package com.bumsoap.store.request;

import com.bumsoap.store.util.OrderStatus;
import lombok.Data;

@Data
public class UpdateWaybillNoReq {
    private Long id;
    private String waybillNo;
    private OrderStatus status;
    public UpdateWaybillNoReq(Long id, String status, String waybillNo) {
        this.id = id;
        this.status = OrderStatus.valueOfLabel(status);
        this.waybillNo = waybillNo;
    }
}
