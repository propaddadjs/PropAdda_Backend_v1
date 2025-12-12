// Author-Hemant Arora
package com.propadda.prop.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.propadda.prop.model.RefreshToken;
import com.propadda.prop.repo.RefreshTokenRepo;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepo repo;

    public RefreshTokenService(RefreshTokenRepo repo) {
        this.repo = repo;
    }

    // hash using SHA-256 -> hex
    public static String hashToken(String token) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash token", e);
        }
    }

    @Transactional
    public RefreshToken create(Integer userId, String rawToken, Instant expiresAt) {
        String h = hashToken(rawToken);
        RefreshToken rt = new RefreshToken(userId, h, Instant.now(), expiresAt);
        return repo.save(rt);
    }

    public Optional<RefreshToken> findByRaw(String rawToken) {
        String h = hashToken(rawToken);
        return repo.findByTokenHash(h);
    }

    @Transactional
    public void revoke(RefreshToken rt) {
        if (rt == null) return;
        rt.setRevoked(true);
        repo.save(rt);
    }

    @Transactional
    public void rotate(RefreshToken oldToken, String newRawToken, Instant newExpiresAt) {
        if (oldToken == null) return;
        String newHash = hashToken(newRawToken);
        oldToken.setRevoked(true);
        oldToken.setReplacedByHash(newHash);
        repo.save(oldToken);

        RefreshToken newRt = new RefreshToken(oldToken.getUserId(), newHash, Instant.now(), newExpiresAt);
        repo.save(newRt);
    }

    public boolean isValid(RefreshToken rt) {
        if (rt == null) return false;
        if (rt.isRevoked()) return false;
        if (rt.getExpiresAt().isBefore(Instant.now())) return false;
        return true;
    }

    @Transactional
    public void revokeAllForUser(Long userId) {
        repo.deleteByUserId(userId);
    }
}
