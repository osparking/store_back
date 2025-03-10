package com.bumsoap.store.request;

import lombok.Data;

@Data
public class PasswordChangeReq {
    private String curPwd;
    private String newPwd;
    private String cnfPwd;
}
