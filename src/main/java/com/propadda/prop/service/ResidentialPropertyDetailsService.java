package com.propadda.prop.service;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.propadda.prop.dto.ResidentialPropertyRequest;
import com.propadda.prop.enumerations.Role;
import com.propadda.prop.mappers.ResidentialPropertyMapper;
import com.propadda.prop.model.ResidentialPropertyAmenities;
import com.propadda.prop.model.ResidentialPropertyDetails;
import com.propadda.prop.model.ResidentialPropertyMedia;
import com.propadda.prop.model.Users;
import com.propadda.prop.repo.EnquiredListingsDetailsRepo;
import com.propadda.prop.repo.FavoriteListingsDetailsRepo;
import com.propadda.prop.repo.ResidentialPropertyDetailsRepo;
import com.propadda.prop.repo.UsersRepo;

import jakarta.transaction.Transactional;

@Service
public class ResidentialPropertyDetailsService {

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private FavoriteListingsDetailsRepo favRepo;

    @Autowired
    private EnquiredListingsDetailsRepo enqRepo;

    private final ResidentialPropertyDetailsRepo repository;
    private final GcsService gcsService;

    public ResidentialPropertyDetailsService(ResidentialPropertyDetailsRepo repository, GcsService gcsService) {
        this.repository = repository;
        this.gcsService = gcsService;
    }

