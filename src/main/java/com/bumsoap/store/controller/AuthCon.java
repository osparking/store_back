package com.bumsoap.store.controller;

import com.bumsoap.store.request.LoginRequest;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.user.UserServInt;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UrlMap.AUTHO)
@RequiredArgsConstructor
public class AuthCon {
    private final UserServInt userService;

    @PostMapping(UrlMap.LOGIN)
    public ResponseEntity<ApiResp> login (@Valid @RequestBody LoginRequest request) {
        try {
            // 이메일로 유저 정보 읽음


        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ApiResp(Feedback.LOGIN_FAILURE + e.getMessage(), null));
        }
    }
}
