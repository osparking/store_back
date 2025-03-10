package com.bumsoap.store.service.password;

import com.bumsoap.store.request.PasswordChangeReq;

public interface PasswordChangeServInf {
    void changePwd(Long userId, PasswordChangeReq request);
}
