package com.bumsoap.store.controller;

import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.token.VerifinTokenServInt;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.TokenResult;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(UrlMap.VERIFY)
public class VerifinTokenCon {
    private final VerifinTokenServInt verifinTokenServ;
    private final UserRepoI userRepo;

    @GetMapping(UrlMap.IS_EXPIRED)
    public ResponseEntity<ApiResp> checkIfTokenExpired(String token) {
        boolean isExpired = verifinTokenServ.hasTokenExpired(token);
        ApiResp response = new ApiResp(isExpired?
                Feedback.TOKEN_EXPIRED :
                Feedback.TOKEN_IS_VALID, null);
        return ResponseEntity.ok(response);
    }

    @GetMapping(UrlMap.TOKEN)
    public ResponseEntity<ApiResp> validateToken(String token) {
        TokenResult result = verifinTokenServ.verifyToken(token);
        ApiResp response = new ApiResp(result.label, null);

        return ResponseEntity.ok(response);
    }
}
