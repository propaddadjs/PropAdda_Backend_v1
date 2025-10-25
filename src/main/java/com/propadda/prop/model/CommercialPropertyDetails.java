// Author-Hemant Arora
package com.propadda.prop.model;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "commercial_property_details")
public class CommercialPropertyDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="listing_id")
    private Integer listingId;

    @Column(name="category", nullable = false)
    private String category;

    @Column(name="preference", nullable = false)
    private String preference;

    @Column(name="property_type", nullable = false)
    private String propertyType;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name="price", nullable = false)
    private Integer price;

    @Column(name="area", nullable = false)
    private Double area;

    @Column(name = "rera_number")
    private String reraNumber;

    @Column(name = "rera_verified")
    private Boolean reraVerified;

    @Column(name="cabins")
    private Integer cabins;

    @Column(name="meeting_room")
    private Boolean meetingRoom;

    @Column(name="washroom")
    private Boolean washroom;

    @Column(name="conference_room")
    private Boolean conferenceRoom;

    @Column(name="reception_area")
    private Boolean receptionArea;

    @Column(name="lift")
    private Boolean lift;

    @Column(name="parking")
    private Boolean parking;

    @Column(name="age")
    private String age;

    @Column(name="availability")
    private String availability;

    @Column(name="securityDeposit")
    private Integer securityDeposit;

    @Column(name="lockIn")
    private Integer lockIn;

    @Column(name="yearlyIncrease")
    private Integer yearlyIncrease;

    @Column(name="floor")
    private Integer floor;

    @Column(name="total_floors")
    private Integer totalFloors;

    @Column(name="address")
    private String address;

    @Column(name="city")
    private String city;

    @Column(name="state")
    private String state;

    @Column(name="pincode")
    private Integer pincode;

    @Column(name="locality")
    private String locality;

    @Column(name="nearby_place")
    private String nearbyPlace;

    @Column(name="admin_approved")
    private String adminApproved;

    @Column(name="expired")
    private Boolean expired;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="user_id")
    @JsonBackReference
    private Users commercialOwner;

    @Column(name="vip")
    private Boolean vip;

    @Column(name="sold")
    private Boolean sold;

    @OneToMany(mappedBy="property", cascade=CascadeType.ALL, orphanRemoval=true)
    @JsonManagedReference
    private List<CommercialPropertyMedia> commercialPropertyMediaFiles;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "approved_at")
    private OffsetDateTime approvedAt;

    public CommercialPropertyDetails() {
    }

    public CommercialPropertyDetails(String address, String age, String adminApproved, Double area, String availability, Integer cabins, String city, Boolean conferenceRoom, String description, Boolean expired, Integer floor, Boolean lift, String locality, Integer lockIn, Boolean meetingRoom, String nearbyPlace, Boolean parking, Integer pincode, String preference, Integer price, String propertyType, Boolean receptionArea, Integer securityDeposit, String state, String title, Integer totalFloors, Boolean vip, Boolean washroom, Integer yearlyIncrease) {
        this.address = address;
        this.age = age;
        this.adminApproved = adminApproved;
        this.area = area;
        this.availability = availability;
        this.cabins = cabins;
        this.city = city;
        this.conferenceRoom = conferenceRoom;
        this.description = description;
        this.expired = expired;
        this.floor = floor;
        this.lift = lift;
        this.locality = locality;
        this.lockIn = lockIn;
        this.meetingRoom = meetingRoom;
        this.nearbyPlace = nearbyPlace;
        this.parking = parking;
        this.pincode = pincode;
        this.preference = preference;
        this.price = price;
        this.propertyType = propertyType;
        this.receptionArea = receptionArea;
        this.securityDeposit = securityDeposit;
        this.state = state;
        this.title = title;
        this.totalFloors = totalFloors;
        this.vip = vip;
        this.washroom = washroom;
        this.yearlyIncrease = yearlyIncrease;
    }


    // Getters and Setters

    public Integer getListingId() { return listingId; }
    public void setListingId(Integer listingId) { this.listingId = listingId; }

    public String getPreference() { return preference; }
    public void setPreference(String preference) { this.preference = preference; }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPropertyType() { return propertyType; }
    public void setPropertyType(String propertyType) { this.propertyType = propertyType; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }

    public Double getArea() { return area; }
    public void setArea(Double area) { this.area = area; }

    public Integer getCabins() { return cabins; }
    public void setCabins(Integer cabins) { this.cabins = cabins; }

    public Boolean getMeetingRoom() { return meetingRoom; }
    public void setMeetingRoom(Boolean meetingRoom) { this.meetingRoom = meetingRoom; }

    public Boolean getWashroom() { return washroom; }
    public void setWashroom(Boolean washroom) { this.washroom = washroom; }

    public Boolean getConferenceRoom() { return conferenceRoom; }
    public void setConferenceRoom(Boolean conferenceRoom) { this.conferenceRoom = conferenceRoom; }

    public Boolean getReceptionArea() { return receptionArea; }
    public void setReceptionArea(Boolean receptionArea) { this.receptionArea = receptionArea; }

    public Integer getFloor() { return floor; }
    public void setFloor(Integer floor) { this.floor = floor; }

    public Boolean getLift() { return lift; }
    public void setLift(Boolean lift) { this.lift = lift; }

    public Boolean getParking() { return parking; }
    public void setParking(Boolean parking) { this.parking = parking; }

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    public Integer getSecurityDeposit() { return securityDeposit; }
    public void setSecurityDeposit(Integer securityDeposit) { this.securityDeposit = securityDeposit; }

    public Integer getLockIn() { return lockIn; }
    public void setLockIn(Integer lockIn) { this.lockIn = lockIn; }

    public Integer getYearlyIncrease() { return yearlyIncrease; }
    public void setYearlyIncrease(Integer yearlyIncrease) { this.yearlyIncrease = yearlyIncrease; }

    public List<CommercialPropertyMedia> getCommercialPropertyMediaFiles() {
        return commercialPropertyMediaFiles;
    }
    public void setCommercialPropertyMediaFiles(List<CommercialPropertyMedia> commercialPropertyMediaFiles) {
        this.commercialPropertyMediaFiles = commercialPropertyMediaFiles;
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

    public Users getCommercialOwner() {
        return commercialOwner;
    }

    public void setCommercialOwner(Users commercialOwner) {
        this.commercialOwner = commercialOwner;
    }

    public Boolean isVip() {
        return vip;
    }

    public void setVip(Boolean vip) {
        this.vip = vip;
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

