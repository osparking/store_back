package com.bumsoap.store.controller;

import com.bumsoap.store.model.BsUser;
import com.bumsoap.store.request.LoginRequest;
import com.bumsoap.store.response.ApiResp;
import com.bumsoap.store.response.JwtResponse;
import com.bumsoap.store.security.jwt.JwtUtilBean;
import com.bumsoap.store.security.user.BsUserDetails;
import com.bumsoap.store.service.TotpService;
import com.bumsoap.store.service.token.VerifinTokenServInt;
import com.bumsoap.store.service.user.UserServInt;
import com.bumsoap.store.util.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(UrlMap.AUTHO)
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173/")
public class AuthCon {
    private final AuthenticationManager authenticationManager;
    private final JwtUtilBean jwtUtilBean;
    private final VerifinTokenServInt verifinTokenService;
    private final UserServInt userService;
    private final AuthUtil authUtil;
    private final TotpService totpService;

    @GetMapping("/user/2fa-status")
    public ResponseEntity<?> get2FAstatus() {
        var details = authUtil.loggedInUserDetails();

        if (details == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Feedback.NOT_FOUND);
        } else {
            return ResponseEntity.ok().body(
                Map.of("2FA-활성화", details.isTwoFaAEnabled()));
        }
    }

    @PostMapping("/verify-2fa")
    public ResponseEntity<String> verify2FA(@RequestParam int code) {
        Long userId = authUtil.loggedInUserId();
        boolean validCode = userService.verifyCode(userId, code);

        if (validCode) {
            userService.enable2FA(userId);
            return ResponseEntity.ok(Feedback.TWO_FA_VERIFIED);
        } else {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Feedback.TWO_FA_CODE_ERROR);
        }
    }

    @PostMapping("/disable-2fa")
    public ResponseEntity<String> disableUserFor2FA() {
        Long userId = authUtil.loggedInUserId();
        userService.disable2FA(userId);
        return ResponseEntity.ok(Feedback.DISABLED_2FA);
    }

    @PostMapping("/enable-2fa")
    public ResponseEntity<String> enableUserFor2FA() {
        Long userId = authUtil.loggedInUserId();
        var secret = userService.generateSecret(userId);
        String qrCodeUrl = totpService.getQRcodeUrl(secret,
            userService.getUserById(userId).getEmail());
        return ResponseEntity.ok(qrCodeUrl);
    }

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

    @PostMapping(UrlMap.LOGIN)
    public ResponseEntity<ApiResp> login (@Valid @RequestBody LoginRequest request) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(
                    authentication);
            var details = (BsUserDetails)authentication.getPrincipal();
            details.setLoginMethod(LoginSource.EMAIL.getLabel());
            String jwt = jwtUtilBean.generateTokenForUser(details);
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
        } catch (RuntimeException e) {
            BsUser user = userService.getByEmail(request.getEmail());
            if (user == null) {
                return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResp(e.getMessage(), null));
            } else {
                var signUp = LoginSource.valueOf(user.getSignUpMethod());
                var message = signUp.getLabel() + Feedback.TRY_SOCIAL_LOGIN;
                return ResponseEntity.status(BAD_REQUEST).body(
                    new ApiResp(message, null));
            }
        }
    }
}
