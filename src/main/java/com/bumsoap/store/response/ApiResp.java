package com.bumsoap.store.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiResp {
    private String message;
    private Object data;
    private String code;

    public ApiResp(String message, Object data) {
        this.message = message;
        this.data = data;
        this.code = null;
    }
}
