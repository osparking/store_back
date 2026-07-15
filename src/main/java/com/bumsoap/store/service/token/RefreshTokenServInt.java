package com.bumsoap.store.service.token;

import com.bumsoap.store.security.user.BsUserDetails;

public interface RefreshTokenServInt {
    String createRefreshForUser(BsUserDetails user);
}
