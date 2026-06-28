package com.bumsoap.store.row;

import com.bumsoap.store.dto.SoapPriceDto;
import com.bumsoap.store.util.BsShape;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SoapPriceRow {
    String shapeLabel;
    BigDecimal unitPrice;

    public SoapPriceRow(SoapPriceDto dto) {
        shapeLabel = BsShape.values()[dto.getShapeOrdinal()].label;
        unitPrice = dto.getUnitPrice();
    }
}
