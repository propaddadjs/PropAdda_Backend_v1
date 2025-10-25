// src/main/java/com/propadda/prop/repo/RefreshTokenRepo.java
// Author-Hemant Arora
package com.propadda.prop.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.propadda.prop.model.RefreshToken;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);
    void deleteByUserId(Long userId);
}
