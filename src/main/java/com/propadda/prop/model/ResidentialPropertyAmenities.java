package com.propadda.prop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "residential_property_amenities")
public class ResidentialPropertyAmenities {
    
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "amenities_id")
private Integer amenitiesId;

@OneToOne(fetch = FetchType.EAGER, optional = false)
@JoinColumn(name = "listing_id", unique = true)
@JsonBackReference
private ResidentialPropertyDetails residentialProperty;

// Property features

@Column(name = "center_cooling")
private Boolean centerCooling;

@Column(name = "fire_alarm")
private Boolean fireAlarm;

@Column(name = "heating")
private Boolean heating;

@Column(name = "gym")
private Boolean gym;

@Column(name = "modular_kitchen")
private Boolean modularKitchen;

@Column(name = "pool")
private Boolean pool;

@Column(name = "elevator")
private Boolean elevator;

@Column(name = "pet_friendly")
private Boolean petFriendly;

@Column(name = "storage")
private Boolean storage;

@Column(name = "laundry")
private Boolean laundry;

@Column(name = "dishwasher")
private Boolean dishwasher;

@Column(name = "dryer")
private Boolean dryer;

@Column(name = "sauna")
private Boolean sauna;

@Column(name = "emergency_exit")
private Boolean emergencyExit;

@Column(name = "water_purifier")
private Boolean waterPurifier;

@Column(name = "gas_pipeline")
private Boolean gasPipeline;

@Column(name = "park")
private Boolean park;

@Column(name = "vastu_compliant")
private Boolean vastuCompliant;

@Column(name = "rain_water_harvesting")
private Boolean rainWaterHarvesting;

@Column(name = "maintenance_staff")
private Boolean maintenanceStaff;

// Other rooms

@Column(name = "pooja_room")
private Boolean poojaRoom;

@Column(name = "study_room")
private Boolean studyRoom;

@Column(name = "servant_room")
private Boolean servantRoom;

@Column(name = "store_room")
private Boolean storeRoom;

// Additional property features

@Column(name = "high_ceiling_height")
private Boolean highCeilingHeight;

@Column(name = "false_ceiling_lighting")
private Boolean falseCeilingLighting;

@Column(name = "internet_connectivity")
private Boolean internetConnectivity;

@Column(name = "centrally_air_conditioned")
private Boolean centrallyAirConditioned;

@Column(name = "security_fire_alarm")
private Boolean securityFireAlarm;

@Column(name = "recently_renovated")
private Boolean recentlyRenovated;

@Column(name = "private_garden_terrace")
private Boolean privateGardenTerrace;

@Column(name = "natural_light")
private Boolean naturalLight;

@Column(name = "airy_rooms")
private Boolean airyRooms;

@Column(name = "intercom_facility")
private Boolean intercomFacility;

@Column(name = "spacious_interiors")
private Boolean spaciousInteriors;

// Society/building features

@Column(name = "fitness_center")
private Boolean fitnessCenter;

@Column(name = "swimming_pool")
private Boolean swimmingPool;

@Column(name = "clubhouse_community_center")
private Boolean clubhouseCommunityCenter;

@Column(name = "security_personnel")
private Boolean securityPersonnel;

@Column(name = "lifts")
private Boolean lifts;

// Additional features

@Column(name = "separate_entry_for_servant_room")
private Boolean separateEntryForServantRoom;

@Column(name = "no_open_drainage_around")
private Boolean noOpenDrainageAround;

@Column(name = "bank_attached_property")
private Boolean bankAttachedProperty;

@Column(name = "low_density_society")
private Boolean lowDensitySociety;

// Water source

@Column(name = "municipal_corporation")
private Boolean municipalCorporation;

@Column(name = "borewell_tank")
private Boolean borewellTank;

@Column(name = "water24x7")  // Keep this as-is unless you change it in DB
private Boolean water24x7;

// Overlooking

@Column(name = "overlooking_pool")
private Boolean overlookingPool;

@Column(name = "overlooking_park_garden")
private Boolean overlookingParkGarden;

@Column(name = "overlooking_club")
private Boolean overlookingClub;

@Column(name = "overlooking_main_road")
private Boolean overlookingMainRoad;

// Other features

@Column(name = "in_gated_society")
private Boolean inGatedSociety;

@Column(name = "corner_property")
private Boolean cornerProperty;

@Column(name = "pet_friendly_society")
private Boolean petFriendlySociety;

@Column(name = "wheelchair_friendly")
private Boolean wheelchairFriendly;

// Location advantages

@Column(name = "close_to_metro_station")
private Boolean closeToMetroStation;

@Column(name = "close_to_school")
private Boolean closeToSchool;

@Column(name = "close_to_hospital")
private Boolean closeToHospital;

