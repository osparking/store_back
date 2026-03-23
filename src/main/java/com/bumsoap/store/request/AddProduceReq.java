package com.bumsoap.store.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AddProduceReq {
    private String shapeLabel;
    private Long quantity;
    private LocalDate produceDate;
    private Long producerId; // 생산직원 ID
}
