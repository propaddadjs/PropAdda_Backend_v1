package com.propadda.prop.service;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.propadda.prop.dto.CommercialPropertyRequest;
import com.propadda.prop.enumerations.Role;
import com.propadda.prop.mappers.CommercialPropertyMapper;
import com.propadda.prop.model.CommercialPropertyDetails;
import com.propadda.prop.model.CommercialPropertyMedia;
import com.propadda.prop.model.Users;
import com.propadda.prop.repo.CommercialPropertyDetailsRepo;
import com.propadda.prop.repo.EnquiredListingsDetailsRepo;
import com.propadda.prop.repo.FavoriteListingsDetailsRepo;
import com.propadda.prop.repo.UsersRepo;

import jakarta.transaction.Transactional;

@Service
public class CommercialPropertyDetailsService {

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private FavoriteListingsDetailsRepo favRepo;

    @Autowired
    private EnquiredListingsDetailsRepo enqRepo;

    private final CommercialPropertyDetailsRepo repository;
    private final GcsService gcsService;

    public CommercialPropertyDetailsService(CommercialPropertyDetailsRepo repository, GcsService gcsService) {
        this.repository = repository;
        this.gcsService = gcsService;
    }

    @Transactional
    public CommercialPropertyDetails saveProperty(CommercialPropertyRequest property, List<MultipartFile> files) throws IOException {
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
        
        Integer order = 1;
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
                media.setOrd(0);
            } else 
            if(contentType != null && contentType.startsWith("image/")) {
                media.setMediaType(CommercialPropertyMedia.MediaType.IMAGE);
                media.setOrd(order);
                order++;
            } else 
            if(contentType != null && (contentType.startsWith("application/") || contentType.startsWith("text/"))){
                media.setMediaType(CommercialPropertyMedia.MediaType.BROCHURE);
                media.setOrd(-1);
            } else {
                media.setMediaType(CommercialPropertyMedia.MediaType.OTHER);
                media.setOrd(-2);
            }
        mediaFilesList.add(media);
        }
        propDetails.setCategory("Commercial");
        propDetails.setCommercialPropertyMediaFiles(mediaFilesList);
        propDetails.setSold(false);
        propDetails.setVip(false);
        propDetails.setExpired(false);
        propDetails.setReraVerified(false);
        propDetails.setAdminApproved("Pending");

        return repository.save(propDetails);
    }

    // public List<CommercialPropertyResponse> getAllProperties() {
    //     List<CommercialPropertyDetails> cpd = repository.findAll();
    //     List<CommercialPropertyResponse> responseList = new ArrayList<>();

    //     for(CommercialPropertyDetails prop : cpd){
    //         List<CommercialPropertyMedia> media = prop.getCommercialPropertyMediaFiles();
    //         List<MediaResponse> mediaFiles = new ArrayList<>();

    //         for(CommercialPropertyMedia m : media){
    //             MediaResponse mediaFile = new MediaResponse(m.getFilename(), m.getOrd(), m.getUrl());
    //             mediaFiles.add(mediaFile);
    //         }

    //         OwnerResponse owner = new OwnerResponse(prop.getCommercialOwner().getEmail(), prop.getCommercialOwner().getFirstName(), prop.getCommercialOwner().getLastName(), prop.getCommercialOwner().getPhoneNumber(), prop.getCommercialOwner().getUserId());

    //         CommercialPropertyResponse res = new CommercialPropertyResponse(prop.getAddress(), prop.getAge(), prop.getAdminApproved(), prop.getArea(), prop.getAvailability(), prop.getCabins(), prop.getCity(), owner, prop.getConferenceRoom(), prop.getDescription(), prop.isExpired(), prop.getFloor(), prop.getLift(), prop.getListingId(), prop.getLocality(), prop.getLockIn(), mediaFiles, prop.getMeetingRoom(), prop.getNearbyPlace(), prop.getParking(), prop.getPincode(), prop.getPreference(), prop.getPrice(), prop.getPropertyType(), prop.getReceptionArea(), prop.getReraNumber(), prop.getReraVerified(), prop.getSecurityDeposit(), prop.getSold(), prop.getState(), prop.getTitle(), prop.getTotalFloors(), prop.isVip(), prop.getWashroom(), prop.getYearlyIncrease());
    //         res.setCategory(prop.getCategory());
    //         responseList.add(res);
    //     }
    //     return responseList;
    // }

    // public CommercialPropertyResponse getPropertyById(Integer id) {
    //     Optional<CommercialPropertyDetails> cpd = repository.findById(id);

    //     if(cpd.isPresent()){
    //         CommercialPropertyDetails prop = cpd.get();
    //         List<CommercialPropertyMedia> media = prop.getCommercialPropertyMediaFiles();
    //         List<MediaResponse> mediaFiles = new ArrayList<>();

    //         for(CommercialPropertyMedia m : media){
    //             MediaResponse mediaFile = new MediaResponse(m.getFilename(), m.getOrd(), m.getUrl());
    //             mediaFiles.add(mediaFile);
    //         }
    //         OwnerResponse owner = new OwnerResponse(prop.getCommercialOwner().getEmail(), prop.getCommercialOwner().getFirstName(), prop.getCommercialOwner().getLastName(), prop.getCommercialOwner().getPhoneNumber(), prop.getCommercialOwner().getUserId());

    //         CommercialPropertyResponse res = new CommercialPropertyResponse(prop.getAddress(), prop.getAge(), prop.getAdminApproved(), prop.getArea(), prop.getAvailability(), prop.getCabins(), prop.getCity(), owner, prop.getConferenceRoom(), prop.getDescription(), prop.isExpired(), prop.getFloor(), prop.getLift(), prop.getListingId(), prop.getLocality(), prop.getLockIn(), mediaFiles, prop.getMeetingRoom(), prop.getNearbyPlace(), prop.getParking(), prop.getPincode(), prop.getPreference(), prop.getPrice(), prop.getPropertyType(), prop.getReceptionArea(), prop.getReraNumber(), prop.getReraVerified(), prop.getSecurityDeposit(), prop.getSold(), prop.getState(), prop.getTitle(), prop.getTotalFloors(), prop.isVip(), prop.getWashroom(), prop.getYearlyIncrease());
    //         res.setCategory(prop.getCategory());
    //         return res;
    //     }
    //     else{
    //         return null;
    //     }
    // }

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
    public Object updateProperty(CommercialPropertyRequest property, List<MultipartFile> files, Integer agentId) throws IOException {
        CommercialPropertyDetails propModel = repository.findByListingIdAndCommercialOwner_UserId(property.getListingId(), agentId)
            .orElseThrow(() -> new IllegalArgumentException("Property not found or not owned by agent"));
        
        CommercialPropertyDetails updated = CommercialPropertyMapper.requestToModel(propModel, property);

        if (files != null && !files.isEmpty()) {
            Integer order = 1;
            List<CommercialPropertyMedia> mediaFilesList = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = gcsService.uploadFile(file,"commercial");
            CommercialPropertyMedia media = new CommercialPropertyMedia();
            media.setUrl(url);
            media.setFilename(file.getOriginalFilename());
            media.setSize(file.getSize());
            media.setUploadedAt(Instant.now());
            media.setProperty(updated);
            String contentType = file.getContentType();
            if (contentType != null && contentType.startsWith("video/")) {
                media.setMediaType(CommercialPropertyMedia.MediaType.VIDEO);
                media.setOrd(0);
            } else 
            if(contentType != null && contentType.startsWith("image/")) {
                media.setMediaType(CommercialPropertyMedia.MediaType.IMAGE);
                media.setOrd(order);
                order++;
            } else 
            if(contentType != null && (contentType.startsWith("application/") || contentType.startsWith("text/"))){
                media.setMediaType(CommercialPropertyMedia.MediaType.BROCHURE);
                media.setOrd(-1);
            } else {
                media.setMediaType(CommercialPropertyMedia.MediaType.OTHER);
                media.setOrd(-2);
            }
        mediaFilesList.add(media);
        }
        updated.setCommercialPropertyMediaFiles(mediaFilesList);
        }
        updated.setCategory("Commercial");
        updated.setSold(false);
        updated.setVip(false);
        updated.setExpired(false);
        updated.setReraVerified(false);
        updated.setAdminApproved("Pending");

        return repository.save(updated);

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
