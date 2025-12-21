// Author-Hemant Arora
package com.propadda.prop.mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.propadda.prop.dto.AllPropertyViewResponse;
import com.propadda.prop.dto.MediaResponse;
import com.propadda.prop.dto.ResidentialPropertyRequest;
import com.propadda.prop.dto.ResidentialPropertyResponse;
import com.propadda.prop.model.ResidentialPropertyAmenities;
import com.propadda.prop.model.ResidentialPropertyDetails;

public class ResidentialPropertyMapper {

    public static ResidentialPropertyResponse toDto(ResidentialPropertyDetails entity) {
        if (entity == null) {
            return null;
        }

        ResidentialPropertyResponse dto = new ResidentialPropertyResponse();

        // 1. Map fields from ResidentialPropertyDetails
        dto.setListingId(entity.getListingId());
        dto.setCategory(entity.getCategory());
        dto.setPreference(entity.getPreference());
        dto.setPropertyType(entity.getPropertyType());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setMaintenance(entity.getMaintenance());
        dto.setArea(entity.getArea());
        dto.setBedrooms(entity.getBedrooms());
        dto.setBathrooms(entity.getBathrooms());
        dto.setFurnishing(entity.getFurnishing());
        dto.setFacing(entity.getFacing());
        dto.setFloor(entity.getFloor());
        dto.setAge(entity.getAge());
        dto.setAvailability(entity.getAvailability());
        dto.setReraNumber(entity.getReraNumber());
        dto.setReraVerified(entity.getReraVerified());
        dto.setState(entity.getState());
        dto.setCity(entity.getCity());
        dto.setLocality(entity.getLocality());
        dto.setAddress(entity.getAddress());
        dto.setPincode(entity.getPincode());
        dto.setNearbyPlace(entity.getNearbyPlace());
        dto.setTotalFloors(entity.getTotalFloors());
        dto.setSecurityDeposit(entity.getSecurityDeposit());
        dto.setBalconies(entity.getBalconies());
        dto.setPowerBackup(entity.getPowerBackup());
        dto.setCoveredParking(entity.getCoveredParking());
        dto.setOpenParking(entity.getOpenParking());
        dto.setAdminApproved(entity.getAdminApproved());
        dto.setExpired(entity.isExpired());
        dto.setVip(entity.isVip());
        dto.setSold(entity.getSold());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setApprovedAt(entity.getApprovedAt());

        // 2. Map Owner (Uses the dedicated OwnerMapper)
        if (entity.getResidentialOwner() != null) {
            dto.setResidentialOwner(OwnerMapper.toDto(entity.getResidentialOwner()));
        }

        // 3. Map Media (Uses the dedicated MediaMapper)
        dto.setMediaFiles(ResidentialMediaMapper.toDtoList(entity.getResidentialPropertyMediaFiles()));
        
        // 4. Map fields from ResidentialPropertyAmenities
        ResidentialPropertyAmenities amenities = entity.getAmenities();
        if (amenities != null) {
            // Property features
            dto.setCenterCooling(amenities.isCenterCooling());
            dto.setFireAlarm(amenities.isFireAlarm());
            dto.setHeating(amenities.isHeating());
            dto.setGym(amenities.isGym());
            dto.setModularKitchen(amenities.isModularKitchen());
            dto.setPool(amenities.isPool());
            dto.setElevator(amenities.isElevator());
            dto.setPetFriendly(amenities.isPetFriendly());
            dto.setStorage(amenities.isStorage());
            dto.setLaundry(amenities.isLaundry());
            dto.setDishwasher(amenities.isDishwasher());
            dto.setDryer(amenities.isDryer());
            dto.setSauna(amenities.isSauna());
            dto.setEmergencyExit(amenities.isEmergencyExit());
            dto.setWaterPurifier(amenities.isWaterPurifier());
            dto.setGasPipeline(amenities.isGasPipeline());
            dto.setPark(amenities.isPark());
            dto.setVastuCompliant(amenities.isVastuCompliant());
            dto.setRainWaterHarvesting(amenities.isRainWaterHarvesting());
            dto.setMaintenanceStaff(amenities.isMaintenanceStaff());
            
            // Other rooms
            dto.setPoojaRoom(amenities.isPoojaRoom());
            dto.setStudyRoom(amenities.isStudyRoom());
            dto.setServantRoom(amenities.isServantRoom());
            dto.setStoreRoom(amenities.isStoreRoom());
            
            // Additional property features
            dto.setHighCeilingHeight(amenities.isHighCeilingHeight());
            dto.setFalseCeilingLighting(amenities.isFalseCeilingLighting());
            dto.setInternetConnectivity(amenities.isInternetConnectivity());
            dto.setCentrallyAirConditioned(amenities.isCentrallyAirConditioned());
            dto.setSecurityFireAlarm(amenities.isSecurityFireAlarm());
            dto.setRecentlyRenovated(amenities.isRecentlyRenovated());
            dto.setPrivateGardenTerrace(amenities.isPrivateGardenTerrace());
            dto.setNaturalLight(amenities.isNaturalLight());
            dto.setAiryRooms(amenities.isAiryRooms());
            dto.setIntercomFacility(amenities.isIntercomFacility());
            dto.setSpaciousInteriors(amenities.isSpaciousInteriors());
            
            // Society/building features
            dto.setFitnessCenter(amenities.isFitnessCenter());
            dto.setSwimmingPool(amenities.isSwimmingPool());
            dto.setClubhouseCommunityCenter(amenities.isClubhouseCommunityCenter());
            dto.setSecurityPersonnel(amenities.isSecurityPersonnel());
            dto.setLifts(amenities.isLifts()); // Note: Renamed in DTO for clarity if needed, using the entity name 'lifts'
            
            // Additional features
            dto.setSeparateEntryForServantRoom(amenities.isSeparateEntryForServantRoom());
            dto.setNoOpenDrainageAround(amenities.isNoOpenDrainageAround());
            dto.setBankAttachedProperty(amenities.isBankAttachedProperty());
            dto.setLowDensitySociety(amenities.isLowDensitySociety());
            
            // Water source
            dto.setMunicipalCorporation(amenities.isMunicipalCorporation());
            dto.setBorewellTank(amenities.isBorewellTank());
            dto.setWater24x7(amenities.isWater24x7());
            
            // Overlooking
            dto.setOverlookingPool(amenities.isOverlookingPool());
            dto.setOverlookingParkGarden(amenities.isOverlookingParkGarden());
            dto.setOverlookingClub(amenities.isOverlookingClub());
            dto.setOverlookingMainRoad(amenities.isOverlookingMainRoad());
            
            // Other features
            dto.setInGatedSociety(amenities.isInGatedSociety());
            dto.setCornerProperty(amenities.isCornerProperty());
            dto.setPetFriendlySociety(amenities.isPetFriendlySociety());
            dto.setWheelchairFriendly(amenities.isWheelchairFriendly());
            
            // Location advantages
            dto.setCloseToMetroStation(amenities.isCloseToMetroStation());
            dto.setCloseToSchool(amenities.isCloseToSchool());
            dto.setCloseToHospital(amenities.isCloseToHospital());
            dto.setCloseToMarket(amenities.isCloseToMarket());
            dto.setCloseToRailwayStation(amenities.isCloseToRailwayStation());
            dto.setCloseToAirport(amenities.isCloseToAirport());
            dto.setCloseToMall(amenities.isCloseToMall());
            dto.setCloseToHighway(amenities.isCloseToHighway());
        }

        return dto;
    }

