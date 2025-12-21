package com.propadda.prop.dto;

import java.time.OffsetDateTime;

public class AllPropertyViewResponse {

    private String globalId;
    private Integer listingId;
    private String preference;
    private String propertyType;
    private String title;
    private String description;
    private Long price;
    private Double area;
    // Residential
    private Integer bedrooms;
    private Integer bathrooms;
    // Commercial
    private Integer cabins;
    private Boolean meetingRoom;
    private Boolean washroom;
    private String state;
    private String city;
    private String locality;
    private String address;
    private String adminApproved;
    private Boolean expired;
    private Boolean vip;
    private Boolean sold;
    private String category;
    private Boolean reraVerified;
    private String reraNumber;
    private OffsetDateTime createdAt;
    private OffsetDateTime approvedAt;
    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String mediaUrl;

    public String getGlobalId() {
        return globalId;
    }
    public void setGlobalId(String globalId) {
        this.globalId = globalId;
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
    public Long getPrice() {
        return price;
    }
    public void setPrice(Long price) {
        this.price = price;
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
    public String getAdminApproved() {
        return adminApproved;
    }
    public void setAdminApproved(String adminApproved) {
        this.adminApproved = adminApproved;
    }
    public Boolean getExpired() {
        return expired;
    }
    public void setExpired(Boolean expired) {
        this.expired = expired;
    }
    public Boolean getVip() {
        return vip;
    }
    public void setVip(Boolean vip) {
        this.vip = vip;
    }
    public Boolean getSold() {
        return sold;
    }
    public void setSold(Boolean sold) {
        this.sold = sold;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public Boolean getReraVerified() {
        return reraVerified;
    }
    public void setReraVerified(Boolean reraVerified) {
        this.reraVerified = reraVerified;
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
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getMediaUrl() {
        return mediaUrl;
    }
    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getReraNumber() {
        return reraNumber;
    }

    public void setReraNumber(String reraNumber) {
        this.reraNumber = reraNumber;
    }
    
}
