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
import com.propadda.prop.dto.ResidentialPropertyRequest;
import com.propadda.prop.dto.UploadedMediaDto;
import com.propadda.prop.enumerations.NotificationType;
import com.propadda.prop.enumerations.Role;
import com.propadda.prop.mappers.ResidentialPropertyMapper;
import com.propadda.prop.model.NotificationDetails;
import com.propadda.prop.model.ResidentialPropertyAmenities;
import com.propadda.prop.model.ResidentialPropertyDetails;
import com.propadda.prop.model.ResidentialPropertyMedia;
import com.propadda.prop.model.Users;
import com.propadda.prop.repo.EnquiredListingsDetailsRepo;
import com.propadda.prop.repo.FavoriteListingsDetailsRepo;
import com.propadda.prop.repo.NotificationDetailsRepository;
import com.propadda.prop.repo.ResidentialPropertyDetailsRepo;
import com.propadda.prop.repo.UsersRepo;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

@Service
public class ResidentialPropertyDetailsService {

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

    private final ResidentialPropertyDetailsRepo repository;
    private final GcsService gcsService;

    public ResidentialPropertyDetailsService(ResidentialPropertyDetailsRepo repository, GcsService gcsService) {
        this.repository = repository;
        this.gcsService = gcsService;
    }

    // Create or Update property
    @Transactional
    public ResidentialPropertyDetails saveProperty(ResidentialPropertyRequest property, List<MultipartFile> files) throws IOException, MessagingException {
    	// Mandatory fields validation
        if (property.getState() == null || property.getCity() == null || property.getLocality() == null) {
            throw new IllegalArgumentException("State, city, and locality must not be null");
        }
        ResidentialPropertyDetails propDetails = new ResidentialPropertyDetails(property.getAddress(), property.getAge(), property.getAdminApproved(), property.getArea(), property.getAvailability(), property.getPossessionBy(), property.getBalconies(), property.getBathrooms(), property.getBedrooms(), property.getCity(), property.getCoveredParking(), property.getDescription(), property.isExpired(), property.getFacing(), property.getFloor(), property.getFurnishing(), property.getLocality(), property.getMaintenance(), property.getNearbyPlace(), property.getOpenParking(), property.getPincode(), property.getPowerBackup(), property.getPreference(), property.getPrice(), property.getPropertyType(), property.getReraNumber(), property.getReraVerified(), property.getSecurityDeposit(), property.getState(), property.getTitle(), property.getTotalFloors(), property.isVip());
        
        ResidentialPropertyAmenities amenities = new ResidentialPropertyAmenities(property.isAiryRooms(), property.isBankAttachedProperty(), property.isBorewellTank(), property.isCenterCooling(), property.isCentrallyAirConditioned(), property.isCloseToAirport(), property.isCloseToHighway(), property.isCloseToHospital(), property.isCloseToMall(), property.isCloseToMarket(), property.isCloseToMetroStation(), property.isCloseToRailwayStation(), property.isCloseToSchool(), property.isClubhouseCommunityCenter(), property.isCornerProperty(), property.isDishwasher(), property.isDryer(), property.isElevator(), property.isEmergencyExit(), property.isFalseCeilingLighting(), property.isFireAlarm(), property.isFitnessCenter(), property.isGasPipeline(), property.isGym(), property.isHeating(), property.isHighCeilingHeight(), property.isInGatedSociety(), property.isIntercomFacility(), property.isInternetConnectivity(), property.isLaundry(), property.isLifts(), property.isLowDensitySociety(), property.isMaintenanceStaff(), property.isModularKitchen(), property.isMunicipalCorporation(), property.isNaturalLight(), 
        property.isNoOpenDrainageAround(), property.isOverlookingClub(), property.isOverlookingMainRoad(), property.isOverlookingParkGarden(), property.isOverlookingPool(), property.isPark(), property.isPetFriendly(), property.isPetFriendlySociety(), property.isPoojaRoom(), property.isPool(), property.isPrivateGardenTerrace(), property.isRainWaterHarvesting(), property.isRecentlyRenovated(), propDetails, property.isSauna(), property.isSecurityFireAlarm(), property.isSecurityPersonnel(), property.isSeparateEntryForServantRoom(), property.isServantRoom(), property.isSpaciousInteriors(), property.isStorage(), property.isStoreRoom(), property.isStudyRoom(), property.isSwimmingPool(), property.isVastuCompliant(), property.isWater24x7(), property.isWaterPurifier(), 
        property.isWheelchairFriendly());

        propDetails.setAmenities(amenities);

        //owner logic here
        if(property.getResidentialOwnerId()!=null){
            Users user = (usersRepo.findById(property.getResidentialOwnerId()).isPresent()) ? usersRepo.findById(property.getResidentialOwnerId()).get() : null;
            if(user!=null){
                propDetails.setResidentialOwner(user);
                if (user.getRole() == Role.BUYER) {
                    user.setRole(Role.AGENT);
                }
            }
        }
        
        if(files!=null && !files.isEmpty()){
        Integer orderImage = 1;
        Integer orderVideo = 11;
        Integer orderBrochure = 21;
        List<ResidentialPropertyMedia> mediaFilesList = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = gcsService.uploadFile(file,"residential");
            ResidentialPropertyMedia media = new ResidentialPropertyMedia();
            media.setUrl(url);
            media.setFilename(file.getOriginalFilename());
            media.setSize(file.getSize());
            media.setUploadedAt(Instant.now());
            media.setProperty(propDetails);
            String contentType = file.getContentType();
            if (contentType != null && contentType.startsWith("video/")) {
                media.setMediaType(ResidentialPropertyMedia.MediaType.VIDEO);
                media.setOrd(orderVideo);
                orderVideo++;
            } else 
            if(contentType != null && contentType.startsWith("image/")) {
                media.setMediaType(ResidentialPropertyMedia.MediaType.IMAGE);
                media.setOrd(orderImage);
                orderImage++;
            } else 
            if(contentType != null && (contentType.startsWith("application/") || contentType.startsWith("text/"))){
                media.setMediaType(ResidentialPropertyMedia.MediaType.BROCHURE);
                media.setOrd(orderBrochure);
                orderBrochure++;
            } else {
                media.setMediaType(ResidentialPropertyMedia.MediaType.OTHER);
                media.setOrd(0);
            }
        mediaFilesList.add(media);
        }
        
        propDetails.setResidentialPropertyMediaFiles(mediaFilesList);
    }
        propDetails.setCategory("Residential");
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
            notification.setNotificationReceiverId(property.getResidentialOwnerId());
            notification.setNotificationReceiverRole(Role.AGENT);
            notification.setNotificationSenderId(1);
            notification.setNotificationSenderRole(Role.ADMIN);
            notificationRepo.save(notification);
        
