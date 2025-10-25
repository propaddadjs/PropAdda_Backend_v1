// Author-Hemant Arora
package com.propadda.prop.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.propadda.prop.model.UploadSession;

public interface UploadSessionRepository extends JpaRepository<UploadSession, String> {
    Optional<UploadSession> findByUploadId(String uploadId);
}