package com.bumsoap.store.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateWaybillNoReq {
    private Long id;
    private String waybillNo;
}
