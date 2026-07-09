package com.bumsoap.store.service.password;

import com.bumsoap.store.request.ActResetPwdReq;
import com.bumsoap.store.request.PasswordChangeReq;

public interface PasswordChangeServInt {
    void changePwd(Long userId, PasswordChangeReq request);

    void resetPwd(Long userId, ActResetPwdReq request);
}
