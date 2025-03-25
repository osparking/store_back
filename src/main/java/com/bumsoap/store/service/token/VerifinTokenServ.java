package com.bumsoap.store.service.token;

import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.model.VerifinToken;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.repository.VerifinTokenRepoI;
import com.bumsoap.store.util.BsUtils;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.TokenResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.bumsoap.store.util.TokenResult.*;

@Service
@RequiredArgsConstructor
public class VerifinTokenServ implements VerifinTokenServInt{
    private final UserRepoI userRepo;
    private final VerifinTokenRepoI verifinTokenRepo;

    @Override
    public TokenResult verifyToken(String token) {
        Optional<VerifinToken> optionalVeriTok = findByToken(token);
        if (optionalVeriTok.isEmpty()) {
            return INVALID;
        }
        BsUser user = optionalVeriTok.get().getUser();
        if (user.isEnabled()) {
            return VERIFIED;
        }
        if (hasTokenExpired(token)) {
            return EXPIRED;
        }
        user.setEnabled(true);
        userRepo.save(user);

        return VALIDATED;
    }

    @Override
    public void saveTokenForUser(String token, BsUser user) {
        var verifinToken = new VerifinToken(token, user);
        verifinTokenRepo.save(verifinToken);
    }

    @Override
    public VerifinToken makeNewToken(String oldToken) {
        Optional<VerifinToken> optionalToken = findByToken(oldToken);

        if (optionalToken.isPresent()) {
            VerifinToken verifToken = optionalToken.get();
            verifToken.setToken(UUID.randomUUID().toString());
            verifToken.setExpireDate(BsUtils.getExpireTime());

            return verifinTokenRepo.save(verifToken);
        }
        throw new IllegalStateException(Feedback.JWT_WRONG + oldToken);
    }

    @Override
    public Optional<VerifinToken> findByToken(String token) {
        return verifinTokenRepo.findByToken(token);
    }

    @Override
    public void deleteTokenByUserId(Long tokenId, Long userId) {
        verifinTokenRepo.deleteByUserId(tokenId, userId);
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
