package com.bumsoap.store.dto;

import com.bumsoap.store.util.BsShape;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@Builder
public class MonthLabelSales {
    private String month;
    private Integer 보통비누;
    private Integer 백설공주;
    private Integer 메주비누;

    public static MonthLabelSales fromMap(String month, Map<String,
            BigDecimal> valueMap) {
        return MonthLabelSales.builder()
                .month("'" + month)
                .보통비누(getValueOrDefault(month, BsShape.NORMAL, valueMap))
                .백설공주(getValueOrDefault(month, BsShape.S_WHITE, valueMap))
                .메주비누(getValueOrDefault(month, BsShape.MAEJU_S, valueMap))
                .build();
    }

    private static Integer getValueOrDefault(
            String month, BsShape shape, Map<String, BigDecimal> valueMap) {
        return valueMap.getOrDefault(shape.getKeyForMonth(month),
                BigDecimal.ZERO).intValue();
    }
}
