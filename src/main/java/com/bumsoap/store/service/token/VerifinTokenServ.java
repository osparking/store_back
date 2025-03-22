package com.bumsoap.store.service.token;

import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.model.VerifinToken;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.repository.VerifinTokenRepoI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerifinTokenServ implements VerifinTokenServInt{
    private final UserRepoI userRepo;
    private final VerifinTokenRepoI verifinTokenRepo;

    @Override
    public String varifyToken(String token) {
        Optional<VerifinToken> optionalVeriTok = findByToken(token);
        if (optionalVeriTok.isEmpty()) {
            return "INVALID";
        }
        BsUser user = optionalVeriTok.get().getUser();
        if (user.isEnabled()) {
            return "VERIFIED";
        }
        if (hasTokenExpired(token)) {
            return "EXPIRED";
        }
        user.setEnabled(true);
        userRepo.save(user);

        return "VALID";
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
        return verifinTokenRepo.findByToken(token);
    }

    @Override
    public void deleteTokenById(Long id) {

    }

    @Override
    public boolean hasTokenExpired(String token) {
        var verifinToken = verifinTokenRepo.findByToken(token);

        if (verifinToken.isEmpty()) {
            return true;
        } else {
            var expireDate = verifinToken.get().getExpireDate();
            return System.currentTimeMillis() > expireDate.getTime();
        }
    }
}