    public static List<ResidentialPropertyResponse> toDtoList(List<ResidentialPropertyDetails> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(ResidentialPropertyMapper::toDto)
                .collect(Collectors.toList());
    }

    public static ResidentialPropertyDetails requestToModel(ResidentialPropertyDetails model, ResidentialPropertyRequest request) {
        if (model == null) {
            return null;
        }

        // 1. Map fields from ResidentialPropertyDetails
        model.setTitle(request.getTitle());
        model.setDescription(request.getDescription());
        model.setPrice(request.getPrice());
        model.setMaintenance(request.getMaintenance());
        model.setArea(request.getArea());
        model.setBedrooms(request.getBedrooms());
        model.setBathrooms(request.getBathrooms());
        model.setFurnishing(request.getFurnishing());
        model.setFacing(request.getFacing());
        model.setFloor(request.getFloor());
        model.setAge(request.getAge());
        model.setAvailability(request.getAvailability());
        model.setReraNumber(request.getReraNumber());
        model.setReraVerified(request.getReraVerified());
        model.setState(request.getState());
        model.setCity(request.getCity());
        model.setLocality(request.getLocality());
        model.setAddress(request.getAddress());
        model.setPincode(request.getPincode());
        model.setNearbyPlace(request.getNearbyPlace());
        model.setTotalFloors(request.getTotalFloors());
        model.setSecurityDeposit(request.getSecurityDeposit());
        model.setBalconies(request.getBalconies());
        model.setPowerBackup(request.getPowerBackup());
        model.setCoveredParking(request.getCoveredParking());
        model.setOpenParking(request.getOpenParking());

        // 4. Map fields from ResidentialPropertyAmenities
        ResidentialPropertyAmenities amenities = new ResidentialPropertyAmenities();
        
            // Property features
            amenities.setCenterCooling(request.isCenterCooling());
            amenities.setFireAlarm(request.isFireAlarm());
            amenities.setHeating(request.isHeating());
            amenities.setGym(request.isGym());
            amenities.setModularKitchen(request.isModularKitchen());
            amenities.setPool(request.isPool());
            amenities.setElevator(request.isElevator());
            amenities.setPetFriendly(request.isPetFriendly());
            amenities.setStorage(request.isStorage());
            amenities.setLaundry(request.isLaundry());
            amenities.setDishwasher(request.isDishwasher());
            amenities.setDryer(request.isDryer());
            amenities.setSauna(request.isSauna());
            amenities.setEmergencyExit(request.isEmergencyExit());
            amenities.setWaterPurifier(request.isWaterPurifier());
            amenities.setGasPipeline(request.isGasPipeline());
            amenities.setPark(request.isPark());
            amenities.setVastuCompliant(request.isVastuCompliant());
            amenities.setRainWaterHarvesting(request.isRainWaterHarvesting());
            amenities.setMaintenanceStaff(request.isMaintenanceStaff());
            
            // Other rooms
            amenities.setPoojaRoom(request.isPoojaRoom());
            amenities.setStudyRoom(request.isStudyRoom());
            amenities.setServantRoom(request.isServantRoom());
            amenities.setStoreRoom(request.isStoreRoom());
            
            // Additional property features
            amenities.setHighCeilingHeight(request.isHighCeilingHeight());
            amenities.setFalseCeilingLighting(request.isFalseCeilingLighting());
            amenities.setInternetConnectivity(request.isInternetConnectivity());
            amenities.setCentrallyAirConditioned(request.isCentrallyAirConditioned());
            amenities.setSecurityFireAlarm(request.isSecurityFireAlarm());
            amenities.setRecentlyRenovated(request.isRecentlyRenovated());
            amenities.setPrivateGardenTerrace(request.isPrivateGardenTerrace());
            amenities.setNaturalLight(request.isNaturalLight());
            amenities.setAiryRooms(request.isAiryRooms());
            amenities.setIntercomFacility(request.isIntercomFacility());
            amenities.setSpaciousInteriors(request.isSpaciousInteriors());
            
            // Society/building features
            amenities.setFitnessCenter(request.isFitnessCenter());
            amenities.setSwimmingPool(request.isSwimmingPool());
            amenities.setClubhouseCommunityCenter(request.isClubhouseCommunityCenter());
            amenities.setSecurityPersonnel(request.isSecurityPersonnel());
            amenities.setLifts(request.isLifts()); // Note: Renamed in amenities for clarity if needed, using the entity name 'lifts'
            
            // Additional features
            amenities.setSeparateEntryForServantRoom(request.isSeparateEntryForServantRoom());
            amenities.setNoOpenDrainageAround(request.isNoOpenDrainageAround());
            amenities.setBankAttachedProperty(request.isBankAttachedProperty());
            amenities.setLowDensitySociety(request.isLowDensitySociety());
            
            // Water source
            amenities.setMunicipalCorporation(request.isMunicipalCorporation());
            amenities.setBorewellTank(request.isBorewellTank());
            amenities.setWater24x7(request.isWater24x7());
            
            // Overlooking
            amenities.setOverlookingPool(request.isOverlookingPool());
            amenities.setOverlookingParkGarden(request.isOverlookingParkGarden());
            amenities.setOverlookingClub(request.isOverlookingClub());
            amenities.setOverlookingMainRoad(request.isOverlookingMainRoad());
            
            // Other features
            amenities.setInGatedSociety(request.isInGatedSociety());
            amenities.setCornerProperty(request.isCornerProperty());
            amenities.setPetFriendlySociety(request.isPetFriendlySociety());
            amenities.setWheelchairFriendly(request.isWheelchairFriendly());
            
            // Location advantages
            amenities.setCloseToMetroStation(request.isCloseToMetroStation());
            amenities.setCloseToSchool(request.isCloseToSchool());
            amenities.setCloseToHospital(request.isCloseToHospital());
            amenities.setCloseToMarket(request.isCloseToMarket());
            amenities.setCloseToRailwayStation(request.isCloseToRailwayStation());
            amenities.setCloseToAirport(request.isCloseToAirport());
            amenities.setCloseToMall(request.isCloseToMall());
            amenities.setCloseToHighway(request.isCloseToHighway());
        
        model.setAmenities(amenities);
        return model;
    }

