
package com.bumsoap.store.dto;

import com.bumsoap.store.util.BsShape;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProducePageRow {
    private Long id;
    private BsShape bsShape;
    private Long quantity;
    private LocalDate produceDate;

    private Long producerId;
    private String producerName;
    private Long registerId;
    private String registerName;
    private LocalDateTime registerTime;
}
