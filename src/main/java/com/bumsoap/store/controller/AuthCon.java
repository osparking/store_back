package com.bumsoap.store.controller;

import com.bumsoap.store.request.LoginRequest;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.UrlMap;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping(UrlMap.AUTHO)
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173/")
public class AuthCon {
    private final AuthenticationManager authenticationManager;

    @PostMapping(UrlMap.LOGIN)
    public ResponseEntity<ApiResp> login (@Valid LoginRequest request) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(
                    authentication);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
