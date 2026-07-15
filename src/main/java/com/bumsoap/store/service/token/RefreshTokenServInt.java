package com.bumsoap.store.service.token;

import com.bumsoap.store.model.RefreshToken;
import com.bumsoap.store.security.user.BsUserDetails;

public interface RefreshTokenServInt {
    RefreshToken getRefrechTokenEntity(String refresh);

    String createRefreshForUser(BsUserDetails user);
}
