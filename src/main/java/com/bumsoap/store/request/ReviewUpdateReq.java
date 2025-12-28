package com.bumsoap.store.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewUpdateReq {
    private byte stars; // 별점
    private Long id; // 주문 ID, orderId 와 다름.
    private String review;
}
