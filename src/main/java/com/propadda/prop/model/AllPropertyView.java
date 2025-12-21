package com.propadda.prop.model;

import java.time.OffsetDateTime;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "v_all_properties")
@Immutable
public class AllPropertyView {

    @Id
    @Column(name = "global_id")
    private String globalId;

    @Column(name = "listing_id")
    private Integer listingId;

    @Column(name = "preference")
    private String preference;
    
    @Column(name = "property_type")
    private String propertyType;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Long price;

    @Column(name = "area")
    private Double area;

    // Residential
    @Column(name = "bedrooms")
    private Integer bedrooms;

    @Column(name = "bathrooms")
    private Integer bathrooms;

    // Commercial
    @Column(name = "cabins")
    private Integer cabins;

    @Column(name = "meeting_room")
    private Boolean meetingRoom;

    @Column(name = "washroom")
    private Boolean washroom;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name = "locality")
    private String locality;

    @Column(name = "address")
    private String address;

    @Column(name = "admin_approved")
    private String adminApproved;

    @Column(name = "expired")
    private Boolean expired;

    @Column(name = "vip")
    private Boolean vip;

    @Column(name = "sold")
    private Boolean sold;

    @Column(name = "category")
    private String category;

    @Column(name = "rera_verified")
    private Boolean reraVerified;

    @Column(name = "rera_number")
    private String reraNumber;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "approved_at")
    private OffsetDateTime approvedAt;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "image_object_name")
    private String imageObjectName;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageObjectName() {
        return imageObjectName;
    }

    public void setImageObjectName(String imageObjectName) {
        this.imageObjectName = imageObjectName;
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

    public String getReraNumber() {
        return reraNumber;
    }

    public void setReraNumber(String reraNumber) {
        this.reraNumber = reraNumber;
    }
    
}
