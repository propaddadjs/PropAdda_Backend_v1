package com.propadda.prop.dto;

import java.time.OffsetDateTime;
import java.util.List;

public class ResidentialPropertyResponse {
  
    private Integer listingId;
    private String category;
    private String preference;
    private String propertyType;
    private String title;
    private String description;
    private Integer price;
    private Integer maintenance;
    private Double area;
    private Integer bedrooms;
    private Integer bathrooms;
    private String furnishing;
    private String facing;
    private Integer floor;
    private String age;
    private String availability;
    private String reraNumber;
    private Boolean reraVerified;
    private String state;
    private String city;
    private String locality;
    private String address;
    private Integer pincode;
    private String nearbyPlace;
    private Integer totalFloors;
    private Integer securityDeposit;
    private Integer balconies;
    private String powerBackup;
    private Integer coveredParking ;
    private Integer openParking ;
    private String adminApproved;
    private Boolean expired;
    private OwnerResponse residentialOwner;
    private Boolean vip;
    private Boolean sold;
    private OffsetDateTime createdAt;
    private OffsetDateTime approvedAt;

    //amenities
    //Property features
    private Boolean centerCooling;
    private Boolean fireAlarm;
    private Boolean heating;
    private Boolean gym;
    private Boolean modularKitchen;
    private Boolean pool;
    private Boolean elevator;
    private Boolean petFriendly;
    private Boolean storage;
    private Boolean laundry;
    private Boolean dishwasher;
    private Boolean dryer;
    private Boolean sauna;
    private Boolean emergencyExit;
    private Boolean waterPurifier;
    private Boolean gasPipeline;
    private Boolean park;
    private Boolean vastuCompliant;
    private Boolean rainWaterHarvesting;
    private Boolean maintenanceStaff;
    //Other rooms
    private Boolean poojaRoom;
    private Boolean studyRoom;
    private Boolean servantRoom;
    private Boolean storeRoom;
    //property features
    private Boolean highCeilingHeight;
    private Boolean falseCeilingLighting;
    private Boolean internetConnectivity;
    private Boolean centrallyAirConditioned;
    private Boolean securityFireAlarm;
    private Boolean recentlyRenovated;
    private Boolean privateGardenTerrace;
    private Boolean naturalLight;
    private Boolean airyRooms;
    private Boolean intercomFacility;
    private Boolean spaciousInteriors;
    //society or building features
    private Boolean fitnessCenter;
    private Boolean swimmingPool;
    private Boolean clubhouseCommunityCenter;
    private Boolean securityPersonnel;
    private Boolean lifts;
    //Additional features
    private Boolean separateEntryForServantRoom;
    private Boolean noOpenDrainageAround;
    private Boolean bankAttachedProperty;
    private Boolean lowDensitySociety;
    //Water source
    private Boolean municipalCorporation;
    private Boolean borewellTank;
    private Boolean water24x7;
    //Overlooking
    private Boolean overlookingPool;
    private Boolean overlookingParkGarden;
    private Boolean overlookingClub;
    private Boolean overlookingMainRoad;
    //Other features
    private Boolean inGatedSociety;
    private Boolean cornerProperty;
    private Boolean petFriendlySociety;
    private Boolean wheelchairFriendly;
    //Location advantages
    private Boolean closeToMetroStation;
    private Boolean closeToSchool;
    private Boolean closeToHospital;
    private Boolean closeToMarket;
    private Boolean closeToRailwayStation;
    private Boolean closeToAirport;
    private Boolean closeToMall;
    private Boolean closeToHighway;

    //media
    private List<MediaResponse> mediaFiles; 

    public ResidentialPropertyResponse() {
    }