    // Create or Update property
    @Transactional
    public ResidentialPropertyDetails saveProperty(ResidentialPropertyRequest property, List<MultipartFile> files) throws IOException {
    	// Mandatory fields validation
        if (property.getState() == null || property.getCity() == null || property.getLocality() == null) {
            throw new IllegalArgumentException("State, city, and locality must not be null");
        }
        ResidentialPropertyDetails propDetails = new ResidentialPropertyDetails(property.getAddress(), property.getAge(), property.getAdminApproved(), property.getArea(), property.getAvailability(), property.getBalconies(), property.getBathrooms(), property.getBedrooms(), property.getCity(), property.getCoveredParking(), property.getDescription(), property.isExpired(), property.getFacing(), property.getFloor(), property.getFurnishing(), property.getLocality(), property.getMaintenance(), property.getNearbyPlace(), property.getOpenParking(), property.getPincode(), property.getPowerBackup(), property.getPreference(), property.getPrice(), property.getPropertyType(), property.getReraNumber(), property.getReraVerified(), property.getSecurityDeposit(), property.getState(), property.getTitle(), property.getTotalFloors(), property.isVip());
        
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
        
        Integer order = 1;
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
                media.setOrd(0);
            } else 
            if(contentType != null && contentType.startsWith("image/")) {
                media.setMediaType(ResidentialPropertyMedia.MediaType.IMAGE);
                media.setOrd(order);
                order++;
            } else 
            if(contentType != null && (contentType.startsWith("application/") || contentType.startsWith("text/"))){
                media.setMediaType(ResidentialPropertyMedia.MediaType.BROCHURE);
                media.setOrd(-1);
            } else {
                media.setMediaType(ResidentialPropertyMedia.MediaType.OTHER);
                media.setOrd(-2);
            }
        mediaFilesList.add(media);
        }
        propDetails.setCategory("Residential");
        propDetails.setResidentialPropertyMediaFiles(mediaFilesList);
        propDetails.setSold(false);
        propDetails.setVip(false);
        propDetails.setExpired(false);
        propDetails.setReraVerified(false);
        propDetails.setAdminApproved("Pending");
        return repository.save(propDetails);
    }

    // Get all properties
    // public List<ResidentialPropertyResponse> getAllProperties() {
    //     List<ResidentialPropertyDetails> rpd = repository.findAll();
    //     List<ResidentialPropertyResponse> responseList = new ArrayList<>();
        
    //     for(ResidentialPropertyDetails prop : rpd){
    //         ResidentialPropertyAmenities amenities = prop.getAmenities();
    //         List<ResidentialPropertyMedia> media = prop.getResidentialPropertyMediaFiles();
    //         List<MediaResponse> mediaFiles = new ArrayList<>();

    //         for(ResidentialPropertyMedia m : media){
    //             MediaResponse mediaFile = new MediaResponse(m.getFilename(), m.getOrd(), m.getUrl());
    //             mediaFiles.add(mediaFile);
    //         }

    //         OwnerResponse owner = new OwnerResponse(prop.getResidentialOwner().getEmail(), prop.getResidentialOwner().getFirstName(), prop.getResidentialOwner().getLastName(), prop.getResidentialOwner().getPhoneNumber(), prop.getResidentialOwner().getUserId());

    //         ResidentialPropertyResponse res = new ResidentialPropertyResponse(prop.getAddress(), 
    //         prop.getAge(), prop.getAdminApproved(), prop.getAmenities().isAiryRooms(), prop.getArea(), prop.getAvailability(), 
    //         prop.getBalconies(), prop.getAmenities().isBankAttachedProperty(), prop.getBathrooms(), 
    //         prop.getBedrooms(), prop.getAmenities().isBorewellTank(), prop.getAmenities().isCenterCooling(), 
    //         prop.getAmenities().isCentrallyAirConditioned(), prop.getCity(), prop.getAmenities().isCloseToAirport(), 
    //         prop.getAmenities().isCloseToHighway(), prop.getAmenities().isCloseToHospital(), prop.getAmenities().isCloseToMall(), 
    //         prop.getAmenities().isCloseToMarket(), prop.getAmenities().isCloseToMetroStation(), 
    //         prop.getAmenities().isCloseToRailwayStation(), prop.getAmenities().isCloseToSchool(), 
    //         prop.getAmenities().isClubhouseCommunityCenter(), prop.getAmenities().isCornerProperty(), 
    //         prop.getCoveredParking(), prop.getDescription(), prop.getAmenities().isDishwasher(), 
    //         prop.getAmenities().isDryer(), prop.getAmenities().isElevator(), prop.getAmenities().isEmergencyExit(), prop.isExpired(), 
    //         prop.getFacing(), prop.getAmenities().isFalseCeilingLighting(), prop.getAmenities().isFireAlarm(), 
    //         prop.getAmenities().isFitnessCenter(), prop.getFloor(), prop.getFurnishing(), 
    //         prop.getAmenities().isGasPipeline(), prop.getAmenities().isGym(), prop.getAmenities().isHeating(), 
    //         prop.getAmenities().isHighCeilingHeight(), prop.getAmenities().isInGatedSociety(), 
    //         prop.getAmenities().isIntercomFacility(), prop.getAmenities().isInternetConnectivity(), 
    //         prop.getAmenities().isLaundry(), prop.getAmenities().isLifts(), prop.getListingId(), prop.getLocality(), 
    //         prop.getAmenities().isLowDensitySociety(), prop.getMaintenance(), prop.getAmenities().isMaintenanceStaff(), 
    //         mediaFiles, prop.getAmenities().isModularKitchen(), prop.getAmenities().isMunicipalCorporation(), 
    //         prop.getAmenities().isNaturalLight(), prop.getNearbyPlace(), prop.getAmenities().isNoOpenDrainageAround(), 
    //         prop.getOpenParking(), prop.getAmenities().isOverlookingClub(), prop.getAmenities().isOverlookingMainRoad(), 
    //         prop.getAmenities().isOverlookingParkGarden(), prop.getAmenities().isOverlookingPool(), prop.getAmenities().isPark(), 
    //         prop.getAmenities().isPetFriendly(), prop.getAmenities().isPetFriendlySociety(), prop.getPincode(), 
    //         prop.getAmenities().isPoojaRoom(), prop.getAmenities().isPool(), prop.getPowerBackup(), prop.getPreference(), 
    //         prop.getPrice(), prop.getAmenities().isPrivateGardenTerrace(), prop.getPropertyType(), 
    //         prop.getAmenities().isRainWaterHarvesting(), prop.getAmenities().isRecentlyRenovated(), prop.getReraNumber(), 
    //         prop.getReraVerified(), owner, prop.getAmenities().isSauna(), prop.getSecurityDeposit(), 
    //         prop.getAmenities().isSecurityFireAlarm(), prop.getAmenities().isSecurityPersonnel(), 
    //         prop.getAmenities().isSeparateEntryForServantRoom(), prop.getAmenities().isServantRoom(), prop.getSold(), 
    //         prop.getAmenities().isSpaciousInteriors(), prop.getState(), prop.getAmenities().isStorage(), 
    //         prop.getAmenities().isStoreRoom(), prop.getAmenities().isStudyRoom(), prop.getAmenities().isSwimmingPool(), 
    //         prop.getTitle(), prop.getTotalFloors(), prop.getAmenities().isVastuCompliant(), prop.isVip(),
    //         prop.getAmenities().isWater24x7(), prop.getAmenities().isWaterPurifier(), amenities.isWheelchairFriendly());
            
    //         res.setCategory(prop.getCategory());
    //         responseList.add(res);
    //     }
    //     return responseList;

    //     // return repository.findAll()
    //     // .stream()
    //     // .map(ResidentialPropertyResponse::fromEntity)
    //     // .toList();
    // }

    // // Get property by ID
    // public ResidentialPropertyResponse getPropertyById(Integer id) {
    //     Optional<ResidentialPropertyDetails> rpd = repository.findById(id);
    //     if(rpd.isPresent()){
    //         ResidentialPropertyDetails prop = rpd.get();
    //         ResidentialPropertyAmenities amenities = prop.getAmenities();
    //         List<ResidentialPropertyMedia> media = prop.getResidentialPropertyMediaFiles();
    //         List<MediaResponse> mediaFiles = new ArrayList<>();

    //         for(ResidentialPropertyMedia m : media){
    //             MediaResponse mediaFile = new MediaResponse(m.getFilename(), m.getOrd(), m.getUrl());
    //             mediaFiles.add(mediaFile);
    //         }

    //         OwnerResponse owner = new OwnerResponse(prop.getResidentialOwner().getEmail(), prop.getResidentialOwner().getFirstName(), prop.getResidentialOwner().getLastName(), prop.getResidentialOwner().getPhoneNumber(), prop.getResidentialOwner().getUserId());
            
    //         ResidentialPropertyResponse res = new ResidentialPropertyResponse(prop.getAddress(), 
    //         prop.getAge(), prop.getAdminApproved(), prop.getAmenities().isAiryRooms(), prop.getArea(), prop.getAvailability(), 
    //         prop.getBalconies(), prop.getAmenities().isBankAttachedProperty(), prop.getBathrooms(), 
    //         prop.getBedrooms(), prop.getAmenities().isBorewellTank(), prop.getAmenities().isCenterCooling(), 
    //         prop.getAmenities().isCentrallyAirConditioned(), prop.getCity(), prop.getAmenities().isCloseToAirport(), 
    //         prop.getAmenities().isCloseToHighway(), prop.getAmenities().isCloseToHospital(), prop.getAmenities().isCloseToMall(), 
    //         prop.getAmenities().isCloseToMarket(), prop.getAmenities().isCloseToMetroStation(), 
    //         prop.getAmenities().isCloseToRailwayStation(), prop.getAmenities().isCloseToSchool(), 
    //         prop.getAmenities().isClubhouseCommunityCenter(), prop.getAmenities().isCornerProperty(), 
    //         prop.getCoveredParking(), prop.getDescription(), prop.getAmenities().isDishwasher(), 
    //         prop.getAmenities().isDryer(), prop.getAmenities().isElevator(), prop.getAmenities().isEmergencyExit(), prop.isExpired(),
    //         prop.getFacing(), prop.getAmenities().isFalseCeilingLighting(), prop.getAmenities().isFireAlarm(), 
    //         prop.getAmenities().isFitnessCenter(), prop.getFloor(), prop.getFurnishing(), 
    //         prop.getAmenities().isGasPipeline(), prop.getAmenities().isGym(), prop.getAmenities().isHeating(), 
    //         prop.getAmenities().isHighCeilingHeight(), prop.getAmenities().isInGatedSociety(), 
    //         prop.getAmenities().isIntercomFacility(), prop.getAmenities().isInternetConnectivity(), 
    //         prop.getAmenities().isLaundry(), prop.getAmenities().isLifts(), prop.getListingId(), prop.getLocality(), 
    //         prop.getAmenities().isLowDensitySociety(), prop.getMaintenance(), prop.getAmenities().isMaintenanceStaff(), 
    //         mediaFiles, prop.getAmenities().isModularKitchen(), prop.getAmenities().isMunicipalCorporation(), 
    //         prop.getAmenities().isNaturalLight(), prop.getNearbyPlace(), prop.getAmenities().isNoOpenDrainageAround(), 
    //         prop.getOpenParking(), prop.getAmenities().isOverlookingClub(), prop.getAmenities().isOverlookingMainRoad(), 
    //         prop.getAmenities().isOverlookingParkGarden(), prop.getAmenities().isOverlookingPool(), prop.getAmenities().isPark(), 
    //         prop.getAmenities().isPetFriendly(), prop.getAmenities().isPetFriendlySociety(), prop.getPincode(), 
    //         prop.getAmenities().isPoojaRoom(), prop.getAmenities().isPool(), prop.getPowerBackup(), prop.getPreference(), 
    //         prop.getPrice(), prop.getAmenities().isPrivateGardenTerrace(), prop.getPropertyType(), 
    //         prop.getAmenities().isRainWaterHarvesting(), prop.getAmenities().isRecentlyRenovated(), prop.getReraNumber(), 
    //         prop.getReraVerified(), owner, prop.getAmenities().isSauna(), prop.getSecurityDeposit(), 
    //         prop.getAmenities().isSecurityFireAlarm(), prop.getAmenities().isSecurityPersonnel(), 
    //         prop.getAmenities().isSeparateEntryForServantRoom(), prop.getAmenities().isServantRoom(), prop.getSold(), 
    //         prop.getAmenities().isSpaciousInteriors(), prop.getState(), prop.getAmenities().isStorage(), 
    //         prop.getAmenities().isStoreRoom(), prop.getAmenities().isStudyRoom(), prop.getAmenities().isSwimmingPool(), 
    //         prop.getTitle(), prop.getTotalFloors(), prop.getAmenities().isVastuCompliant(), prop.isVip(),
    //         prop.getAmenities().isWater24x7(), prop.getAmenities().isWaterPurifier(), amenities.isWheelchairFriendly());
            
    //         res.setCategory(prop.getCategory());
    //         return res;
    //     }
    //     else{
    //         return null;
    //     }
    // }

    
    // Delete property
    @Transactional
    public void deleteProperty(Integer listingId, Integer agentId) {
        ResidentialPropertyDetails rpd = repository.findByListingIdAndResidentialOwner_UserId(listingId,agentId).orElseThrow(() -> new IllegalArgumentException("Property not found or not owned by agent"));;
        
            for(ResidentialPropertyMedia m : rpd.getResidentialPropertyMediaFiles()){
                String url = m.getUrl();
                gcsService.deleteFile(url);
            }
            favRepo.deleteByPropertyIdAndPropertyType(listingId,"Commercial");
            enqRepo.deleteByPropertyIdAndPropertyType(listingId, "Commercial");
            repository.deleteById(listingId);
        
    }

    @Transactional
    public Object updateProperty(ResidentialPropertyRequest property, List<MultipartFile> files, Integer agentId) throws IOException {
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
            media.setProperty(updated);
            String contentType = file.getContentType();
            if (contentType != null && contentType.startsWith("video/")) {
                media.setMediaType(ResidentialPropertyMedia.MediaType.VIDEO);
                media.setOrd(0);
            } else 
            if(contentType != null && contentType.startsWith("image/")) {
                media.setMediaType(ResidentialPropertyMedia.MediaType.IMAGE);
                media.setOrd(order);
                order++;
            } else 
            if(contentType != null && (contentType.startsWith("application/") || contentType.startsWith("text/"))){
                media.setMediaType(ResidentialPropertyMedia.MediaType.BROCHURE);
                media.setOrd(-1);
            } else {
                media.setMediaType(ResidentialPropertyMedia.MediaType.OTHER);
                media.setOrd(-2);
            }
        mediaFilesList.add(media);
        }
        updated.setResidentialPropertyMediaFiles(mediaFilesList);
        }
        updated.setCategory("Residential");
        updated.setSold(false);
        updated.setVip(false);
        updated.setExpired(false);
        updated.setReraVerified(false);
        updated.setAdminApproved("Pending");

        return repository.save(updated);
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