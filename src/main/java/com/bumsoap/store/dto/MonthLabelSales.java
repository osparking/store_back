package com.bumsoap.store.dto;

import com.bumsoap.store.util.BsShape;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class MonthLabelSales {
    private String month;
    private Integer 보통비누;
    private Integer 백설공주;
    private Integer 메주비누;

    public MonthLabelSales(String month, Map<String, BigDecimal> valueMap) {
        this.month = month;
        String soapKey = month + "_" + BsShape.NORMAL.ordinal();
        this.보통비누 = valueMap.containsKey(soapKey) ?
                valueMap.get(soapKey).intValue():0;

        soapKey = month + "_" + BsShape.S_WHITE.ordinal();
        this.백설공주 = valueMap.containsKey(soapKey) ?
                valueMap.get(soapKey).intValue():0;

        soapKey = month + "_" + BsShape.MAEJU_S.ordinal();
        this.메주비누 = valueMap.containsKey(soapKey) ?
                valueMap.get(soapKey).intValue():0;
    }
}