    public ResidentialPropertyResponse(String address, String age, String adminApproved, Boolean airyRooms, Double area, String availability, Integer balconies, Boolean bankAttachedProperty, Integer bathrooms, Integer bedrooms, Boolean borewellTank, Boolean centerCooling, Boolean centrallyAirConditioned, String city, Boolean closeToAirport, Boolean closeToHighway, Boolean closeToHospital, Boolean closeToMall, Boolean closeToMarket, Boolean closeToMetroStation, Boolean closeToRailwayStation, Boolean closeToSchool, Boolean clubhouseCommunityCenter, Boolean cornerProperty, Integer coveredParking, String description, Boolean dishwasher, Boolean dryer, Boolean elevator, Boolean emergencyExit, Boolean expired, String facing, Boolean falseCeilingLighting, Boolean fireAlarm, Boolean fitnessCenter, Integer floor, String furnishing, Boolean gasPipeline, Boolean gym, Boolean heating, Boolean highCeilingHeight, Boolean inGatedSociety, Boolean intercomFacility, Boolean internetConnectivity, Boolean laundry, Boolean lifts, Integer listingId, String locality, Boolean lowDensitySociety, Integer maintenance, Boolean maintenanceStaff, List<MediaResponse> mediaFiles, Boolean modularKitchen, Boolean municipalCorporation, Boolean naturalLight, String nearbyPlace, Boolean noOpenDrainageAround, Integer openParking, Boolean overlookingClub, Boolean overlookingMainRoad, Boolean overlookingParkGarden, Boolean overlookingPool, Boolean park, Boolean petFriendly, Boolean petFriendlySociety, Integer pincode, Boolean poojaRoom, Boolean pool, String powerBackup, String preference, Integer price, Boolean privateGardenTerrace, String propertyType, Boolean rainWaterHarvesting, Boolean recentlyRenovated, String reraNumber, Boolean reraVerified, OwnerResponse residentialOwner, Boolean sauna, Integer securityDeposit, Boolean securityFireAlarm, Boolean securityPersonnel, Boolean separateEntryForServantRoom, Boolean servantRoom, Boolean sold, Boolean spaciousInteriors, String state, Boolean storage, Boolean storeRoom, Boolean studyRoom, Boolean swimmingPool, String title, Integer totalFloors, Boolean vastuCompliant, Boolean vip, Boolean water24x7, Boolean waterPurifier, Boolean wheelchairFriendly) {
        this.address = address;
        this.age = age;
        this.adminApproved = adminApproved;
        this.airyRooms = airyRooms;
        this.area = area;
        this.availability = availability;
        this.balconies = balconies;
        this.bankAttachedProperty = bankAttachedProperty;
        this.bathrooms = bathrooms;
        this.bedrooms = bedrooms;
        this.borewellTank = borewellTank;
        this.centerCooling = centerCooling;
        this.centrallyAirConditioned = centrallyAirConditioned;
        this.city = city;
        this.closeToAirport = closeToAirport;
        this.closeToHighway = closeToHighway;
        this.closeToHospital = closeToHospital;
        this.closeToMall = closeToMall;
        this.closeToMarket = closeToMarket;
        this.closeToMetroStation = closeToMetroStation;
        this.closeToRailwayStation = closeToRailwayStation;
        this.closeToSchool = closeToSchool;
        this.clubhouseCommunityCenter = clubhouseCommunityCenter;
        this.cornerProperty = cornerProperty;
        this.coveredParking = coveredParking;
        this.description = description;
        this.dishwasher = dishwasher;
        this.dryer = dryer;
        this.elevator = elevator;
        this.emergencyExit = emergencyExit;
        this.expired = expired;
        this.facing = facing;
        this.falseCeilingLighting = falseCeilingLighting;
        this.fireAlarm = fireAlarm;
        this.fitnessCenter = fitnessCenter;
        this.floor = floor;
        this.furnishing = furnishing;
        this.gasPipeline = gasPipeline;
        this.gym = gym;
        this.heating = heating;
        this.highCeilingHeight = highCeilingHeight;
        this.inGatedSociety = inGatedSociety;
        this.intercomFacility = intercomFacility;
        this.internetConnectivity = internetConnectivity;
        this.laundry = laundry;
        this.lifts = lifts;
        this.listingId = listingId;
        this.locality = locality;
        this.lowDensitySociety = lowDensitySociety;
        this.maintenance = maintenance;
        this.maintenanceStaff = maintenanceStaff;
        this.mediaFiles = mediaFiles;
        this.modularKitchen = modularKitchen;
        this.municipalCorporation = municipalCorporation;
        this.naturalLight = naturalLight;
        this.nearbyPlace = nearbyPlace;
        this.noOpenDrainageAround = noOpenDrainageAround;
        this.openParking = openParking;
        this.overlookingClub = overlookingClub;
        this.overlookingMainRoad = overlookingMainRoad;
        this.overlookingParkGarden = overlookingParkGarden;
        this.overlookingPool = overlookingPool;
        this.park = park;
        this.petFriendly = petFriendly;
        this.petFriendlySociety = petFriendlySociety;
        this.pincode = pincode;
        this.poojaRoom = poojaRoom;
        this.pool = pool;
        this.powerBackup = powerBackup;
        this.preference = preference;
        this.price = price;
        this.privateGardenTerrace = privateGardenTerrace;
        this.propertyType = propertyType;
        this.rainWaterHarvesting = rainWaterHarvesting;
        this.recentlyRenovated = recentlyRenovated;
        this.reraNumber = reraNumber;
        this.reraVerified = reraVerified;
        this.residentialOwner = residentialOwner;
        this.sauna = sauna;
        this.securityDeposit = securityDeposit;
        this.securityFireAlarm = securityFireAlarm;
        this.securityPersonnel = securityPersonnel;
        this.separateEntryForServantRoom = separateEntryForServantRoom;
        this.servantRoom = servantRoom;
        this.sold = sold;
        this.spaciousInteriors = spaciousInteriors;
        this.state = state;
        this.storage = storage;
        this.storeRoom = storeRoom;
        this.studyRoom = studyRoom;
        this.swimmingPool = swimmingPool;
        this.title = title;
        this.totalFloors = totalFloors;
        this.vastuCompliant = vastuCompliant;
        this.vip = vip;
        this.water24x7 = water24x7;
        this.waterPurifier = waterPurifier;
        this.wheelchairFriendly = wheelchairFriendly;
    }

