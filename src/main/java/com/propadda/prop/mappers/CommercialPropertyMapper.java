package com.propadda.prop.mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.propadda.prop.dto.CommercialPropertyRequest;
import com.propadda.prop.dto.CommercialPropertyResponse;
import com.propadda.prop.model.CommercialPropertyDetails;

public class CommercialPropertyMapper {

    // Maps a single CommercialPropertyDetails entity to a CommercialPropertyResponse DTO
    public static CommercialPropertyResponse toDto(CommercialPropertyDetails entity) {
        if (entity == null) {
            return null;
        }

        CommercialPropertyResponse dto = new CommercialPropertyResponse();
        
        // 1. Basic Field Mapping (Direct Copy)
        dto.setListingId(entity.getListingId());
        dto.setCategory(entity.getCategory());
        dto.setPreference(entity.getPreference());
        dto.setPropertyType(entity.getPropertyType());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setArea(entity.getArea());
        dto.setReraNumber(entity.getReraNumber());
        dto.setReraVerified(entity.getReraVerified());
        
        // Amenities
        dto.setCabins(entity.getCabins());
        dto.setMeetingRoom(entity.getMeetingRoom());
        dto.setWashroom(entity.getWashroom());
        dto.setConferenceRoom(entity.getConferenceRoom());
        dto.setReceptionArea(entity.getReceptionArea());
        dto.setLift(entity.getLift());
        dto.setParking(entity.getParking());
        
        // Rental Terms & Structure
        dto.setAge(entity.getAge());
        dto.setAvailability(entity.getAvailability());
        dto.setSecurityDeposit(entity.getSecurityDeposit());
        dto.setLockIn(entity.getLockIn());
        dto.setYearlyIncrease(entity.getYearlyIncrease());
        dto.setFloor(entity.getFloor());
        dto.setTotalFloors(entity.getTotalFloors());
        
        // Location & Status
        dto.setAddress(entity.getAddress());
        dto.setCity(entity.getCity());
        dto.setState(entity.getState());
        dto.setPincode(entity.getPincode());
        dto.setLocality(entity.getLocality());
        dto.setNearbyPlace(entity.getNearbyPlace());
        dto.setAdminApproved(entity.getAdminApproved());
        dto.setExpired(entity.isExpired());
        dto.setVip(entity.isVip());
        dto.setSold(entity.getSold());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setApprovedAt(entity.getApprovedAt());

        // 2. Nested Object Mapping (Requires separate mapper for OwnerResponse)
        // You MUST ensure OwnerMapper.toDto() exists and handles the Users entity.
        if (entity.getCommercialOwner() != null) {
            dto.setCommercialOwner(OwnerMapper.toDto(entity.getCommercialOwner()));
        }

        // 3. Nested List Mapping (Uses MediaMapper)
        dto.setMediaFiles(CommercialMediaMapper.toDtoList(entity.getCommercialPropertyMediaFiles()));
        
        return dto;
    }

    // Maps a list of CommercialPropertyDetails entities to a list of DTOs
    public static List<CommercialPropertyResponse> toDtoList(List<CommercialPropertyDetails> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(CommercialPropertyMapper::toDto)
                .collect(Collectors.toList());
    }

    public static CommercialPropertyDetails requestToModel(CommercialPropertyDetails model, CommercialPropertyRequest request) {
        if (request == null) {
            return null;
        }
        
        // 1. Basic Field Mapping (Direct Copy)
        model.setTitle(request.getTitle());
        model.setDescription(request.getDescription());
        model.setPrice(request.getPrice());
        model.setArea(request.getArea());
        model.setReraNumber(request.getReraNumber());
        model.setReraVerified(request.getReraVerified());
        
        // Amenities
        model.setCabins(request.getCabins());
        model.setMeetingRoom(request.getMeetingRoom());
        model.setWashroom(request.getWashroom());
        model.setConferenceRoom(request.getConferenceRoom());
        model.setReceptionArea(request.getReceptionArea());
        model.setLift(request.getLift());
        model.setParking(request.getParking());
        
        // Rental Terms & Structure
        model.setAge(request.getAge());
        model.setAvailability(request.getAvailability());
        model.setSecurityDeposit(request.getSecurityDeposit());
        model.setLockIn(request.getLockIn());
        model.setYearlyIncrease(request.getYearlyIncrease());
        model.setFloor(request.getFloor());
        model.setTotalFloors(request.getTotalFloors());
        
        // Location & Status
        model.setAddress(request.getAddress());
        model.setCity(request.getCity());
        model.setState(request.getState());
        model.setPincode(request.getPincode());
        model.setLocality(request.getLocality());
        model.setNearbyPlace(request.getNearbyPlace());
        
        return model;
    }
}
