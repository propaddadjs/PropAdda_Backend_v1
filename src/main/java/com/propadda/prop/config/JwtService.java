package com.propadda.prop.config;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.propadda.prop.security.CustomUserDetails;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {
    @Value("${security.jwt.secret}") private String secret;
    @Value("${security.jwt.access-exp-days:30}") private long accessExpDays;
    @Value("${security.jwt.refresh-exp-days:7}") private long refreshExpDays;

        private Key key() {
        if (secret == null || secret.isBlank()) {
        throw new IllegalStateException("Missing security.jwt.secret");
        }
        byte[] keyBytes;
        try {
        keyBytes = java.util.Base64.getDecoder().decode(secret); // decode the string directly
        } catch (IllegalArgumentException e) {
        keyBytes = secret.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        }
        if (keyBytes.length < 32) {
        throw new IllegalStateException("security.jwt.secret must be >= 32 bytes");
        }
        return io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(UserDetails principal) {
    var now = Instant.now();
    Integer uid = (principal instanceof CustomUserDetails cud) ? cud.getUser().getUserId() : null;
    return Jwts.builder()
            .setSubject(principal.getUsername())
            .claim("uid", uid)
            .claim("role", principal.getAuthorities().stream()
                .findFirst().map(GrantedAuthority::getAuthority).orElse(null))
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plus(accessExpDays, ChronoUnit.DAYS)))
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact();
    }

    public String generateRefreshToken(UserDetails principal) {
        var now = Instant.now();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(refreshExpDays, ChronoUnit.DAYS)))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }
}
