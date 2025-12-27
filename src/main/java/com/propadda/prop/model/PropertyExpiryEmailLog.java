package com.propadda.prop.model;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "property_expiry_email_log",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"category", "listing_id", "approved_at", "reminder_day"}
    )
)
public class PropertyExpiryEmailLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="elog_id")
    private Long elogId;

    @Column(name="category")
    private String category;   // COMMERCIAL / RESIDENTIAL

    @Column(name="listing_id")
    private Integer listingId;

    @Column(name="approved_at")
    private OffsetDateTime approvedAt;

    @Column(name="reminder_day")
    private Integer reminderDay;

    @Column(name="sent_at")
    private OffsetDateTime sentAt = OffsetDateTime.now();

    public Long getElogId() {
        return elogId;
    }

    public void setElogId(Long elogId) {
        this.elogId = elogId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getListingId() {
        return listingId;
    }

    public void setListingId(Integer listingId) {
        this.listingId = listingId;
    }

    public OffsetDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(OffsetDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public Integer getReminderDay() {
        return reminderDay;
    }

    public void setReminderDay(Integer reminderDay) {
        this.reminderDay = reminderDay;
    }

    public OffsetDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(OffsetDateTime sentAt) {
        this.sentAt = sentAt;
    }

    
}
