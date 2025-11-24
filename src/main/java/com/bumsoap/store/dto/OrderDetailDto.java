package com.bumsoap.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDto {
    private OrderField order;
    private List<ItemsField> items;
    private int totalSoapCount;
}
