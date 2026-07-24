package com.bumsoap.store.controller;

import com.bumsoap.store.dto.FeeDto;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.soap.FeeDeliveryServI;
import com.bumsoap.store.service.soap.FeeOtherServI;
import com.bumsoap.store.util.BoxSize;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping(UrlMap.FEE_ETC)
@RequiredArgsConstructor
public class FeeEtcCon {
    private final FeeDeliveryServI feeDeliveryServ;
    private final FeeOtherServI feeOtherServ;

    @GetMapping(UrlMap.GET_LATEST)
    public ResponseEntity<ApiResp> getFeeEtc() {
        try {
            var feeDto = new FeeDto(
                    feeDeliveryServ.getDeliveryFeeOf(BoxSize.BOX_03),
                    feeDeliveryServ.getDeliveryFeeOf(BoxSize.BOX_12),
                    feeOtherServ.getLatestFeeOther()
            );

            return ResponseEntity.ok(new ApiResp("배송비 등 정보", feeDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
        }
    }
}
