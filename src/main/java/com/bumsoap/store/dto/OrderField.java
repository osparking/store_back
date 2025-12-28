package com.bumsoap.store.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class OrderField extends OrderPageRow {
    private String zipcode;
    private String roadAddress;
    private String addressDetail;
    private String mbPhone;
    private String waybillNo;
    private String review;
    private byte stars;

    public OrderField(Long id, String orderId, Timestamp orderTime,
                      int orderStatus, String orderName, String review,
                      byte stars, String customer,
                      String recipient, Long user_id, BigDecimal payment,
                      String waybillNo, String zipcode, String roadAddress,
                      String addressDetail, String mbPhone) {
        super(id, orderId, orderTime, orderStatus, orderName, customer,
                recipient, user_id, payment);
        this.zipcode = zipcode;
        this.roadAddress = roadAddress;
        this.addressDetail = addressDetail;
        this.mbPhone = mbPhone;
        this.waybillNo = waybillNo;
        this.review = review;
        this.stars = stars;
    }
}
