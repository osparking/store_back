package com.bumsoap.store.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ShapeMonthDto {
    private Byte shape;
    private BigDecimal soaps;
    private String month;
}
