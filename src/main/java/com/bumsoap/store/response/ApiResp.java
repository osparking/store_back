package com.bumsoap.store.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiResp {
    private String message;
    private Object data;
}
