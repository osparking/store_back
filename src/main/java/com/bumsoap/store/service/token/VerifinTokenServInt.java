package com.bumsoap.store.service.token;

import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.model.VerifinToken;

import java.util.Optional;

public interface VerifinTokenServInt {
    String varifyToken(String token);
    void saveTokenForUser(String token, BsUser user);
    VerifinToken makeNewToken(String oldToken);
    Optional<VerifinToken> findByToken(String token);
    void deleteTokenById(Long id);
    boolean hasTokenExpired(String token);
}
