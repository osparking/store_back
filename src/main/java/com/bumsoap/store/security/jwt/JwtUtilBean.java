package com.bumsoap.store.security.jwt;

import com.bumsoap.store.security.user.BsUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class JwtUtilBean {
    private String jwtSecret;
    private int expirationMs;

    public String generateTokenForUser(Authentication authentication) {
        var userDetails = (BsUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        return "";
    }
}
