package com.propadda.prop.dto;

import java.time.OffsetDateTime;
import java.util.List;

public class CommercialPropertyResponse {
  
    private Integer listingId;
    private String category;
    private String preference;
    private String propertyType;
    private String title;
    private String description;
    private Integer price;
    private Double area;
    private String reraNumber;
    private Boolean reraVerified;
    private Integer cabins;
    private Boolean meetingRoom;
    private Boolean washroom;
    private Boolean conferenceRoom;
    private Boolean receptionArea;
    private Boolean lift;
    private Boolean parking;
    private String age;
    private String availability;
    private Integer securityDeposit;
    private Integer lockIn;
    private Integer yearlyIncrease;
    private Integer floor;
    private Integer totalFloors;
    private String address;
    private String city;
    private String state;
    private Integer pincode;
    private String locality;
    private String nearbyPlace;
    private String adminApproved;
    private Boolean expired;
    private OwnerResponse commercialOwner;
    private Boolean vip;
    private Boolean sold;
    private OffsetDateTime createdAt;
    private OffsetDateTime approvedAt;

    //media
    private List<MediaResponse> mediaFiles; 

    public CommercialPropertyResponse() {
    }

    public CommercialPropertyResponse(String address, String age, String adminApproved, Double area, String availability, Integer cabins, String city, OwnerResponse commercialOwner, Boolean conferenceRoom, String description, Boolean expired, Integer floor, Boolean lift, Integer listingId, String locality, Integer lockIn, List<MediaResponse> mediaFiles, Boolean meetingRoom, String nearbyPlace, Boolean parking, Integer pincode, String preference, Integer price, String propertyType, Boolean receptionArea, String reraNumber, Boolean reraVerified, Integer securityDeposit, Boolean sold, String state, String title, Integer totalFloors, Boolean vip, Boolean washroom, Integer yearlyIncrease) {
        this.address = address;
        this.age = age;
        this.adminApproved = adminApproved;
        this.area = area;
        this.availability = availability;
        this.cabins = cabins;
        this.city = city;
        this.commercialOwner = commercialOwner;
        this.conferenceRoom = conferenceRoom;
        this.description = description;
        this.expired = expired;
        this.floor = floor;
        this.lift = lift;
        this.listingId = listingId;
        this.locality = locality;
        this.lockIn = lockIn;
        this.mediaFiles = mediaFiles;
        this.meetingRoom = meetingRoom;
        this.nearbyPlace = nearbyPlace;
        this.parking = parking;
        this.pincode = pincode;
        this.preference = preference;
        this.price = price;
        this.propertyType = propertyType;
        this.receptionArea = receptionArea;
        this.reraNumber = reraNumber;
        this.reraVerified = reraVerified;
        this.securityDeposit = securityDeposit;
        this.sold = sold;
        this.state = state;
        this.title = title;
        this.totalFloors = totalFloors;
        this.vip = vip;
        this.washroom = washroom;
        this.yearlyIncrease = yearlyIncrease;
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

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Integer getCabins() {
        return cabins;
    }

    public void setCabins(Integer cabins) {
        this.cabins = cabins;
    }

    public Boolean getMeetingRoom() {
        return meetingRoom;
    }

    public void setMeetingRoom(Boolean meetingRoom) {
        this.meetingRoom = meetingRoom;
    }

    public Boolean getWashroom() {
        return washroom;
    }

    public void setWashroom(Boolean washroom) {
        this.washroom = washroom;
    }

    public Boolean getConferenceRoom() {
        return conferenceRoom;
    }

    public void setConferenceRoom(Boolean conferenceRoom) {
        this.conferenceRoom = conferenceRoom;
    }

    public Boolean getReceptionArea() {
        return receptionArea;
    }

    public void setReceptionArea(Boolean receptionArea) {
        this.receptionArea = receptionArea;
    }

    public Boolean getLift() {
        return lift;
    }

    public void setLift(Boolean lift) {
        this.lift = lift;
    }

    public Boolean getParking() {
        return parking;
    }

    public void setParking(Boolean parking) {
        this.parking = parking;
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

    public Integer getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(Integer securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public Integer getLockIn() {
        return lockIn;
    }

    public void setLockIn(Integer lockIn) {
        this.lockIn = lockIn;
    }

    public Integer getYearlyIncrease() {
        return yearlyIncrease;
    }

    public void setYearlyIncrease(Integer yearlyIncrease) {
        this.yearlyIncrease = yearlyIncrease;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getTotalFloors() {
        return totalFloors;
    }

    public void setTotalFloors(Integer totalFloors) {
        this.totalFloors = totalFloors;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getPincode() {
        return pincode;
    }

    public void setPincode(Integer pincode) {
        this.pincode = pincode;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getNearbyPlace() {
        return nearbyPlace;
    }

    public void setNearbyPlace(String nearbyPlace) {
        this.nearbyPlace = nearbyPlace;
    }

    public List<MediaResponse> getMediaFiles() {
        return mediaFiles;
    }

    public void setMediaFiles(List<MediaResponse> mediaFiles) {
        this.mediaFiles = mediaFiles;
    }

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

    public OwnerResponse getCommercialOwner() {
        return commercialOwner;
    }

    public void setCommercialOwner(OwnerResponse commercialOwner) {
        this.commercialOwner = commercialOwner;
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

    @Override
    public String toString() {
        return "CommercialPropertyResponse [listingId=" + listingId + ", category=" + category + ", preference="
                + preference + ", propertyType=" + propertyType + ", title=" + title + ", description=" + description
                + ", price=" + price + ", area=" + area + ", reraNumber=" + reraNumber + ", reraVerified="
                + reraVerified + ", cabins=" + cabins + ", meetingRoom=" + meetingRoom + ", washroom=" + washroom
                + ", conferenceRoom=" + conferenceRoom + ", receptionArea=" + receptionArea + ", lift=" + lift
                + ", parking=" + parking + ", age=" + age + ", availability=" + availability + ", securityDeposit="
                + securityDeposit + ", lockIn=" + lockIn + ", yearlyIncrease=" + yearlyIncrease + ", floor=" + floor
                + ", totalFloors=" + totalFloors + ", address=" + address + ", city=" + city + ", state=" + state
                + ", pincode=" + pincode + ", locality=" + locality + ", nearbyPlace=" + nearbyPlace
                + ", adminApproved=" + adminApproved + ", expired=" + expired + ", commercialOwner=" + commercialOwner
                + ", vip=" + vip + ", mediaFiles=" + mediaFiles + "]";
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
