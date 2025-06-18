package com.bumsoap.store.security.jwt;

import com.bumsoap.store.security.user.BsUserDetails;
import com.bumsoap.store.util.LoginSource;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.List;

@Component
public class JwtUtilBean {
    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;
    @Value("${auth.token.expirationMs}")
    private int expirationMs;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key()).build().parse(token);
            return true;
        } catch (Exception e) {
            throw new JwtException(e.getMessage());
        }
    }

    public String getUsernameFrom(String token) {
        return Jwts.parser().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String generateTokenForUser(BsUserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        var signUp = LoginSource.valueOf(userDetails.getSignUpMethod());
        return Jwts.builder().setSubject(userDetails.getUsername())
                .claim("id", userDetails.getId())
                .claim("email", userDetails.getEmail())
                .claim("roles", roles)
                .claim("signUpMethod",signUp.getLabel())
                .claim("loginMethod", userDetails.getLoginMethod())
                .setIssuedAt(new java.util.Date())
                .setExpiration(new java.util.Date(expirationMs
                        + System.currentTimeMillis()))
                .signWith(key(), SignatureAlgorithm.HS256).compact();
    }
}
