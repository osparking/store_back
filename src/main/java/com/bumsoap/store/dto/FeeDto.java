package com.bumsoap.store.dto;

import com.bumsoap.store.model.FeeDelivery;
import com.bumsoap.store.model.FeeOther;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeeDto {
    private FeeDelivery fee_03;
    private FeeDelivery fee_12;
    private FeeOther feeOther;
}
