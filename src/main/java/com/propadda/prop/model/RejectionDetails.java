package com.propadda.prop.model;

import java.time.OffsetDateTime;

import com.propadda.prop.enumerations.RejectionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "rejection_details")
public class RejectionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rejection_id", nullable = false)
    private Integer rejectionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "rejection_type")
    private RejectionType rejectionType;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "agent_notified")
    private Boolean agentNotified = false;

    @Column(name = "agent_emailed")
    private Boolean agentEmailed = false;

    @Column(name = "agent_id", nullable = false)
    private Integer agentId;

    @Column(name = "listing_id")
    private Integer listingId;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public Integer getRejectionId() {
        return rejectionId;
    }

    public void setRejectionId(Integer rejectionId) {
        this.rejectionId = rejectionId;
    }

    public RejectionType getRejectionType() {
        return rejectionType;
    }

    public void setRejectionType(RejectionType rejectionType) {
        this.rejectionType = rejectionType;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public Boolean getAgentNotified() {
        return agentNotified;
    }

    public void setAgentNotified(Boolean agentNotified) {
        this.agentNotified = agentNotified;
    }

    public Boolean getAgentEmailed() {
        return agentEmailed;
    }

    public void setAgentEmailed(Boolean agentEmailed) {
        this.agentEmailed = agentEmailed;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Integer getListingId() {
        return listingId;
    }

    public void setListingId(Integer listingId) {
        this.listingId = listingId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }    
    
}
