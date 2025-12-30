package com.bumsoap.store.dto;

import com.bumsoap.store.util.BsShape;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ReviewRow {
  private String orderName;
  private LocalDateTime orderTime;
  private String orderTimeStr;
  private String reviewPreview;
  private LocalDateTime reviewTime;
  private byte stars;
  private Long id; // 주문 ID
  private String customerName;
  private String shapesList;
  private Boolean hasVideo;
  private Boolean hasImage;

  public ReviewRow(String orderName, Timestamp orderTime,
                   String reviewPreview, Timestamp reviewTime,
                   byte stars, Long id, String customerName,
                   String shapesList, int hasVideo, int hasImage) {
    this.orderName = orderName;
    this.orderTime = orderTime.toLocalDateTime();
    this.orderTimeStr = formatKoreanDateTime(orderTime.toLocalDateTime());
    this.reviewPreview = reviewPreview;
    this.reviewTime = reviewTime.toLocalDateTime();;
    this.id = id;
    this.customerName = customerName;
    this.stars = stars;
    this.shapesList = shapesList;
    this.hasVideo = hasVideo == 1;
    this.hasImage = hasImage == 1;
  }

  public static String formatKoreanDateTime(LocalDateTime dateTime) {
    // DateTimeFormatter를 사용하여 포맷 지정
    var formatter = DateTimeFormatter.ofPattern("yy년 MM월 dd일 HH:mm");

    // 한국어 로케일 적용 (선택사항)
    formatter = formatter.withLocale(Locale.KOREAN);

    return "'" + dateTime.format(formatter);
  }

  public String getShapesList() {
    return ordinalsToLabels(shapesList);
  }

  public Long getReviewDayDelay() {
    return ChronoUnit.DAYS.between(orderTime, reviewTime);
  }

  private String ordinalsToLabels(String ordinals) {
    return Arrays.stream(ordinals.split(",")).map(num ->
                    BsShape.values()[Integer.parseInt(num)].toString())
            .collect(Collectors.joining(", "));
  }
}