    public static AllPropertyViewResponse toAllViewDto(ResidentialPropertyResponse src) {
        if (src == null) {
            return null;
        }

        AllPropertyViewResponse dto = new AllPropertyViewResponse();

        dto.setListingId(src.getListingId());
        dto.setCategory(src.getCategory());
        dto.setPreference(src.getPreference());
        dto.setPropertyType(src.getPropertyType());
        dto.setTitle(src.getTitle());
        dto.setDescription(src.getDescription());
        dto.setPrice(src.getPrice());
        dto.setArea(src.getArea());

        // Residential-specific
        dto.setBedrooms(src.getBedrooms());
        dto.setBathrooms(src.getBathrooms());

        // Location
        dto.setState(src.getState());
        dto.setCity(src.getCity());
        dto.setLocality(src.getLocality());
        dto.setAddress(src.getAddress());

        // Status
        dto.setExpired(src.isExpired());
        dto.setVip(src.isVip());
        dto.setSold(src.getSold());
        dto.setReraVerified(src.getReraVerified());
        dto.setReraNumber(src.getReraNumber());

        dto.setCreatedAt(src.getCreatedAt());
        dto.setApprovedAt(src.getApprovedAt());

        // Owner flattening
        if (src.getResidentialOwner() != null) {
            dto.setFirstName(src.getResidentialOwner().getFirstName());
            dto.setLastName(src.getResidentialOwner().getLastName());
            dto.setEmail(src.getResidentialOwner().getEmail());
            dto.setPhoneNumber(src.getResidentialOwner().getPhoneNumber());
        }

        // Media (pick first image)
        if (src.getMediaFiles() != null && !src.getMediaFiles().isEmpty()) {
            for(MediaResponse m : src.getMediaFiles()){
                if(m.getOrd()==1){
                    dto.setMediaUrl(m.getUrl());
                }
            }
            // dto.setMediaUrl(src.getMediaFiles().get(0).getUrl());
        }

        return dto;
    }
}