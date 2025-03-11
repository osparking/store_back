package com.bumsoap.store.controller;

import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.request.LoginRequest;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.service.user.UserServInt;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping(UrlMap.AUTHO)
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173/")
public class AuthCon {
    private final UserServInt userService;

    @PostMapping(UrlMap.LOGIN)
    public ResponseEntity<ApiResp> login (@Valid @RequestBody LoginRequest request) {
        try {
            BsUser user = userService.getByEmail(request.getEmail());

            if (user.getPassword().equals(request.getPassword())) {
                user.setPassword(null);
                return ResponseEntity.ok(
                        new ApiResp(Feedback.LOGIN_SUCCESS, user.getId()));
            }
            return ResponseEntity.status(UNAUTHORIZED).body(
                    new ApiResp(Feedback.BAD_CREDENTIAL, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ApiResp(Feedback.LOGIN_FAILURE + e.getMessage(), null));
        }
    }
}