    public Integer getListingId() {
        return listingId;
    }

    public void setListingId(Integer listingId) {
        this.listingId = listingId;
    }
   
    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Integer maintenance) {
        this.maintenance = maintenance;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public Integer getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getFurnishing() {
        return furnishing;
    }

    public void setFurnishing(String furnishing) {
        this.furnishing = furnishing;
    }

    public String getFacing() {
        return facing;
    }

    public void setFacing(String facing) {
        this.facing = facing;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getReraNumber() {
        return reraNumber;
    }

    public void setReraNumber(String reraNumber) {
        this.reraNumber = reraNumber;
    }

    public Boolean getReraVerified() {
        return reraVerified;
    }

    public void setReraVerified(Boolean reraVerified) {
        this.reraVerified = reraVerified;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPincode() {
        return pincode;
    }

    public void setPincode(Integer pincode) {
        this.pincode = pincode;
    }

    public String getNearbyPlace() {
        return nearbyPlace;
    }

    public void setNearbyPlace(String nearbyPlace) {
        this.nearbyPlace = nearbyPlace;
    }

    public Integer getTotalFloors() {
        return totalFloors;
    }

    public void setTotalFloors(Integer totalFloors) {
        this.totalFloors = totalFloors;
    }

    public Integer getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(Integer securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public Integer getBalconies() {
        return balconies;
    }

    public void setBalconies(Integer balconies) {
        this.balconies = balconies;
    }

    public String getPowerBackup() {
        return powerBackup;
    }

    public void setPowerBackup(String powerBackup) {
        this.powerBackup = powerBackup;
    }

    public Integer getCoveredParking() {
        return coveredParking;
    }

    public void setCoveredParking(Integer coveredParking) {
        this.coveredParking = coveredParking;
    }

    public Integer getOpenParking() {
        return openParking;
    }

    public void setOpenParking(Integer openParking) {
        this.openParking = openParking;
    }

    public Boolean isCenterCooling() {
        return centerCooling;
    }

    public void setCenterCooling(Boolean centerCooling) {
        this.centerCooling = centerCooling;
    }

    public Boolean isFireAlarm() {
        return fireAlarm;
    }

    public void setFireAlarm(Boolean fireAlarm) {
        this.fireAlarm = fireAlarm;
    }

    public Boolean isHeating() {
        return heating;
    }

    public void setHeating(Boolean heating) {
        this.heating = heating;
    }

    public Boolean isGym() {
        return gym;
    }

    public void setGym(Boolean gym) {
        this.gym = gym;
    }

    public Boolean isModularKitchen() {
        return modularKitchen;
    }

    public void setModularKitchen(Boolean modularKitchen) {
        this.modularKitchen = modularKitchen;
    }

    public Boolean isPool() {
        return pool;
    }

    public void setPool(Boolean pool) {
        this.pool = pool;
    }

    public Boolean isElevator() {
        return elevator;
    }

    public void setElevator(Boolean elevator) {
        this.elevator = elevator;
    }

    public Boolean isPetFriendly() {
        return petFriendly;
    }

    public void setPetFriendly(Boolean petFriendly) {
        this.petFriendly = petFriendly;
    }

    public Boolean isStorage() {
        return storage;
    }

    public void setStorage(Boolean storage) {
        this.storage = storage;
    }

    public Boolean isLaundry() {
        return laundry;
    }

    public void setLaundry(Boolean laundry) {
        this.laundry = laundry;
    }

    public Boolean isDishwasher() {
        return dishwasher;
    }

    public void setDishwasher(Boolean dishwasher) {
        this.dishwasher = dishwasher;
    }

    public Boolean isDryer() {
        return dryer;
    }

    public void setDryer(Boolean dryer) {
        this.dryer = dryer;
    }

    public Boolean isSauna() {
        return sauna;
    }

    public void setSauna(Boolean sauna) {
        this.sauna = sauna;
    }

    public Boolean isEmergencyExit() {
        return emergencyExit;
    }

    public void setEmergencyExit(Boolean emergencyExit) {
        this.emergencyExit = emergencyExit;
    }

    public Boolean isWaterPurifier() {
        return waterPurifier;
    }

    public void setWaterPurifier(Boolean waterPurifier) {
        this.waterPurifier = waterPurifier;
    }

    public Boolean isGasPipeline() {
        return gasPipeline;
    }

    public void setGasPipeline(Boolean gasPipeline) {
        this.gasPipeline = gasPipeline;
    }

    public Boolean isPark() {
        return park;
    }

    public void setPark(Boolean park) {
        this.park = park;
    }

    public Boolean isVastuCompliant() {
        return vastuCompliant;
    }

    public void setVastuCompliant(Boolean vastuCompliant) {
        this.vastuCompliant = vastuCompliant;
    }

    public Boolean isRainWaterHarvesting() {
        return rainWaterHarvesting;
    }

    public void setRainWaterHarvesting(Boolean rainWaterHarvesting) {
        this.rainWaterHarvesting = rainWaterHarvesting;
    }

    public Boolean isMaintenanceStaff() {
        return maintenanceStaff;
    }

    public void setMaintenanceStaff(Boolean maintenanceStaff) {
        this.maintenanceStaff = maintenanceStaff;
    }

    public Boolean isPoojaRoom() {
        return poojaRoom;
    }

    public void setPoojaRoom(Boolean poojaRoom) {
        this.poojaRoom = poojaRoom;
    }

    public Boolean isStudyRoom() {
        return studyRoom;
    }

    public void setStudyRoom(Boolean studyRoom) {
        this.studyRoom = studyRoom;
    }

    public Boolean isServantRoom() {
        return servantRoom;
    }

    public void setServantRoom(Boolean servantRoom) {
        this.servantRoom = servantRoom;
    }

    public Boolean isStoreRoom() {
        return storeRoom;
    }

    public void setStoreRoom(Boolean storeRoom) {
        this.storeRoom = storeRoom;
    }

    public Boolean isHighCeilingHeight() {
        return highCeilingHeight;
    }

    public void setHighCeilingHeight(Boolean highCeilingHeight) {
        this.highCeilingHeight = highCeilingHeight;
    }

    public Boolean isFalseCeilingLighting() {
        return falseCeilingLighting;
    }

    public void setFalseCeilingLighting(Boolean falseCeilingLighting) {
        this.falseCeilingLighting = falseCeilingLighting;
    }

    public Boolean isInternetConnectivity() {
        return internetConnectivity;
    }

    public void setInternetConnectivity(Boolean internetConnectivity) {
        this.internetConnectivity = internetConnectivity;
    }

    public Boolean isCentrallyAirConditioned() {
        return centrallyAirConditioned;
    }

    public void setCentrallyAirConditioned(Boolean centrallyAirConditioned) {
        this.centrallyAirConditioned = centrallyAirConditioned;
    }

    public Boolean isSecurityFireAlarm() {
        return securityFireAlarm;
    }

    public void setSecurityFireAlarm(Boolean securityFireAlarm) {
        this.securityFireAlarm = securityFireAlarm;
    }

    public Boolean isRecentlyRenovated() {
        return recentlyRenovated;
    }

    public void setRecentlyRenovated(Boolean recentlyRenovated) {
        this.recentlyRenovated = recentlyRenovated;
    }

    public Boolean isPrivateGardenTerrace() {
        return privateGardenTerrace;
    }

    public void setPrivateGardenTerrace(Boolean privateGardenTerrace) {
        this.privateGardenTerrace = privateGardenTerrace;
    }

    public Boolean isNaturalLight() {
        return naturalLight;
    }

    public void setNaturalLight(Boolean naturalLight) {
        this.naturalLight = naturalLight;
    }

    public Boolean isAiryRooms() {
        return airyRooms;
    }

    public void setAiryRooms(Boolean airyRooms) {
        this.airyRooms = airyRooms;
    }

    public Boolean isIntercomFacility() {
        return intercomFacility;
    }

    public void setIntercomFacility(Boolean intercomFacility) {
        this.intercomFacility = intercomFacility;
    }

    public Boolean isSpaciousInteriors() {
        return spaciousInteriors;
    }

    public void setSpaciousInteriors(Boolean spaciousInteriors) {
        this.spaciousInteriors = spaciousInteriors;
    }

    public Boolean isFitnessCenter() {
        return fitnessCenter;
    }

    public void setFitnessCenter(Boolean fitnessCenter) {
        this.fitnessCenter = fitnessCenter;
    }

    public Boolean isSwimmingPool() {
        return swimmingPool;
    }

    public void setSwimmingPool(Boolean swimmingPool) {
        this.swimmingPool = swimmingPool;
    }

    public Boolean isClubhouseCommunityCenter() {
        return clubhouseCommunityCenter;
    }

    public void setClubhouseCommunityCenter(Boolean clubhouseCommunityCenter) {
        this.clubhouseCommunityCenter = clubhouseCommunityCenter;
    }

    public Boolean isSecurityPersonnel() {
        return securityPersonnel;
    }

    public void setSecurityPersonnel(Boolean securityPersonnel) {
        this.securityPersonnel = securityPersonnel;
    }

    public Boolean isLifts() {
        return lifts;
    }

    public void setLifts(Boolean lifts) {
        this.lifts = lifts;
    }

    public Boolean isSeparateEntryForServantRoom() {
        return separateEntryForServantRoom;
    }

    public void setSeparateEntryForServantRoom(Boolean separateEntryForServantRoom) {
        this.separateEntryForServantRoom = separateEntryForServantRoom;
    }

    public Boolean isNoOpenDrainageAround() {
        return noOpenDrainageAround;
    }

    public void setNoOpenDrainageAround(Boolean noOpenDrainageAround) {
        this.noOpenDrainageAround = noOpenDrainageAround;
    }

    public Boolean isBankAttachedProperty() {
        return bankAttachedProperty;
    }

    public void setBankAttachedProperty(Boolean bankAttachedProperty) {
        this.bankAttachedProperty = bankAttachedProperty;
    }

    public Boolean isLowDensitySociety() {
        return lowDensitySociety;
    }

    public void setLowDensitySociety(Boolean lowDensitySociety) {
        this.lowDensitySociety = lowDensitySociety;
    }

    public Boolean isMunicipalCorporation() {
        return municipalCorporation;
    }

    public void setMunicipalCorporation(Boolean municipalCorporation) {
        this.municipalCorporation = municipalCorporation;
    }

    public Boolean isBorewellTank() {
        return borewellTank;
    }

    public void setBorewellTank(Boolean borewellTank) {
        this.borewellTank = borewellTank;
    }

    public Boolean isWater24x7() {
        return water24x7;
    }

    public void setWater24x7(Boolean water24x7) {
        this.water24x7 = water24x7;
    }

    public Boolean isOverlookingPool() {
        return overlookingPool;
    }

    public void setOverlookingPool(Boolean overlookingPool) {
        this.overlookingPool = overlookingPool;
    }

    public Boolean isOverlookingParkGarden() {
        return overlookingParkGarden;
    }

    public void setOverlookingParkGarden(Boolean overlookingParkGarden) {
        this.overlookingParkGarden = overlookingParkGarden;
    }

    public Boolean isOverlookingClub() {
        return overlookingClub;
    }

    public void setOverlookingClub(Boolean overlookingClub) {
        this.overlookingClub = overlookingClub;
    }

    public Boolean isOverlookingMainRoad() {
        return overlookingMainRoad;
    }

    public void setOverlookingMainRoad(Boolean overlookingMainRoad) {
        this.overlookingMainRoad = overlookingMainRoad;
    }

    public Boolean isInGatedSociety() {
        return inGatedSociety;
    }

    public void setInGatedSociety(Boolean inGatedSociety) {
        this.inGatedSociety = inGatedSociety;
    }

    public Boolean isCornerProperty() {
        return cornerProperty;
    }

    public void setCornerProperty(Boolean cornerProperty) {
        this.cornerProperty = cornerProperty;
    }

    public Boolean isPetFriendlySociety() {
        return petFriendlySociety;
    }

    public void setPetFriendlySociety(Boolean petFriendlySociety) {
        this.petFriendlySociety = petFriendlySociety;
    }

    public Boolean isWheelchairFriendly() {
        return wheelchairFriendly;
    }

    public void setWheelchairFriendly(Boolean wheelchairFriendly) {
        this.wheelchairFriendly = wheelchairFriendly;
    }

    public Boolean isCloseToMetroStation() {
        return closeToMetroStation;
    }

    public void setCloseToMetroStation(Boolean closeToMetroStation) {
        this.closeToMetroStation = closeToMetroStation;
    }

    public Boolean isCloseToSchool() {
        return closeToSchool;
    }

    public void setCloseToSchool(Boolean closeToSchool) {
        this.closeToSchool = closeToSchool;
    }

    public Boolean isCloseToHospital() {
        return closeToHospital;
    }

    public void setCloseToHospital(Boolean closeToHospital) {
        this.closeToHospital = closeToHospital;
    }

    public Boolean isCloseToMarket() {
        return closeToMarket;
    }

    public void setCloseToMarket(Boolean closeToMarket) {
        this.closeToMarket = closeToMarket;
    }

    public Boolean isCloseToRailwayStation() {
        return closeToRailwayStation;
    }

    public void setCloseToRailwayStation(Boolean closeToRailwayStation) {
        this.closeToRailwayStation = closeToRailwayStation;
    }

    public Boolean isCloseToAirport() {
        return closeToAirport;
    }

    public void setCloseToAirport(Boolean closeToAirport) {
        this.closeToAirport = closeToAirport;
    }

    public Boolean isCloseToMall() {
        return closeToMall;
    }

    public void setCloseToMall(Boolean closeToMall) {
        this.closeToMall = closeToMall;
    }

    public Boolean isCloseToHighway() {
        return closeToHighway;
    }

    public void setCloseToHighway(Boolean closeToHighway) {
        this.closeToHighway = closeToHighway;
    }

    public List<MediaResponse> getMediaFiles() {
        return mediaFiles;
    }

    public void setMediaFiles(List<MediaResponse> mediaFiles) {
        this.mediaFiles = mediaFiles;
    }

// public static ResidentialPropertyResponse fromEntity(ResidentialPropertyDetails prop) {
//         ResidentialPropertyAmenities a = prop.getAmenities();
//         List<MediaResponse> mediaFiles = prop.getResidentialPropertyMediaFiles().stream()
//             .map(m -> new MediaResponse(m.getFilename(), m.getOrd(), m.getUrl()))
//             .toList();

//         return new ResidentialPropertyResponse(
//             prop.getAddress(),
//             prop.getAge(),
//             a.isAiryRooms(),
//             prop.getArea(),
//             prop.getAvailability(),
//             prop.getBalconies(),
//             a.isBankAttachedProperty(),
//             prop.getBathrooms(),
//             prop.getBedrooms(),
//             a.isBorewellTank(),
//             a.isCenterCooling(),
//             a.isCentrallyAirConditioned(),
//             prop.getCity(),
//             a.isCloseToAirport(),
//             a.isCloseToHighway(),
//             a.isCloseToHospital(),
//             a.isCloseToMall(),
//             a.isCloseToMarket(),
//             a.isCloseToMetroStation(),
//             a.isCloseToRailwayStation(),
//             a.isCloseToSchool(),
//             a.isClubhouseCommunityCenter(),
//             a.isCornerProperty(),
//             prop.getCoveredParking(),
//             prop.getDescription(),
//             a.isDishwasher(),
//             a.isDryer(),
//             a.isElevator(),
//             a.isEmergencyExit(),
//             prop.getFacing(),
//             a.isFalseCeilingLighting(),
//             a.isFireAlarm(),
//             a.isFitnessCenter(),
//             prop.getFloor(),
//             prop.getFurnishing(),
//             a.isGasPipeline(),
//             a.isGym(),
//             a.isHeating(),
//             a.isHighCeilingHeight(),
//             a.isInGatedSociety(),
//             a.isIntercomFacility(),
//             a.isInternetConnectivity(),
//             a.isLaundry(),
//             a.isLifts(),
//             prop.getListingId(),
//             prop.getLocality(),
//             a.isLowDensitySociety(),
//             prop.getMaintenance(),
//             a.isMaintenanceStaff(),
//             mediaFiles,
//             a.isModularKitchen(),
//             a.isMunicipalCorporation(),
//             a.isNaturalLight(),
//             prop.getNearbyPlace(),
//             a.isNoOpenDrainageAround(),
//             prop.getOpenParking(),
//             a.isOverlookingClub(),
//             a.isOverlookingMainRoad(),
//             a.isOverlookingParkGarden(),
//             a.isOverlookingPool(),
//             a.isPark(),
//             a.isPetFriendly(),
//             a.isPetFriendlySociety(),
//             prop.getPincode(),
//             a.isPoojaRoom(),
//             a.isPool(),
//             prop.getPowerBackup(),
//             prop.getPreference(),
//             prop.getPrice(),
//             a.isPrivateGardenTerrace(),
//             prop.getPropertyType(),
//             a.isRainWaterHarvesting(),
//             a.isRecentlyRenovated(),
//             prop.getReraNumber(),
//             prop.getReraVerified(),
//             a.isSauna(),
//             prop.getSecurityDeposit(),
//             a.isSecurityFireAlarm(),
//             a.isSecurityPersonnel(),
//             a.isSeparateEntryForServantRoom(),
//             a.isServantRoom(),
//             a.isSpaciousInteriors(),
//             prop.getState(),
//             a.isStorage(),
//             a.isStoreRoom(),
//             a.isStudyRoom(),
//             a.isSwimmingPool(),
//             prop.getTitle(),
//             prop.getTotalFloors(),
//             a.isVastuCompliant(),
//             a.isWater24x7(),
//             a.isWaterPurifier(),
//             a.isWheelchairFriendly()
//         );
//     }

    public String getAdminApproved() {
        return adminApproved;
    }

    public void setAdminApproved(String adminApproved) {
        this.adminApproved = adminApproved;
    }

    public Boolean isExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public OwnerResponse getResidentialOwner() {
        return residentialOwner;
    }

    public void setResidentialOwner(OwnerResponse residentialOwner) {
        this.residentialOwner = residentialOwner;
    }

    public Boolean isVip() {
        return vip;
    }

    public void setVip(Boolean vip) {
        this.vip = vip;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "ResidentialPropertyResponse [listingId=" + listingId + ", category=" + category + ", preference="
                + preference + ", propertyType=" + propertyType + ", title=" + title + ", description=" + description
                + ", price=" + price + ", maintenance=" + maintenance + ", area=" + area + ", bedrooms=" + bedrooms
                + ", bathrooms=" + bathrooms + ", furnishing=" + furnishing + ", facing=" + facing + ", floor=" + floor
                + ", age=" + age + ", availability=" + availability + ", reraNumber=" + reraNumber + ", reraVerified="
                + reraVerified + ", state=" + state + ", city=" + city + ", locality=" + locality + ", address="
                + address + ", pincode=" + pincode + ", nearbyPlace=" + nearbyPlace + ", totalFloors=" + totalFloors
                + ", securityDeposit=" + securityDeposit + ", balconies=" + balconies + ", powerBackup=" + powerBackup
                + ", coveredParking=" + coveredParking + ", openParking=" + openParking + ", adminApproved="
                + adminApproved + ", expired=" + expired + ", residentialOwner=" + residentialOwner + ", vip=" + vip
                + ", centerCooling=" + centerCooling + ", fireAlarm=" + fireAlarm + ", heating=" + heating + ", gym="
                + gym + ", modularKitchen=" + modularKitchen + ", pool=" + pool + ", elevator=" + elevator
                + ", petFriendly=" + petFriendly + ", storage=" + storage + ", laundry=" + laundry + ", dishwasher="
                + dishwasher + ", dryer=" + dryer + ", sauna=" + sauna + ", emergencyExit=" + emergencyExit
                + ", waterPurifier=" + waterPurifier + ", gasPipeline=" + gasPipeline + ", park=" + park
                + ", vastuCompliant=" + vastuCompliant + ", rainWaterHarvesting=" + rainWaterHarvesting
                + ", maintenanceStaff=" + maintenanceStaff + ", poojaRoom=" + poojaRoom + ", studyRoom=" + studyRoom
                + ", servantRoom=" + servantRoom + ", storeRoom=" + storeRoom + ", highCeilingHeight="
                + highCeilingHeight + ", falseCeilingLighting=" + falseCeilingLighting + ", internetConnectivity="
                + internetConnectivity + ", centrallyAirConditioned=" + centrallyAirConditioned + ", securityFireAlarm="
                + securityFireAlarm + ", recentlyRenovated=" + recentlyRenovated + ", privateGardenTerrace="
                + privateGardenTerrace + ", naturalLight=" + naturalLight + ", airyRooms=" + airyRooms
                + ", intercomFacility=" + intercomFacility + ", spaciousInteriors=" + spaciousInteriors
                + ", fitnessCenter=" + fitnessCenter + ", swimmingPool=" + swimmingPool + ", clubhouseCommunityCenter="
                + clubhouseCommunityCenter + ", securityPersonnel=" + securityPersonnel + ", lifts=" + lifts
                + ", separateEntryForServantRoom=" + separateEntryForServantRoom + ", noOpenDrainageAround="
                + noOpenDrainageAround + ", bankAttachedProperty=" + bankAttachedProperty + ", lowDensitySociety="
                + lowDensitySociety + ", municipalCorporation=" + municipalCorporation + ", borewellTank="
                + borewellTank + ", water24x7=" + water24x7 + ", overlookingPool=" + overlookingPool
                + ", overlookingParkGarden=" + overlookingParkGarden + ", overlookingClub=" + overlookingClub
                + ", overlookingMainRoad=" + overlookingMainRoad + ", inGatedSociety=" + inGatedSociety
                + ", cornerProperty=" + cornerProperty + ", petFriendlySociety=" + petFriendlySociety
                + ", wheelchairFriendly=" + wheelchairFriendly + ", closeToMetroStation=" + closeToMetroStation
                + ", closeToSchool=" + closeToSchool + ", closeToHospital=" + closeToHospital + ", closeToMarket="
                + closeToMarket + ", closeToRailwayStation=" + closeToRailwayStation + ", closeToAirport="
                + closeToAirport + ", closeToMall=" + closeToMall + ", closeToHighway=" + closeToHighway
                + ", mediaFiles=" + mediaFiles + "]";
    }

    public Boolean getSold() {
        return sold;
    }

    public void setSold(Boolean sold) {
        this.sold = sold;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(OffsetDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    
}
