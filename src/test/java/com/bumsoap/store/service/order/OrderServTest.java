package com.bumsoap.store.service.order;

import com.bumsoap.store.model.FeeDelivery;
import com.bumsoap.store.model.FeeOther;
import com.bumsoap.store.request.DeliveryFeeReq;
import com.bumsoap.store.service.soap.FeeDeliveryServI;
import com.bumsoap.store.service.soap.FeeOtherServI;
import com.bumsoap.store.util.BoxSize;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class OrderServTest {

    @Autowired
    private OrderServI orderServ;

    @Autowired
    private FeeDeliveryServI feeDeliveryServ;

    @Autowired
    private FeeOtherServI feeOtherServ;

    private FeeDelivery[] baseFee;

    @BeforeEach
    public void initFeeData() {
        baseFee = new FeeDelivery[]{
                feeDeliveryServ.getDeliveryFeeOf(BoxSize.BOX_03),
                feeDeliveryServ.getDeliveryFeeOf(BoxSize.BOX_12)
        };
        feeOther = feeOtherServ.getLatestFeeOther();
    }

    private FeeOther feeOther;

    @Test
    /**
     * 제주지역, 도서지역
     * 비누 3개 마라도: 11600원
     */
    void findDeliveryFee_1() {
        var req = new DeliveryFeeReq("63515", 3, BigDecimal.valueOf(10000));
        var result = orderServ.findDeliveryFee(req);
        Assertions.assertEquals(
                baseFee[0].getAreaJeju().add(feeOther.getIslandAdd()),
                result, "");
    }

    @Test
    /**
     * 동일지역, 기본 배송비 할인
     * 비누 12개 경기도. 0 원
     */
    void findDeliveryFee_2() {
        var req = new DeliveryFeeReq("12918", 12, BigDecimal.valueOf(40000));
        var result = orderServ.findDeliveryFee(req);
        Assertions.assertEquals(0, result.compareTo(BigDecimal.ZERO), "");
    }
    @Test
    /**
     * 다른지역
     * 비누 3개 경북 : 4600 원
     */
    void findDeliveryFee_3() {
         var req = new DeliveryFeeReq("38156", 3, BigDecimal.valueOf(12000));
        var result = orderServ.findDeliveryFee(req);
        Assertions.assertEquals(
                baseFee[0].getAreaDiff(),
                result, "");
    }
    @Test
    /**
     * 다른 지역, 섬지역
     * 비누 3개 보길도: 4600 + 4000
     */
    void findDeliveryFee_4() {
        var req = new DeliveryFeeReq("59163", 3, BigDecimal.valueOf(10000));
        var result = orderServ.findDeliveryFee(req);
        Assertions.assertEquals(
                baseFee[0].getAreaDiff().add(feeOther.getIslandAdd()),
                result, "");
    }
    @Test
    /**
     * 동일 지역, 경량
     * 비누 3개 인천: 4100
     */
    void findDeliveryFee_5() {
        var req = new DeliveryFeeReq("21088", 3, BigDecimal.valueOf(13000));
        var result = orderServ.findDeliveryFee(req);
        Assertions.assertEquals(
                baseFee[0].getAreaSame(),
                result, "");
    }


}