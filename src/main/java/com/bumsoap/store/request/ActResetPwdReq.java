package com.bumsoap.store.request;

import lombok.Data;

@Data
public class ActResetPwdReq {
    private String newPwd;
    private String cnfPwd;
}