        //email flow
        String to = propDetails.getResidentialOwner().getEmail();
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
        notificationAdmin.setNotificationSenderId(property.getResidentialOwnerId());
        notificationAdmin.setNotificationSenderRole(Role.AGENT);
        notificationRepo.save(notificationAdmin);

        //email flow for admin
        String toAdmin = "sales@propadda.in";
        String subjectAdmin = "New Listing Added";
        String bodyAdmin = "New Property titled- "+property.getTitle()+" added. Approve/Reject";
        mailService.send(toAdmin, subjectAdmin, bodyAdmin);

        return propDetails;
    }

    @Transactional
    public ResidentialPropertyDetails savePropertyWithUploadedObjects(ResidentialPropertyRequest property, List<UploadedMediaDto> uploadedMedia) throws IOException, MessagingException {
        // Validate mandatory fields (reuse check from saveProperty)
        if (property.getState() == null || property.getCity() == null || property.getLocality() == null) {
            throw new IllegalArgumentException("State, city, and locality must not be null");
        }

        ResidentialPropertyDetails propDetails = new ResidentialPropertyDetails(property.getAddress(), property.getAge(), property.getAdminApproved(), property.getArea(), property.getAvailability(), property.getPossessionBy(), property.getBalconies(), property.getBathrooms(), property.getBedrooms(), property.getCity(), property.getCoveredParking(), property.getDescription(), property.isExpired(), property.getFacing(), property.getFloor(), property.getFurnishing(), property.getLocality(), property.getMaintenance(), property.getNearbyPlace(), property.getOpenParking(), property.getPincode(), property.getPowerBackup(), property.getPreference(), property.getPrice(), property.getPropertyType(), property.getReraNumber(), property.getReraVerified(), property.getSecurityDeposit(), property.getState(), property.getTitle(), property.getTotalFloors(), property.isVip());
        
        ResidentialPropertyAmenities amenities = new ResidentialPropertyAmenities(property.isAiryRooms(), property.isBankAttachedProperty(), property.isBorewellTank(), property.isCenterCooling(), property.isCentrallyAirConditioned(), property.isCloseToAirport(), property.isCloseToHighway(), property.isCloseToHospital(), property.isCloseToMall(), property.isCloseToMarket(), property.isCloseToMetroStation(), property.isCloseToRailwayStation(), property.isCloseToSchool(), property.isClubhouseCommunityCenter(), property.isCornerProperty(), property.isDishwasher(), property.isDryer(), property.isElevator(), property.isEmergencyExit(), property.isFalseCeilingLighting(), property.isFireAlarm(), property.isFitnessCenter(), property.isGasPipeline(), property.isGym(), property.isHeating(), property.isHighCeilingHeight(), property.isInGatedSociety(), property.isIntercomFacility(), property.isInternetConnectivity(), property.isLaundry(), property.isLifts(), property.isLowDensitySociety(), property.isMaintenanceStaff(), property.isModularKitchen(), property.isMunicipalCorporation(), property.isNaturalLight(), 
        property.isNoOpenDrainageAround(), property.isOverlookingClub(), property.isOverlookingMainRoad(), property.isOverlookingParkGarden(), property.isOverlookingPool(), property.isPark(), property.isPetFriendly(), property.isPetFriendlySociety(), property.isPoojaRoom(), property.isPool(), property.isPrivateGardenTerrace(), property.isRainWaterHarvesting(), property.isRecentlyRenovated(), propDetails, property.isSauna(), property.isSecurityFireAlarm(), property.isSecurityPersonnel(), property.isSeparateEntryForServantRoom(), property.isServantRoom(), property.isSpaciousInteriors(), property.isStorage(), property.isStoreRoom(), property.isStudyRoom(), property.isSwimmingPool(), property.isVastuCompliant(), property.isWater24x7(), property.isWaterPurifier(), 
        property.isWheelchairFriendly());

        propDetails.setAmenities(amenities);

        //owner logic here
        if(property.getResidentialOwnerId()!=null){
            Users user = (usersRepo.findById(property.getResidentialOwnerId()).isPresent()) ? usersRepo.findById(property.getResidentialOwnerId()).get() : null;
            if(user!=null){
                propDetails.setResidentialOwner(user);
                if (user.getRole() == Role.BUYER) {
                    user.setRole(Role.AGENT);
                }
            }
        }

        List<ResidentialPropertyMedia> mediaFilesList = new ArrayList<>();
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
                destObjectName = um.objectName.replaceFirst("^temp/", "uploads/residential/");
            } else {
                // if source isn't under temp for some reason, place into uploads/commercial/{uploadId}/...
                // fallback: prefix with uploads/commercial/
                destObjectName = "uploads/residential/" + um.objectName;
            }

            // move (copy then delete)
            gcsService.moveObject(um.objectName, destObjectName);
            movedDestinations.add(destObjectName);

            // optional: verify destination exists
            Blob destBlob = gcsService.getStorage().get(BlobId.of(gcsService.getBucketName(), destObjectName));
            if (destBlob == null) {
                throw new IllegalStateException("Failed to move to destination: " + destObjectName);
            }

                ResidentialPropertyMedia media = new ResidentialPropertyMedia();
                // choose final public URL or keep temp objectName and later copy — here we create public signed URL
                // String publicUrl = String.format("https://storage.googleapis.com/%s/%s", gcsService.getBucketName(), destObjectName);
                // media.setUrl(publicUrl);
                String signedGet = gcsService.generateV4GetSignedUrl(destObjectName); // 1 year
                media.setUrl(signedGet);
                media.setFilename(um.name);
                media.setObjectName(destObjectName);
                media.setFilename(um.name);
                media.setSize(um.size);
                media.setUploadedAt(Instant.now());
                media.setProperty(propDetails);

                String mediaType = um.mediaType != null ? um.mediaType.toUpperCase() : "OTHER";
                switch (mediaType) {
                    case "VIDEO" -> {
                        media.setMediaType(ResidentialPropertyMedia.MediaType.VIDEO);
                        media.setOrd(orderVideo++);
                    }
                    case "IMAGE" -> {
                        media.setMediaType(ResidentialPropertyMedia.MediaType.IMAGE);
                        media.setOrd(orderImage++);
                    }
                    case "BROCHURE" -> {
                        media.setMediaType(ResidentialPropertyMedia.MediaType.BROCHURE);
                        media.setOrd(orderBrochure++);
                    }
                    default -> {
                        media.setMediaType(ResidentialPropertyMedia.MediaType.OTHER);
                        media.setOrd(0);
                    }
                }
                mediaFilesList.add(media);
            }
            propDetails.setResidentialPropertyMediaFiles(mediaFilesList);
        }

        // set defaults
        propDetails.setCategory("Residential");
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
            notification.setNotificationReceiverId(property.getResidentialOwnerId());
            notification.setNotificationReceiverRole(Role.AGENT);
            notification.setNotificationSenderId(1);
            notification.setNotificationSenderRole(Role.ADMIN);
            notificationRepo.save(notification);
        
        //email flow
        String to = propDetails.getResidentialOwner().getEmail();
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
        notificationAdmin.setNotificationSenderId(property.getResidentialOwnerId());
        notificationAdmin.setNotificationSenderRole(Role.AGENT);
        notificationRepo.save(notificationAdmin);

        //email flow for admin
        String toAdmin = "sales@propadda.in";
        String subjectAdmin = "New Listing Added";
        String bodyAdmin = "New Property titled- "+property.getTitle()+" added. Approve/Reject";
        mailService.send(toAdmin, subjectAdmin, bodyAdmin);

        return propDetails;
    }
    
    // Delete property
    @Transactional
    public void deleteProperty(Integer listingId, Integer agentId) {
        ResidentialPropertyDetails rpd = repository.findByListingIdAndResidentialOwner_UserId(listingId,agentId).orElseThrow(() -> new IllegalArgumentException("Property not found or not owned by agent"));
        
            for(ResidentialPropertyMedia m : rpd.getResidentialPropertyMediaFiles()){
                String url = m.getUrl();
                gcsService.deleteFile(url);
            }
            favRepo.deleteByPropertyIdAndPropertyType(listingId,"Commercial");
            enqRepo.deleteByPropertyIdAndPropertyType(listingId, "Commercial");
            repository.deleteById(listingId);
        
    }

    @Transactional
    public Object updateProperty(ResidentialPropertyRequest property, List<MultipartFile> files, Integer agentId) throws IOException, MessagingException {
        System.out.println("Property request: "+property.toString()+" agent id: "+agentId);
        ResidentialPropertyDetails propModel = repository.findByListingIdAndResidentialOwner(property.getListingId(), usersRepo.findById(agentId).get())
            .orElseThrow(() -> new IllegalArgumentException("Property not found or not owned by agent"));
        
        ResidentialPropertyDetails updated = ResidentialPropertyMapper.requestToModel(propModel, property);

        if (files != null && !files.isEmpty()) {
            Integer order = 1;
            List<ResidentialPropertyMedia> mediaFilesList = new ArrayList<>();
            for (MultipartFile file : files) {
                String url = gcsService.uploadFile(file,"residential");
                ResidentialPropertyMedia media = new ResidentialPropertyMedia();
                media.setUrl(url);
                media.setFilename(file.getOriginalFilename());
                media.setSize(file.getSize());
                media.setUploadedAt(Instant.now());
                // set parent now (still ok, but collection instance must not be replaced)
                media.setProperty(updated);
                String contentType = file.getContentType();
                if (contentType != null && contentType.startsWith("video/")) {
                    media.setMediaType(ResidentialPropertyMedia.MediaType.VIDEO);
                    media.setOrd(0);
                } else if (contentType != null && contentType.startsWith("image/")) {
                    media.setMediaType(ResidentialPropertyMedia.MediaType.IMAGE);
                    media.setOrd(order++);
                } else if (contentType != null && (contentType.startsWith("application/") || contentType.startsWith("text/"))) {
                    media.setMediaType(ResidentialPropertyMedia.MediaType.BROCHURE);
                    media.setOrd(-1);
                } else {
                    media.setMediaType(ResidentialPropertyMedia.MediaType.OTHER);
                    media.setOrd(-2);
                }
                mediaFilesList.add(media);
            }

            // ---- mutate existing collection instead of replacing it ----
            List<ResidentialPropertyMedia> existing = updated.getResidentialPropertyMediaFiles();
            if (existing == null) {
                // defensive: if entity didn't initialize the collection
                existing = new ArrayList<>();
                // only allowed once — prefer entity to initialize the list to avoid this branch
                updated.setResidentialPropertyMediaFiles(existing);
            } else {
                existing.clear(); // keep same collection instance so Hibernate can manage orphans
            }

            for (ResidentialPropertyMedia m : mediaFilesList) {
                // ensure bidirectional link
                m.setProperty(updated);
                existing.add(m);
            }
        }
        updated.setCategory("Residential");
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
        String to = updated.getResidentialOwner().getEmail();
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
        String toAdmin = "sales@propadda.in";
        String subjectAdmin = "Listing Updated- "+property.getTitle();
        String bodyAdmin = "Property titled- "+property.getTitle()+" updated. Approve/Reject";
        mailService.send(toAdmin, subjectAdmin, bodyAdmin);

        return updated;
    }

    @Transactional
    public Object updatePropertyWithUploadedObjects(ResidentialPropertyRequest property, List<UploadedMediaDto> uploadedMedia, Integer agentId) throws IOException, MessagingException {
        System.out.println("Property update (claimed uploads) request: " + property + " agent id: " + agentId);
        ResidentialPropertyDetails propModel = repository.findByListingIdAndResidentialOwner(property.getListingId(), usersRepo.findById(agentId).get())
            .orElseThrow(() -> new IllegalArgumentException("Property not found or not owned by agent"));

        ResidentialPropertyDetails updated = ResidentialPropertyMapper.requestToModel(propModel, property);
        // Build Media entities from uploadedMedia (if provided)
        if (uploadedMedia != null && !uploadedMedia.isEmpty()) {
            Integer order = 1;
            List<ResidentialPropertyMedia> mediaFilesList = new ArrayList<>();
            List<String> movedDestinations = new ArrayList<>();
            for (UploadedMediaDto um : uploadedMedia) {
                // verify object exists in GCS
                Blob blob = null;
                try {
                    blob = gcsResumableService.getBlobMetadata(um.objectName);
                } catch (Exception ex) {
                    // defensive: try via GcsService as fallback if you didn't add getBlobMetadata
                    blob = gcsService.getStorage().get(BlobId.of(gcsService.getBucketName(), um.objectName));
                }
                if (blob == null) {
                    throw new IllegalStateException("Uploaded object not found: " + um.objectName);
                }
                // optional size check:
                if (um.size > 0 && blob.getSize() != um.size) {
                    // Log mismatch but do not necessarily fail — choose your policy
                    System.out.println("Warning: size mismatch for " + um.objectName + " expected=" + um.size + " actual=" + blob.getSize());
                }

                    // build destination object name in final folder
            // e.g. temp/UPLOADID/uuid-file.png -> uploads/commercial/UPLOADID/uuid-file.png
            String destObjectName;
            if (um.objectName.startsWith("temp/")) {
                destObjectName = um.objectName.replaceFirst("^temp/", "uploads/residential/");
            } else {
                // if source isn't under temp for some reason, place into uploads/commercial/{uploadId}/...
                // fallback: prefix with uploads/commercial/
                destObjectName = "uploads/residential/" + um.objectName;
            }

            // move (copy then delete)
            gcsService.moveObject(um.objectName, destObjectName);
            movedDestinations.add(destObjectName);

            // optional: verify destination exists
            Blob destBlob = gcsService.getStorage().get(BlobId.of(gcsService.getBucketName(), destObjectName));
            if (destBlob == null) {
                throw new IllegalStateException("Failed to move to destination: " + destObjectName);
            }

                ResidentialPropertyMedia media = new ResidentialPropertyMedia();
                // For display, point to public URL (or keep objectName and later copy to final path)
                // String publicUrl = String.format("https://storage.googleapis.com/%s/%s", gcsService.getBucketName(), um.objectName);
                // media.setUrl(publicUrl);
                // media.setFilename(um.name);
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
                        media.setMediaType(ResidentialPropertyMedia.MediaType.VIDEO);
                        media.setOrd(0);
                    }
                    case "IMAGE" -> {
                        media.setMediaType(ResidentialPropertyMedia.MediaType.IMAGE);
                        media.setOrd(order++);
                    }
                    case "BROCHURE" -> {
                        media.setMediaType(ResidentialPropertyMedia.MediaType.BROCHURE);
                        media.setOrd(-1);
                    }
                    default -> {
                        media.setMediaType(ResidentialPropertyMedia.MediaType.OTHER);
                        media.setOrd(-2);
                    }
                }
                mediaFilesList.add(media);
            }

            // mutate existing collection instead of replacing
            List<ResidentialPropertyMedia> existing = updated.getResidentialPropertyMediaFiles();
            if (existing == null) {
                existing = new ArrayList<>();
                updated.setResidentialPropertyMediaFiles(existing);
            } else {
                existing.clear();
            }
            for (ResidentialPropertyMedia m : mediaFilesList) {
                m.setProperty(updated);
                existing.add(m);
            }
        }

        // copy the same reset/default code you already have
        updated.setCategory("Residential");
        updated.setSold(false);
        updated.setVip(false);
        updated.setExpired(false);
        updated.setReraVerified(false);
        updated.setAdminApproved("Pending");

        repository.save(updated);

        // notification + email flows same as your existing updateProperty method
        NotificationDetails notification = new NotificationDetails();
        notification.setNotificationType(NotificationType.ListingAcknowledgement);
        String message = "Thanks! Your property titled- " + property.getTitle() + " was received after update and is pending for approval.";
        notification.setNotificationMessage(message);
        notification.setNotificationReceiverId(agentId);
        notification.setNotificationReceiverRole(Role.AGENT);
        notification.setNotificationSenderId(1);
        notification.setNotificationSenderRole(Role.ADMIN);
        notificationRepo.save(notification);

        String to = updated.getResidentialOwner().getEmail();
        String subject = "Updated- " + property.getTitle();
        String body = "Thanks! Your property titled- " + property.getTitle() + " was received after update and is pending for approval.";
        mailService.send(to, subject, body);

        NotificationDetails notificationAdmin = new NotificationDetails();
        String messageAdmin = "Property titled- " + property.getTitle() + " updated. Approve/Reject";
        notificationAdmin.setNotificationType(NotificationType.ListingApprovalRequest);
        notificationAdmin.setNotificationMessage(messageAdmin);
        notificationAdmin.setNotificationReceiverId(1);
        notificationAdmin.setNotificationReceiverRole(Role.ADMIN);
        notificationAdmin.setNotificationSenderId(agentId);
        notificationAdmin.setNotificationSenderRole(Role.AGENT);
        notificationRepo.save(notificationAdmin);

        String toAdmin = "sales@propadda.in";
        String subjectAdmin = "Listing Updated- " + property.getTitle();
        String bodyAdmin = "Property titled- " + property.getTitle() + " updated. Approve/Reject";
        mailService.send(toAdmin, subjectAdmin, bodyAdmin);

        return updated;
    }

    @Transactional
    public void deletePropertyMedia(Integer listingId, Integer agentId) {
         ResidentialPropertyDetails prop = repository.findByListingIdAndResidentialOwner_UserId(listingId, agentId)
            .orElseThrow(() -> new IllegalArgumentException("Property not found or not owned by agent"));

        for(ResidentialPropertyMedia m : prop.getResidentialPropertyMediaFiles()){
                String url = m.getUrl();
                gcsService.deleteFile(url);
            }
        prop.getResidentialPropertyMediaFiles().clear();
        repository.save(prop);
    }
}