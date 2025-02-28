package com.bumsoap.store.service.user;

import com.bumsoap.store.model.BsUser;

public interface UserServInt {
    BsUser findById(Long id);

    BsUser getUserById(Long id);
}
