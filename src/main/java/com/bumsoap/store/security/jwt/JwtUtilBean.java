package com.bumsoap.store.security.jwt;

import com.bumsoap.store.security.user.BsUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.security.Key;
import java.util.List;

public class JwtUtilBean {
    private String jwtSecret;
    private int expirationMs;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsernameFrom(String token) {
        return Jwts.parser().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String generateTokenForUser(Authentication authentication) {
        var userDetails = (BsUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder().setSubject(userDetails.getUsername())
                .claim("id", userDetails.getId())
                .claim("roles", roles)
                .setIssuedAt(new java.util.Date())
                .setExpiration(new java.util.Date(expirationMs
                        + System.currentTimeMillis()))
                .signWith(key(), SignatureAlgorithm.HS256).compact();
    }
}
