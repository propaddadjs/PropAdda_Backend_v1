package com.propadda.prop.model;

import java.time.OffsetDateTime;

import com.propadda.prop.enumerations.NotificationType;
import com.propadda.prop.enumerations.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notification_details")
public class NotificationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id", nullable = false)
    private Integer notificationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;

    @Column(name = "notification_message", columnDefinition = "TEXT")
    private String notificationMessage;

    @Column(name = "notification_sender_id", nullable = false)
    private Integer notificationSenderId;

    @Column(name = "notification_receiver_id", nullable = false)
    private Integer notificationReceiverId;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_sender_role")
    private Role notificationSenderRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_receiver_role")
    private Role notificationReceiverRole;

    @Column(name = "notification_viewed")
    private Boolean notificationViewed;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public Integer getNotificationSenderId() {
        return notificationSenderId;
    }

    public void setNotificationSenderId(Integer notificationSenderId) {
        this.notificationSenderId = notificationSenderId;
    }

    public Integer getNotificationReceiverId() {
        return notificationReceiverId;
    }

    public void setNotificationReceiverId(Integer notificationReceiverId) {
        this.notificationReceiverId = notificationReceiverId;
    }

    public Role getNotificationSenderRole() {
        return notificationSenderRole;
    }

    public void setNotificationSenderRole(Role notificationSenderRole) {
        this.notificationSenderRole = notificationSenderRole;
    }

    public Role getNotificationReceiverRole() {
        return notificationReceiverRole;
    }

    public void setNotificationReceiverRole(Role notificationReceiverRole) {
        this.notificationReceiverRole = notificationReceiverRole;
    }

    public Boolean getNotificationViewed() {
        return notificationViewed;
    }

    public void setNotificationViewed(Boolean notificationViewed) {
        this.notificationViewed = notificationViewed;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    
}
