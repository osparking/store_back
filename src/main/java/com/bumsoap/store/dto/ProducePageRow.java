
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
    private BsShape bsShape;
    private Long quantity;
    private LocalDate produceDate;

    private String producer;
    private String register;
    private LocalDateTime registerTime;
}
