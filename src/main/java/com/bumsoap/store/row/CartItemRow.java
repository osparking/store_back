package com.bumsoap.store.row;

import com.bumsoap.store.util.BsShape;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CartItemRow {
    private long id;
    private Timestamp addTime;
    private int count;
    private byte shapeOrd;
    private long userId;
    private BigDecimal unitPrice;
    public LocalDateTime getAddTime() {
        return addTime.toLocalDateTime();
    }
    public BsShape getShape() {
        return BsShape.values()[shapeOrd];
    }
    public BigDecimal getUnitPrice() {
        return unitPrice.setScale(0, RoundingMode.HALF_UP);
    }
}
