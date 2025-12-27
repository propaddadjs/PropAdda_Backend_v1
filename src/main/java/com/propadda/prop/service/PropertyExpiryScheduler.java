package com.propadda.prop.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.propadda.prop.enumerations.NotificationType;
import com.propadda.prop.enumerations.Role;
import com.propadda.prop.model.NotificationDetails;
import com.propadda.prop.model.PropertyExpiryEmailLog;
import com.propadda.prop.model.Users;
import com.propadda.prop.repo.CommercialPropertyDetailsRepo;
import com.propadda.prop.repo.NotificationDetailsRepository;
import com.propadda.prop.repo.PropertyExpiryEmailLogRepository;
import com.propadda.prop.repo.ResidentialPropertyDetailsRepo;
import com.propadda.prop.repo.UsersRepo;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class PropertyExpiryScheduler {

    private final CommercialPropertyDetailsRepo commercialRepo;
    private final ResidentialPropertyDetailsRepo residentialRepo;
    private final PropertyExpiryEmailLogRepository emailLogRepo;
    private final MailSenderService mailSenderService;
    private final UsersRepo userRepository;
    private final NotificationDetailsRepository notificationRepo;

    private static final Map<Integer, Integer> REMINDER_MAP = Map.of(
            76, 14,
            83, 7,
            87, 3,
            89, 1
    );

    @Scheduled(cron = "0 0 12 * * *", zone = "Asia/Kolkata") // daily at 12 PM
    @Transactional
    public void processExpiryAndReminders() {

        processCommercial();
        processResidential();
        sendExpiredEmails();

        // int expiredCommercial = commercialRepo.expireOldApprovedProperties();
        // int expiredResidential = residentialRepo.expireOldApprovedProperties();

        // System.out.println("Expired commercial"+expiredCommercial+ "residential="+expiredResidential);
    }
    
    private void processCommercial() {
        commercialRepo.findForExpiryReminders()
                .forEach(p -> sendReminder(
                        "Commercial",
                        p.getListingId(),
                        p.getApprovedAt(),
                        p.getTitle(),
                        p.getCommercialOwner().getUserId()
                ));
    }

    private void processResidential() {
        residentialRepo.findForExpiryReminders()
                .forEach(p -> sendReminder(
                        "Residential",
                        p.getListingId(),
                        p.getApprovedAt(),
                        p.getTitle(),
                        p.getResidentialOwner().getUserId()
                ));
    }

    private void sendReminder(
            String category,
            Integer listingId,
            OffsetDateTime approvedAt,
            String title,
            Integer userId
    ) {
        int daysSinceApproval = Math.toIntExact(
            ChronoUnit.DAYS.between(
                approvedAt.toLocalDate(),
                LocalDate.now(ZoneId.of("Asia/Kolkata"))
            )
        );

        Integer daysLeft = REMINDER_MAP.get(daysSinceApproval);
        if (daysLeft == null) return;

        Users user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getEmail() == null) return;

        try {

            String subject = "Your listing titled- "+title+" expires in " + daysLeft + " day"
                    + (daysLeft == 1 ? "" : "s");

            String html = PropertyExpiryEmailTemplate.buildReminderEmail(
                    user.getFirstName(),
                    title,
                    daysLeft
            );

            mailSenderService.sendHtml(user.getEmail(), subject, html);
  
            PropertyExpiryEmailLog elog = new PropertyExpiryEmailLog();
            elog.setCategory(category);
            elog.setListingId(listingId);
            elog.setApprovedAt(approvedAt); 
            elog.setReminderDay(daysSinceApproval);

            emailLogRepo.save(elog);

            NotificationDetails notification = new NotificationDetails();
            String message = "Your listing titled- "+title+" expires in " + daysLeft + " day" + (daysLeft == 1 ? "" : "s");
            notification.setNotificationMessage(message);
            notification.setNotificationType(NotificationType.ExpiryReminder);
            notification.setNotificationReceiverId(user.getUserId());
            notification.setNotificationReceiverRole(Role.AGENT);
            notification.setNotificationSenderId(1);
            notification.setNotificationSenderRole(Role.ADMIN);
            notificationRepo.save(notification);

        } catch (MessagingException e) {
            System.out.println("Failed to send reminder email!!!"+e.getMessage());
        }
    }

    private void sendExpiredEmails() {

        // 🟥 Commercial
        commercialRepo.expireAndFetchExpired()
                .forEach(p -> sendExpiredEmail(
                        "Commercial",
                        p.getListingId(),
                        p.getTitle(),
                        p.getUserId()
                ));

        // 🟦 Residential
        residentialRepo.expireAndFetchExpired()
                .forEach(p -> sendExpiredEmail(
                        "Residential",
                        p.getListingId(),
                        p.getTitle(),
                        p.getUserId()
                ));
    }

    private void sendExpiredEmail(
        String category,
        Integer listingId,
        String title,
        Integer userId
    ) {
        Users user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getEmail() == null) return;

        try {
            String subject = "Your listing titled- "+title+" has expired";

            String html = PropertyExpiryEmailTemplate.buildExpiredEmail(
                    user.getFirstName(),
                    title
            );

            mailSenderService.sendHtml(user.getEmail(), subject, html);

            System.out.println("Expired email sent for "+category+", "+listingId);

            NotificationDetails notification = new NotificationDetails();
            String message = "Your listing titled- "+title+" has expired. Please renew it from your Agent Dashboard.";
            notification.setNotificationMessage(message);
            notification.setNotificationType(NotificationType.ExpiredListing);
            notification.setNotificationReceiverId(user.getUserId());
            notification.setNotificationReceiverRole(Role.AGENT);
            notification.setNotificationSenderId(1);
            notification.setNotificationSenderRole(Role.ADMIN);
            notificationRepo.save(notification);

        }  catch (MessagingException e) {
            System.out.println("Failed to send expiry email!!!"+e.getMessage());
        }
    }
}
