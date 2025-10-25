// Author-Hemant Arora
package com.propadda.prop.service;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.propadda.prop.dto.CommercialPropertyRequest;
import com.propadda.prop.dto.UploadedMediaDto;
import com.propadda.prop.enumerations.NotificationType;
import com.propadda.prop.enumerations.Role;
import com.propadda.prop.mappers.CommercialPropertyMapper;
import com.propadda.prop.model.CommercialPropertyDetails;
import com.propadda.prop.model.CommercialPropertyMedia;
import com.propadda.prop.model.NotificationDetails;
import com.propadda.prop.model.Users;
import com.propadda.prop.repo.CommercialPropertyDetailsRepo;
import com.propadda.prop.repo.EnquiredListingsDetailsRepo;
import com.propadda.prop.repo.FavoriteListingsDetailsRepo;
import com.propadda.prop.repo.NotificationDetailsRepository;
import com.propadda.prop.repo.UsersRepo;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

@Service
public class CommercialPropertyDetailsService {

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private FavoriteListingsDetailsRepo favRepo;

    @Autowired
    private EnquiredListingsDetailsRepo enqRepo;

    @Autowired
    private NotificationDetailsRepository notificationRepo;

    @Autowired
    private MailSenderService mailService;

    @Autowired
    private GcsResumableService gcsResumableService;

    private final CommercialPropertyDetailsRepo repository;
    private final GcsService gcsService;

    public CommercialPropertyDetailsService(CommercialPropertyDetailsRepo repository, GcsService gcsService) {
        this.repository = repository;
        this.gcsService = gcsService;
    }

    @Transactional
    public CommercialPropertyDetails saveProperty(CommercialPropertyRequest property, List<MultipartFile> files) throws IOException, MessagingException {
        // Add validations if needed (e.g., preference not null, price > 0)
        if (property.getState() == null || property.getCity() == null || property.getLocality() == null) {
            throw new IllegalArgumentException("State, city, and locality must not be null");
        }

        CommercialPropertyDetails propDetails = new CommercialPropertyDetails(property.getAddress(), property.getAge(), property.getAdminApproved(), property.getArea(), property.getAvailability(), property.getCabins(), property.getCity(), property.getConferenceRoom(), property.getDescription(), property.isExpired(),property.getFloor(), property.getLift(), property.getLocality(), property.getLockIn(), property.getMeetingRoom(), property.getNearbyPlace(), property.getParking(), property.getPincode(), property.getPreference(), property.getPrice(), property.getPropertyType(), property.getReceptionArea(), property.getSecurityDeposit(), property.getState(), property.getTitle(), property.getTotalFloors(), property.isVip(), property.getWashroom(), property.getYearlyIncrease());

        //owner logic here
        if(property.getCommercialOwnerId()!=null){
            Users user = (usersRepo.findById(property.getCommercialOwnerId()).isPresent()) ? usersRepo.findById(property.getCommercialOwnerId()).get() : null;
            if(user!=null){
                propDetails.setCommercialOwner(user);
                if (user.getRole() == Role.BUYER) {
                    user.setRole(Role.AGENT);
                }
            }
        }
        
        if (files != null && !files.isEmpty()) {
        Integer orderImage = 1;
        Integer orderVideo = 11;
        Integer orderBrochure = 21;
        List<CommercialPropertyMedia> mediaFilesList = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = gcsService.uploadFile(file,"commercial");
            CommercialPropertyMedia media = new CommercialPropertyMedia();
            media.setUrl(url);
            media.setFilename(file.getOriginalFilename());
            media.setSize(file.getSize());
            media.setUploadedAt(Instant.now());
            media.setProperty(propDetails);
            String contentType = file.getContentType();
            if (contentType != null && contentType.startsWith("video/")) {
                media.setMediaType(CommercialPropertyMedia.MediaType.VIDEO);
                media.setOrd(orderVideo);
                orderVideo++;
            } else 
            if(contentType != null && contentType.startsWith("image/")) {
                media.setMediaType(CommercialPropertyMedia.MediaType.IMAGE);
                media.setOrd(orderImage);
                orderImage++;
            } else 
            if(contentType != null && (contentType.startsWith("application/") || contentType.startsWith("text/"))){
                media.setMediaType(CommercialPropertyMedia.MediaType.BROCHURE);
                media.setOrd(orderBrochure);
                orderBrochure++;
            } else {
                media.setMediaType(CommercialPropertyMedia.MediaType.OTHER);
                media.setOrd(0);
            }
        mediaFilesList.add(media);
        }
        propDetails.setCommercialPropertyMediaFiles(mediaFilesList);
    }
        propDetails.setCategory("Commercial");
        propDetails.setSold(false);
        propDetails.setVip(false);
        propDetails.setExpired(false);
        propDetails.setReraVerified(false);
        propDetails.setAdminApproved("Pending");
        repository.save(propDetails);

