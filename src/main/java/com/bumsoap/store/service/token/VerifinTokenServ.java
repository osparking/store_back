package com.bumsoap.store.service.token;

import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.model.VerifinToken;
import com.bumsoap.store.repository.UserRepoI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerifinTokenServ implements VerifinTokenServInt{
    private final UserRepoI userRepo;

    @Override
    public String varifyToken(String token) {
        return "";
    }

    @Override
    public void saveTokenForUser(String token, BsUser user) {

    }

    @Override
    public VerifinToken makeNewToken(String oldToken) {
        return null;
    }

    @Override
    public Optional<VerifinToken> findByToken(String token) {
        return Optional.empty();
    }

    @Override
    public void deleteTokenById(Long id) {

    }

    @Override
    public boolean hasTokenExpired(String token) {
        return false;
    }
}
