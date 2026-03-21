package com.bumsoap.store.dto;

import com.bumsoap.store.util.BsShape;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class SoapSaleLabel {
    private String shapeLabel;
    private BigDecimal soaps;
    private String month;

    public SoapSaleLabel(SoapSaleDto saleDto) {
        this.shapeLabel = BsShape.values()[saleDto.getShape()].toString();
        this.soaps = saleDto.getSoaps();
        this.month = saleDto.getMonth();
    }
}