        //notification flow
        NotificationDetails notification = new NotificationDetails();
            notification.setNotificationType(NotificationType.ListingAcknowledgement);
            String message = "Thanks! Your property titled- "+property.getTitle()+" was received and is pending for approval.";
            notification.setNotificationMessage(message);
            notification.setNotificationReceiverId(property.getCommercialOwnerId());
            notification.setNotificationReceiverRole(Role.AGENT);
            notification.setNotificationSenderId(1);
            notification.setNotificationSenderRole(Role.ADMIN);
            notificationRepo.save(notification);
        
        //email flow
        String to = propDetails.getCommercialOwner().getEmail();
        String subject = "Received- "+property.getTitle();
        String body = "Thanks! Your property titled- "+property.getTitle()+" was received and is pending for approval.";
        mailService.send(to, subject, body);

        //notification flow for admin
        NotificationDetails notificationAdmin = new NotificationDetails();
        String messageAdmin = "New Property titled- "+property.getTitle()+" added. Approve/Reject";
        notificationAdmin.setNotificationType(NotificationType.ListingApprovalRequest);
        notificationAdmin.setNotificationMessage(messageAdmin);
        notificationAdmin.setNotificationReceiverId(1);
        notificationAdmin.setNotificationReceiverRole(Role.ADMIN);
        notificationAdmin.setNotificationSenderId(property.getCommercialOwnerId());
        notificationAdmin.setNotificationSenderRole(Role.AGENT);
        notificationRepo.save(notificationAdmin);

        //email flow for admin
        String toAdmin = "propaddadjs@gmail.com";
        String subjectAdmin = "New Listing Added";
        String bodyAdmin = "New Property titled- "+property.getTitle()+" added. Approve/Reject";
        mailService.send(toAdmin, subjectAdmin, bodyAdmin);

