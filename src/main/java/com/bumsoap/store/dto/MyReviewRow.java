package com.bumsoap.store.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MyReviewRow {
  private String orderName;
  private LocalDateTime orderTime;
  private String reviewPreview;
  private Integer reviewLength;
  private LocalDateTime reviewTime;
  private Long id; // 주문 ID

  public MyReviewRow(String orderName, Timestamp orderTime,
                     String reviewPreview, Integer reviewLength,
                     Timestamp reviewTime, Long id) {
    this.orderName = orderName;
    this.orderTime = orderTime.toLocalDateTime();
    this.reviewPreview = reviewPreview;
    this.reviewLength = reviewLength;
    this.reviewTime = reviewTime.toLocalDateTime();
    this.id = id;
  }
}
