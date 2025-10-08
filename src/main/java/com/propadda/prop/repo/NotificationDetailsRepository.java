package com.propadda.prop.repo;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.propadda.prop.enumerations.Role;
import com.propadda.prop.model.NotificationDetails;

@Repository
public interface NotificationDetailsRepository extends JpaRepository<NotificationDetails, Integer> {
    
    List<NotificationDetails> findByNotificationReceiverRole(Role role);

    List<NotificationDetails> findByNotificationReceiverId(Integer notificationReceiverId);

    List<NotificationDetails> findByCreatedAtAfterAndNotificationReceiverRole(OffsetDateTime afterTime, Role role);

    @Query("SELECT count(c) FROM NotificationDetails c WHERE c.notificationReceiverRole = :notificationReceiverRole AND c.notificationReceiverId= :notificationReceiverId AND notificationViewed = false")
    Integer countUnreadNotifications(@Param("notificationReceiverRole") Role notificationReceiverRole, @Param("notificationReceiverId") Integer notificationReceiverId);
} 