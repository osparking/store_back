package com.bumsoap.store.controller;

import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.repository.UserRepoI;
import com.bumsoap.store.request.TokenVerifinReq;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.token.VerifinTokenServInt;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.TokenResult;
import com.bumsoap.store.util.UrlMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(UrlMap.VERIFY)
public class VerifinTokenCon {
    private final VerifinTokenServInt verifinTokenServ;
    private final UserRepoI userRepo;

    @PostMapping(UrlMap.SAVE_TOKEN)
    public ResponseEntity<ApiResp> saveUserVerifToken(
            @RequestBody TokenVerifinReq request) {
        Long userId = request.getUser().getId();
        BsUser user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException(
                        Feedback.USER_ID_NOT_FOUND + userId));
        verifinTokenServ.saveTokenForUser(request.getToken(), user);
        return ResponseEntity.ok(new ApiResp(Feedback.TOKEN_SAVED, null));
    }

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
