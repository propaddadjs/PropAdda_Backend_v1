// Author-Hemant Arora
package com.propadda.prop.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "upload_session")
public class UploadSession {

    @Id
    @Column(name = "upload_id", nullable = false, updatable = false)
    private String uploadId = UUID.randomUUID().toString();

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "status")
    private String status = "PENDING"; // PENDING, CLAIMED, FAILED, EXPIRED

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Column(name = "expires_at")
    private Instant expiresAt;

    // constructors/getters/setters
    public UploadSession() {}
    public UploadSession(Integer userId, Instant expiresAt) {
        this.userId = userId; this.expiresAt = expiresAt;
    }
    public String getUploadId() { return uploadId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }
}