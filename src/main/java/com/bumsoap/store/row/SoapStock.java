package com.bumsoap.store.row;

import com.bumsoap.store.util.BsShape;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SoapStock {
    private BsShape shape;
    private Long stock;

    public SoapStock(Byte shapeByte, Long stock) {
        this.stock = stock;
        this.shape = BsShape.values()[shapeByte];
    }
}
