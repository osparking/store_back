package com.bumsoap.store.dto;

import com.bumsoap.store.util.BsShape;
import lombok.Data;

@Data
public class ItemsField {
    private String shape;
    private int count;

    public ItemsField(int shape, int count) {
        this.shape = BsShape.values()[shape].toString();
        this.count = count;
    }
}
