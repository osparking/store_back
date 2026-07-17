package com.bumsoap.store.service.token;

import com.bumsoap.store.model.RefreshToken;
import jakarta.transaction.Transactional;

public interface RefreshTokenServInt {
    RefreshToken getRefrechTokenEntity(String refresh);

    /**
     * 유저를 위하여 리프레시 토큰을 만들고, 이를 DB 에 저장한다.
     * @param userId
     * @return
     */
    @Transactional
    String createRefreshForUser(Long userId);
}