        return propDetails;
    }

    @Transactional
    public CommercialPropertyDetails savePropertyWithUploadedObjects(CommercialPropertyRequest property, List<UploadedMediaDto> uploadedMedia) throws IOException, MessagingException {
        // Validate mandatory fields (reuse check from saveProperty)
        if (property.getState() == null || property.getCity() == null || property.getLocality() == null) {
            throw new IllegalArgumentException("State, city, and locality must not be null");
        }

        CommercialPropertyDetails propDetails = new CommercialPropertyDetails(property.getAddress(), property.getAge(), property.getAdminApproved(), property.getArea(), property.getAvailability(), property.getCabins(), property.getCity(), property.getConferenceRoom(), property.getDescription(), property.isExpired(),property.getFloor(), property.getLift(), property.getLocality(), property.getLockIn(), property.getMeetingRoom(), property.getNearbyPlace(), property.getParking(), property.getPincode(), property.getPreference(), property.getPrice(), property.getPropertyType(), property.getReceptionArea(), property.getSecurityDeposit(), property.getState(), property.getTitle(), property.getTotalFloors(), property.isVip(), property.getWashroom(), property.getYearlyIncrease());

        //owner logic here
        if(property.getCommercialOwnerId()!=null){
            Users user = (usersRepo.findById(property.getCommercialOwnerId()).isPresent()) ? usersRepo.findById(property.getCommercialOwnerId()).get() : null;
            if(user!=null){
                propDetails.setCommercialOwner(user);
                if (user.getRole() == Role.BUYER) {
                    user.setRole(Role.AGENT);
                }
            }
        }

        List<CommercialPropertyMedia> mediaFilesList = new ArrayList<>();
        List<String> movedDestinations = new ArrayList<>();
        if (uploadedMedia != null && !uploadedMedia.isEmpty()) {
            Integer orderImage = 1;
            Integer orderVideo = 11;
            Integer orderBrochure = 21;

            for (UploadedMediaDto um : uploadedMedia) {
                // verify object exists and size matches (optional)
                Blob blob = gcsService.getStorage().get(BlobId.of(gcsService.getBucketName(), um.objectName));
                if (blob == null) {
                    throw new IllegalStateException("Uploaded object not found: " + um.objectName);
                }
                // Optionally compare size: blob.getSize() vs um.size

            // build destination object name in final folder
            // e.g. temp/UPLOADID/uuid-file.png -> uploads/commercial/UPLOADID/uuid-file.png
            String destObjectName;
            if (um.objectName.startsWith("temp/")) {
                destObjectName = um.objectName.replaceFirst("^temp/", "uploads/commercial/");
            } else {
                // if source isn't under temp for some reason, place into uploads/commercial/{uploadId}/...
                // fallback: prefix with uploads/commercial/
                destObjectName = "uploads/commercial/" + um.objectName;
            }

            // move (copy then delete)
            gcsService.moveObject(um.objectName, destObjectName);
            movedDestinations.add(destObjectName);

            // optional: verify destination exists
            Blob destBlob = gcsService.getStorage().get(BlobId.of(gcsService.getBucketName(), destObjectName));
            if (destBlob == null) {
                throw new IllegalStateException("Failed to move to destination: " + destObjectName);
            }
                CommercialPropertyMedia media = new CommercialPropertyMedia();
                // choose final public URL or keep temp objectName and later copy — here we create public signed URL
                // String publicUrl = String.format("https://storage.googleapis.com/%s/%s", gcsService.getBucketName(), destObjectName);
                // media.setUrl(publicUrl);
                String signedGet = gcsService.generateV4GetSignedUrl(destObjectName); // 1 year
                media.setUrl(signedGet);
                media.setFilename(um.name);
                media.setObjectName(destObjectName);
                media.setSize(um.size);
                media.setUploadedAt(Instant.now());
                media.setProperty(propDetails);

                String mediaType = um.mediaType != null ? um.mediaType.toUpperCase() : "OTHER";
                switch (mediaType) {
                    case "VIDEO" -> {
                        media.setMediaType(CommercialPropertyMedia.MediaType.VIDEO);
                        media.setOrd(orderVideo++);
                    }
                    case "IMAGE" -> {
                        media.setMediaType(CommercialPropertyMedia.MediaType.IMAGE);
                        media.setOrd(orderImage++);
                    }
                    case "BROCHURE" -> {
                        media.setMediaType(CommercialPropertyMedia.MediaType.BROCHURE);
                        media.setOrd(orderBrochure++);
                    }
                    default -> {
                        media.setMediaType(CommercialPropertyMedia.MediaType.OTHER);
                        media.setOrd(0);
                    }
                }
                mediaFilesList.add(media);
            }
            propDetails.setCommercialPropertyMediaFiles(mediaFilesList);
        }

        // set defaults
        propDetails.setCategory("Commercial");
        propDetails.setSold(false);
        propDetails.setVip(false);
        propDetails.setExpired(false);
        propDetails.setReraVerified(false);
        propDetails.setAdminApproved("Pending");

        repository.save(propDetails);

        //notification flow
        NotificationDetails notification = new NotificationDetails();
            notification.setNotificationType(NotificationType.ListingAcknowledgement);
            String message = "Thanks! Your property titled- "+property.getTitle()+" was received and is pending for approval.";
            notification.setNotificationMessage(message);
            notification.setNotificationReceiverId(property.getCommercialOwnerId());
            notification.setNotificationReceiverRole(Role.AGENT);
            notification.setNotificationSenderId(1);
            notification.setNotificationSenderRole(Role.ADMIN);
            notificationRepo.save(notification);
        
        //email flow
        String to = propDetails.getCommercialOwner().getEmail();
        String subject = "Received- "+property.getTitle();
        String body = "Thanks! Your property titled- "+property.getTitle()+" was received and is pending for approval.";
        mailService.send(to, subject, body);

        //notification flow for admin
        NotificationDetails notificationAdmin = new NotificationDetails();
        String messageAdmin = "New Property titled- "+property.getTitle()+" added. Approve/Reject";
        notificationAdmin.setNotificationType(NotificationType.ListingApprovalRequest);
        notificationAdmin.setNotificationMessage(messageAdmin);
        notificationAdmin.setNotificationReceiverId(1);
        notificationAdmin.setNotificationReceiverRole(Role.ADMIN);
        notificationAdmin.setNotificationSenderId(property.getCommercialOwnerId());
        notificationAdmin.setNotificationSenderRole(Role.AGENT);
        notificationRepo.save(notificationAdmin);

        //email flow for admin
        String toAdmin = "propaddadjs@gmail.com";
        String subjectAdmin = "New Listing Added";
        String bodyAdmin = "New Property titled- "+property.getTitle()+" added. Approve/Reject";
        mailService.send(toAdmin, subjectAdmin, bodyAdmin);

        return propDetails;
    }

    @Transactional
    public void deleteProperty(Integer listingId, Integer agentId) {
        CommercialPropertyDetails cpd = repository.findByListingIdAndCommercialOwner_UserId(listingId, agentId).orElseThrow(() -> new IllegalArgumentException("Property not found or not owned by agent"));
        
            for(CommercialPropertyMedia m : cpd.getCommercialPropertyMediaFiles()){
                String url = m.getUrl();
                gcsService.deleteFile(url);
            }
            favRepo.deleteByPropertyIdAndPropertyType(listingId,"Commercial");
            enqRepo.deleteByPropertyIdAndPropertyType(listingId, "Commercial");
            repository.deleteById(listingId);
        
    }

    @Transactional
    public Object updateProperty(CommercialPropertyRequest property, List<MultipartFile> files, Integer agentId) throws IOException, MessagingException {
        CommercialPropertyDetails propModel = repository.findByListingIdAndCommercialOwner_UserId(property.getListingId(), agentId)
            .orElseThrow(() -> new IllegalArgumentException("Property not found or not owned by agent"));
        
        CommercialPropertyDetails updated = CommercialPropertyMapper.requestToModel(propModel, property);

        if (files != null && !files.isEmpty()) {
            Integer order = 1;
            List<CommercialPropertyMedia> mediaFilesList = new ArrayList<>();
            for (MultipartFile file : files) {
                String url = gcsService.uploadFile(file, "commercial");
                CommercialPropertyMedia media = new CommercialPropertyMedia();
                media.setUrl(url);
                media.setFilename(file.getOriginalFilename());
                media.setSize(file.getSize());
                media.setUploadedAt(Instant.now());
                // set parent backref (still ok, but collection instance must not be replaced)
                media.setProperty(updated);

                String contentType = file.getContentType();
                if (contentType != null && contentType.startsWith("video/")) {
                    media.setMediaType(CommercialPropertyMedia.MediaType.VIDEO);
                    media.setOrd(0);
                } else if (contentType != null && contentType.startsWith("image/")) {
                    media.setMediaType(CommercialPropertyMedia.MediaType.IMAGE);
                    media.setOrd(order++);
                } else if (contentType != null && (contentType.startsWith("application/") || contentType.startsWith("text/"))) {
                    media.setMediaType(CommercialPropertyMedia.MediaType.BROCHURE);
                    media.setOrd(-1);
                } else {
                    media.setMediaType(CommercialPropertyMedia.MediaType.OTHER);
                    media.setOrd(-2);
                }
                mediaFilesList.add(media);
            }

            // ---- mutate existing collection instead of replacing it ----
            List<CommercialPropertyMedia> existing = updated.getCommercialPropertyMediaFiles();
            if (existing == null) {
                // defensive: entity should ideally initialize this to avoid this branch
                existing = new ArrayList<>();
                updated.setCommercialPropertyMediaFiles(existing);
            } else {
                existing.clear(); // retain same collection instance so Hibernate can manage orphans
            }

            for (CommercialPropertyMedia m : mediaFilesList) {
                // ensure bidirectional link
                m.setProperty(updated);
                existing.add(m);
            }
        }

        updated.setCategory("Commercial");
        updated.setSold(false);
        updated.setVip(false);
        updated.setExpired(false);
        updated.setReraVerified(false);
        updated.setAdminApproved("Pending");

        repository.save(updated);

         //notification flow
        NotificationDetails notification = new NotificationDetails();
            notification.setNotificationType(NotificationType.ListingAcknowledgement);
            String message = "Thanks! Your property titled- "+property.getTitle()+" was received after update and is pending for approval.";
            notification.setNotificationMessage(message);
            notification.setNotificationReceiverId(agentId);
            notification.setNotificationReceiverRole(Role.AGENT);
            notification.setNotificationSenderId(1);
            notification.setNotificationSenderRole(Role.ADMIN);
            notificationRepo.save(notification);
        
        //email flow
        String to = updated.getCommercialOwner().getEmail();
        String subject = "Updated- "+property.getTitle();
        String body = "Thanks! Your property titled- "+property.getTitle()+" was received after update and is pending for approval.";
        mailService.send(to, subject, body);

        //notification flow for admin
        NotificationDetails notificationAdmin = new NotificationDetails();
        String messageAdmin = "Property titled- "+property.getTitle()+" updated. Approve/Reject";
        notificationAdmin.setNotificationType(NotificationType.ListingApprovalRequest);
        notificationAdmin.setNotificationMessage(messageAdmin);
        notificationAdmin.setNotificationReceiverId(1);
        notificationAdmin.setNotificationReceiverRole(Role.ADMIN);
        notificationAdmin.setNotificationSenderId(agentId);
        notificationAdmin.setNotificationSenderRole(Role.AGENT);
        notificationRepo.save(notificationAdmin);

        //email flow for admin
        String toAdmin = "propaddadjs@gmail.com";
        String subjectAdmin = "Listing Updated- "+property.getTitle();
        String bodyAdmin = "Property titled- "+property.getTitle()+" updated. Approve/Reject";
        mailService.send(toAdmin, subjectAdmin, bodyAdmin);

        return updated;

    }

    @Transactional
    public Object updatePropertyWithUploadedObjects(CommercialPropertyRequest property, List<UploadedMediaDto> uploadedMedia, Integer agentId) throws IOException, MessagingException {
        System.out.println("Commercial property update (claimed uploads) request: " + property + " agent id: " + agentId);

        // Load existing property and validate ownership
        CommercialPropertyDetails propModel = repository.findByListingIdAndCommercialOwner_UserId(property.getListingId(), agentId)
            .orElseThrow(() -> new IllegalArgumentException("Property not found or not owned by agent"));

        // Map request into existing entity (reuse your mapper)
        CommercialPropertyDetails updated = CommercialPropertyMapper.requestToModel(propModel, property);
        // If uploaded media provided, verify and attach as media entities
        if (uploadedMedia != null && !uploadedMedia.isEmpty()) {
            Integer orderImage = 1;
            Integer orderVideo = 11;
            Integer orderBrochure = 21;

            List<CommercialPropertyMedia> mediaFilesList = new ArrayList<>();
            List<String> movedDestinations = new ArrayList<>();
            for (UploadedMediaDto um : uploadedMedia) {
                // Try to fetch blob metadata via GcsResumableService first (recommended)
                Blob blob = null;
                try {
                    blob = gcsResumableService.getBlobMetadata(um.objectName);
                } catch (Exception ex) {
                    // fallback to GcsService storage access if gcsResumableService isn't available
                    blob = gcsService.getStorage().get(BlobId.of(gcsService.getBucketName(), um.objectName));
                }

                if (blob == null) {
                    throw new IllegalStateException("Uploaded object not found in GCS: " + um.objectName);
                }

                // build destination object name in final folder
            // e.g. temp/UPLOADID/uuid-file.png -> uploads/commercial/UPLOADID/uuid-file.png
            String destObjectName;
            if (um.objectName.startsWith("temp/")) {
                destObjectName = um.objectName.replaceFirst("^temp/", "uploads/commercial/");
            } else {
                // if source isn't under temp for some reason, place into uploads/commercial/{uploadId}/...
                // fallback: prefix with uploads/commercial/
                destObjectName = "uploads/commercial/" + um.objectName;
            }

            // move (copy then delete)
            gcsService.moveObject(um.objectName, destObjectName);
            movedDestinations.add(destObjectName);

            // optional: verify destination exists
            Blob destBlob = gcsService.getStorage().get(BlobId.of(gcsService.getBucketName(), destObjectName));
            if (destBlob == null) {
                throw new IllegalStateException("Failed to move to destination: " + destObjectName);
            }

                // Optional: size check (log mismatch, but not mandatory to fail)
                if (um.size > 0 && blob.getSize() != um.size) {
                    System.out.println("Warning: size mismatch for " + um.objectName + " expected=" + um.size + " actual=" + blob.getSize());
                }

                CommercialPropertyMedia media = new CommercialPropertyMedia();
                // Set public URL for consumption in frontend (or keep objectName and copy later)
                // String publicUrl = String.format("https://storage.googleapis.com/%s/%s", gcsService.getBucketName(), um.objectName);
                // media.setUrl(publicUrl);
                String signedGet = gcsService.generateV4GetSignedUrl(destObjectName); // 1 year
                media.setUrl(signedGet);
                media.setFilename(um.name);
                media.setObjectName(destObjectName);
                media.setSize(um.size);
                media.setUploadedAt(Instant.now());
                media.setProperty(updated);

                String mt = (um.mediaType == null) ? "" : um.mediaType.toUpperCase();
                switch (mt) {
                    case "VIDEO" -> {
                        media.setMediaType(CommercialPropertyMedia.MediaType.VIDEO);
                        media.setOrd(orderVideo++);
                    }
                    case "IMAGE" -> {
                        media.setMediaType(CommercialPropertyMedia.MediaType.IMAGE);
                        media.setOrd(orderImage++);
                    }
                    case "BROCHURE" -> {
                        media.setMediaType(CommercialPropertyMedia.MediaType.BROCHURE);
                        media.setOrd(orderBrochure++);
                    }
                    default -> {
                        media.setMediaType(CommercialPropertyMedia.MediaType.OTHER);
                        media.setOrd(0);
                    }
                }
                mediaFilesList.add(media);
            }

            // Replace elements inside the existing collection instance (avoid replacing the collection object)
            List<CommercialPropertyMedia> existing = updated.getCommercialPropertyMediaFiles();
            if (existing == null) {
                existing = new ArrayList<>();
                updated.setCommercialPropertyMediaFiles(existing);
            } else {
                existing.clear();
            }
            for (CommercialPropertyMedia m : mediaFilesList) {
                m.setProperty(updated);
                existing.add(m);
            }
        }

        // Preserve same defaults as your other update flow
        updated.setCategory("Commercial");
        updated.setSold(false);
        updated.setVip(false);
        updated.setExpired(false);
        updated.setReraVerified(false);
        updated.setAdminApproved("Pending");

        // Persist
        repository.save(updated);

        // Notification flow (same pattern as your existing updateProperty)
        NotificationDetails notification = new NotificationDetails();
        notification.setNotificationType(NotificationType.ListingAcknowledgement);
        String message = "Thanks! Your property titled- " + property.getTitle() + " was received after update and is pending for approval.";
        notification.setNotificationMessage(message);
        notification.setNotificationReceiverId(agentId);
        notification.setNotificationReceiverRole(Role.AGENT);
        notification.setNotificationSenderId(1);
        notification.setNotificationSenderRole(Role.ADMIN);
        notificationRepo.save(notification);

        // Email flow to owner
        String to = updated.getCommercialOwner().getEmail();
        String subject = "Updated- " + property.getTitle();
        String body = "Thanks! Your property titled- " + property.getTitle() + " was received after update and is pending for approval.";
        mailService.send(to, subject, body);

        // Notification flow for admin
        NotificationDetails notificationAdmin = new NotificationDetails();
        String messageAdmin = "Property titled- " + property.getTitle() + " updated. Approve/Reject";
        notificationAdmin.setNotificationType(NotificationType.ListingApprovalRequest);
        notificationAdmin.setNotificationMessage(messageAdmin);
        notificationAdmin.setNotificationReceiverId(1);
        notificationAdmin.setNotificationReceiverRole(Role.ADMIN);
        notificationAdmin.setNotificationSenderId(agentId);
        notificationAdmin.setNotificationSenderRole(Role.AGENT);
        notificationRepo.save(notificationAdmin);

        // Email flow for admin
        String toAdmin = "propaddadjs@gmail.com";
        String subjectAdmin = "Listing Updated- " + property.getTitle();
        String bodyAdmin = "Property titled- " + property.getTitle() + " updated. Approve/Reject";
        mailService.send(toAdmin, subjectAdmin, bodyAdmin);

        return updated;
    }


    @Transactional
    public void deletePropertyMedia(Integer listingId, Integer agentId) {
       CommercialPropertyDetails prop = repository.findByListingIdAndCommercialOwner_UserId(listingId, agentId).orElseThrow(() -> new IllegalArgumentException("Property not found or not owned by agent"));

        for(CommercialPropertyMedia m : prop.getCommercialPropertyMediaFiles()){
                String url = m.getUrl();
                gcsService.deleteFile(url);
            }
        prop.getCommercialPropertyMediaFiles().clear();
        repository.save(prop);
    }
}
