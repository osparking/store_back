package com.bumsoap.store.dto;

import com.bumsoap.store.util.BsShape;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ReviewRow {
  private String orderName;
  private LocalDateTime orderTime;
  private String reviewPreview;
  private LocalDateTime reviewTime;
  private Long id; // 주문 ID
  private String customerName;
  private String shapesList;
  private Boolean hasVideo;
  private Boolean hasImage;

  public ReviewRow(String orderName, Timestamp orderTime,
                   String reviewPreview, Timestamp reviewTime,
                   Long id, String customerName, String shapesList,
                   int hasVideo, int hasImage) {
    this.orderName = orderName;
    this.orderTime = orderTime.toLocalDateTime();;
    this.reviewPreview = reviewPreview;
    this.reviewTime = reviewTime.toLocalDateTime();;
    this.id = id;
    this.customerName = customerName;
    this.shapesList = shapesList;
    this.hasVideo = hasVideo == 1;
    this.hasImage = hasImage == 1;
  }

  public String getShapesList() {
    return ordinalsToLabels(shapesList);
  }

  private String ordinalsToLabels(String ordinals) {
    return Arrays.stream(ordinals.split(",")).map(num ->
                    BsShape.values()[Integer.parseInt(num)].toString())
            .collect(Collectors.joining(", "));
  }
}
