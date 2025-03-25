package com.bumsoap.store.request;

import com.bumsoap.store.model.BsUser;
import lombok.Data;

@Data
public class TokenVerifinReq {
    private String token;
    private BsUser user;
}