@Column(name = "close_to_market")
private Boolean closeToMarket;

@Column(name = "close_to_railway_station")
private Boolean closeToRailwayStation;

@Column(name = "close_to_airport")
private Boolean closeToAirport;

@Column(name = "close_to_mall")
private Boolean closeToMall;

@Column(name = "close_to_highway")
private Boolean closeToHighway;

    public ResidentialPropertyAmenities() {
    }

    public ResidentialPropertyAmenities(Boolean airyRooms, Boolean bankAttachedProperty, Boolean borewellTank, Boolean centerCooling, Boolean centrallyAirConditioned, Boolean closeToAirport, Boolean closeToHighway, Boolean closeToHospital, Boolean closeToMall, Boolean closeToMarket, Boolean closeToMetroStation, Boolean closeToRailwayStation, Boolean closeToSchool, Boolean clubhouseCommunityCenter, Boolean cornerProperty, Boolean dishwasher, Boolean dryer, Boolean elevator, Boolean emergencyExit, Boolean falseCeilingLighting, Boolean fireAlarm, Boolean fitnessCenter, Boolean gasPipeline, Boolean gym, Boolean heating, Boolean highCeilingHeight, Boolean inGatedSociety, Boolean intercomFacility, Boolean internetConnectivity, Boolean laundry, Boolean lifts, Boolean lowDensitySociety, Boolean maintenanceStaff, Boolean modularKitchen, Boolean municipalCorporation, Boolean naturalLight, Boolean noOpenDrainageAround, Boolean overlookingClub, Boolean overlookingMainRoad, Boolean overlookingParkGarden, Boolean overlookingPool, Boolean park, Boolean petFriendly, Boolean petFriendlySociety, Boolean poojaRoom, Boolean pool, Boolean privateGardenTerrace, Boolean rainWaterHarvesting, Boolean recentlyRenovated, ResidentialPropertyDetails residentialProperty, Boolean sauna, Boolean securityFireAlarm, Boolean securityPersonnel, Boolean separateEntryForServantRoom, Boolean servantRoom, Boolean spaciousInteriors, Boolean storage, Boolean storeRoom, Boolean studyRoom, Boolean swimmingPool, Boolean vastuCompliant, Boolean water24x7, Boolean waterPurifier, Boolean wheelchairFriendly) {
        this.airyRooms = airyRooms;
        this.bankAttachedProperty = bankAttachedProperty;
        this.borewellTank = borewellTank;
        this.centerCooling = centerCooling;
        this.centrallyAirConditioned = centrallyAirConditioned;
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
        this.dishwasher = dishwasher;
        this.dryer = dryer;
        this.elevator = elevator;
        this.emergencyExit = emergencyExit;
        this.falseCeilingLighting = falseCeilingLighting;
        this.fireAlarm = fireAlarm;
        this.fitnessCenter = fitnessCenter;
        this.gasPipeline = gasPipeline;
        this.gym = gym;
        this.heating = heating;
        this.highCeilingHeight = highCeilingHeight;
        this.inGatedSociety = inGatedSociety;
        this.intercomFacility = intercomFacility;
        this.internetConnectivity = internetConnectivity;
        this.laundry = laundry;
        this.lifts = lifts;
        this.lowDensitySociety = lowDensitySociety;
        this.maintenanceStaff = maintenanceStaff;
        this.modularKitchen = modularKitchen;
        this.municipalCorporation = municipalCorporation;
        this.naturalLight = naturalLight;
        this.noOpenDrainageAround = noOpenDrainageAround;
        this.overlookingClub = overlookingClub;
        this.overlookingMainRoad = overlookingMainRoad;
        this.overlookingParkGarden = overlookingParkGarden;
        this.overlookingPool = overlookingPool;
        this.park = park;
        this.petFriendly = petFriendly;
        this.petFriendlySociety = petFriendlySociety;
        this.poojaRoom = poojaRoom;
        this.pool = pool;
        this.privateGardenTerrace = privateGardenTerrace;
        this.rainWaterHarvesting = rainWaterHarvesting;
        this.recentlyRenovated = recentlyRenovated;
        this.residentialProperty = residentialProperty;
        this.sauna = sauna;
        this.securityFireAlarm = securityFireAlarm;
        this.securityPersonnel = securityPersonnel;
        this.separateEntryForServantRoom = separateEntryForServantRoom;
        this.servantRoom = servantRoom;
        this.spaciousInteriors = spaciousInteriors;
        this.storage = storage;
        this.storeRoom = storeRoom;
        this.studyRoom = studyRoom;
        this.swimmingPool = swimmingPool;
        this.vastuCompliant = vastuCompliant;
        this.water24x7 = water24x7;
        this.waterPurifier = waterPurifier;
        this.wheelchairFriendly = wheelchairFriendly;
    }

    public Integer getAmenitiesId() {
        return amenitiesId;
    }

    public void setAmenitiesId(Integer amenitiesId) {
        this.amenitiesId = amenitiesId;
    }

    public ResidentialPropertyDetails getResidentialProperty() {
        return residentialProperty;
    }

    public void setResidentialProperty(ResidentialPropertyDetails residentialProperty) {
        this.residentialProperty = residentialProperty;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ResidentialPropertyAmenities{");
        sb.append("amenitiesId=").append(amenitiesId);
        sb.append(", residentialProperty=").append(residentialProperty);
        sb.append(", centerCooling=").append(centerCooling);
        sb.append(", fireAlarm=").append(fireAlarm);
        sb.append(", heating=").append(heating);
        sb.append(", gym=").append(gym);
        sb.append(", modularKitchen=").append(modularKitchen);
        sb.append(", pool=").append(pool);
        sb.append(", elevator=").append(elevator);
        sb.append(", petFriendly=").append(petFriendly);
        sb.append(", storage=").append(storage);
        sb.append(", laundry=").append(laundry);
        sb.append(", dishwasher=").append(dishwasher);
        sb.append(", dryer=").append(dryer);
        sb.append(", sauna=").append(sauna);
        sb.append(", emergencyExit=").append(emergencyExit);
        sb.append(", waterPurifier=").append(waterPurifier);
        sb.append(", gasPipeline=").append(gasPipeline);
        sb.append(", park=").append(park);
        sb.append(", vastuCompliant=").append(vastuCompliant);
        sb.append(", rainWaterHarvesting=").append(rainWaterHarvesting);
        sb.append(", maintenanceStaff=").append(maintenanceStaff);
        sb.append(", poojaRoom=").append(poojaRoom);
        sb.append(", studyRoom=").append(studyRoom);
        sb.append(", servantRoom=").append(servantRoom);
        sb.append(", storeRoom=").append(storeRoom);
        sb.append(", highCeilingHeight=").append(highCeilingHeight);
        sb.append(", falseCeilingLighting=").append(falseCeilingLighting);
        sb.append(", internetConnectivity=").append(internetConnectivity);
        sb.append(", centrallyAirConditioned=").append(centrallyAirConditioned);
        sb.append(", securityFireAlarm=").append(securityFireAlarm);
        sb.append(", recentlyRenovated=").append(recentlyRenovated);
        sb.append(", privateGardenTerrace=").append(privateGardenTerrace);
        sb.append(", naturalLight=").append(naturalLight);
        sb.append(", airyRooms=").append(airyRooms);
        sb.append(", intercomFacility=").append(intercomFacility);
        sb.append(", spaciousInteriors=").append(spaciousInteriors);
        sb.append(", fitnessCenter=").append(fitnessCenter);
        sb.append(", swimmingPool=").append(swimmingPool);
        sb.append(", clubhouseCommunityCenter=").append(clubhouseCommunityCenter);
        sb.append(", securityPersonnel=").append(securityPersonnel);
        sb.append(", lifts=").append(lifts);
        sb.append(", separateEntryForServantRoom=").append(separateEntryForServantRoom);
        sb.append(", noOpenDrainageAround=").append(noOpenDrainageAround);
        sb.append(", bankAttachedProperty=").append(bankAttachedProperty);
        sb.append(", lowDensitySociety=").append(lowDensitySociety);
        sb.append(", municipalCorporation=").append(municipalCorporation);
        sb.append(", borewellTank=").append(borewellTank);
        sb.append(", Water24x7=").append(water24x7);
        sb.append(", overlookingPool=").append(overlookingPool);
        sb.append(", overlookingParkGarden=").append(overlookingParkGarden);
        sb.append(", overlookingClub=").append(overlookingClub);
        sb.append(", overlookingMainRoad=").append(overlookingMainRoad);
        sb.append(", inGatedSociety=").append(inGatedSociety);
        sb.append(", cornerProperty=").append(cornerProperty);
        sb.append(", petFriendlySociety=").append(petFriendlySociety);
        sb.append(", wheelchairFriendly=").append(wheelchairFriendly);
        sb.append(", closeToMetroStation=").append(closeToMetroStation);
        sb.append(", closeToSchool=").append(closeToSchool);
        sb.append(", closeToHospital=").append(closeToHospital);
        sb.append(", closeToMarket=").append(closeToMarket);
        sb.append(", closeToRailwayStation=").append(closeToRailwayStation);
        sb.append(", closeToAirport=").append(closeToAirport);
        sb.append(", closeToMall=").append(closeToMall);
        sb.append(", closeToHighway=").append(closeToHighway);
        sb.append('}');
        return sb.toString();
    }

}
