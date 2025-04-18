package com.bumsoap.store.controller;

import com.bumsoap.store.request.LoginRequest;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.response.JwtResponse;
import com.bumsoap.store.security.jwt.JwtUtilBean;
import com.bumsoap.store.security.user.BsUserDetails;
import com.bumsoap.store.service.token.VerifinTokenServInt;
import com.bumsoap.store.util.Feedback;
import com.bumsoap.store.util.TokenResult;
import com.bumsoap.store.util.UrlMap;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping(UrlMap.AUTHO)
@RequiredArgsConstructor
public class AuthCon {
    private final AuthenticationManager authenticationManager;
    private final JwtUtilBean jwtUtilBean;
    private final VerifinTokenServInt verifinTokenService;

    @GetMapping(UrlMap.EMAIL_ADDRESS)
    public ResponseEntity<ApiResp> verifyEmailToken(@RequestParam("token") String token) {
        try {
            TokenResult result = verifinTokenService.verifyToken(token);
            HttpStatus status = HttpStatus.OK;
            if (result == TokenResult.EXPIRED || result == TokenResult.INVALID) {
                status = HttpStatus.GONE;
            }
            return ResponseEntity.status(status).body(new ApiResp(result.label, null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResp(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMap.COMBINE)
    public Map<String, Object> combine(
            @AuthenticationPrincipal(errorOnInvalidType = true)
            OAuth2User principal) {
        if (principal != null) {
            return principal.getAttributes();
        } else {
            return new HashMap<String, Object>();
        }
    }

    @PostMapping(UrlMap.LOGIN)
    public ResponseEntity<ApiResp> login (@Valid @RequestBody LoginRequest request,
                                          @AuthenticationPrincipal OAuth2User oauth) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(
                    authentication);
            String jwt = jwtUtilBean.generateTokenForUser(authentication);
            BsUserDetails userDetails =
                    (BsUserDetails) authentication.getPrincipal();
            JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);
            return ResponseEntity.ok(
                    new ApiResp(Feedback.AUTHEN_SUCCESS, jwtResponse));
        } catch (DisabledException e) {
            String message = Feedback.DISABLED_ACCOUNT;
            if (verifinTokenService.hasNotExpiredTokenFor(request.getEmail())) {
              message = Feedback.PLZ_VERIFY_EMAIL;
            }
            return ResponseEntity.status(UNAUTHORIZED).body(
                    new ApiResp(message, null));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(
                    new ApiResp(e.getMessage(), Feedback.BAD_CREDENTIAL));
        }
    }
}
