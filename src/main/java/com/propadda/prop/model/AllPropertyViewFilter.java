package com.propadda.prop.model;

import java.time.OffsetDateTime;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "v_all_properties_filter")
@Immutable
public class AllPropertyViewFilter {

    @Id
    @Column(name = "global_id")
    private String globalId;

    @Column(name = "listing_id")
    private Integer listingId;

    @Column(name = "category")
    private String category;

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

    @Column(name = "bedrooms")
    private Integer bedrooms;

    @Column(name = "bathrooms")
    private Integer bathrooms;

    @Column(name = "cabins")
    private Integer cabins;

    @Column(name = "meeting_room")
    private Boolean meetingRoom;

    @Column(name = "washroom")
    private Boolean washroom;

    @Column(name = "furnishing")
    private String furnishing;

    @Column(name = "age")
    private String age;

    @Column(name = "availability")
    private String availability;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name = "locality")
    private String locality;

    @Column(name = "address")
    private String address;

    @Column(name = "nearby_place")
    private String nearbyPlace;

    @Column(name = "admin_approved")
    private String adminApproved;

    @Column(name = "expired")
    private Boolean expired;

    @Column(name = "sold")
    private Boolean sold;

    @Column(name = "vip")
    private Boolean vip;

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

    // ===== AMENITIES =====
    @Column(name = "elevator")
    private Boolean elevator;
    @Column(name = "water24x7")
    private Boolean water24x7;
    @Column(name = "gas_pipeline")
    private Boolean gasPipeline;
    @Column(name = "pet_friendly")
    private Boolean petFriendly;
    @Column(name = "emergency_exit")
    private Boolean emergencyExit;
    @Column(name = "wheelchair_friendly")
    private Boolean wheelchairFriendly;
    @Column(name = "vastu_compliant")
    private Boolean vastuCompliant;
    @Column(name = "pooja_room")
    private Boolean poojaRoom;
    @Column(name = "study_room")
    private Boolean studyRoom;
    @Column(name = "servant_room")
    private Boolean servantRoom;
    @Column(name = "store_room")
    private Boolean storeRoom;
    @Column(name = "modular_kitchen")
    private Boolean modularKitchen;
    @Column(name = "high_ceiling_height")
    private Boolean highCeilingHeight;
    @Column(name = "park")
    private Boolean park;
    @Column(name = "swimming_pool")
    private Boolean swimmingPool;
    @Column(name = "gym")
    private Boolean gym;
    @Column(name = "clubhouse_community_center")
    private Boolean clubhouseCommunityCenter;
    @Column(name = "municipal_corporation")
    private Boolean municipalCorporation;
    @Column(name = "in_gated_society")
    private Boolean inGatedSociety;
    @Column(name = "corner_property")
    private Boolean cornerProperty;

    public String getGlobalId() {
        return globalId;
    }

    public Integer getListingId() {
        return listingId;
    }

    public String getCategory() {
        return category;
    }

    public String getPreference() {
        return preference;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getPrice() {
        return price;
    }

    public Double getArea() {
        return area;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public Integer getBathrooms() {
        return bathrooms;
    }

    public Integer getCabins() {
        return cabins;
    }

    public String getFurnishing() {
        return furnishing;
    }

    public String getAge() {
        return age;
    }

    public String getAvailability() {
        return availability;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getLocality() {
        return locality;
    }

    public String getAddress() {
        return address;
    }

    public String getAdminApproved() {
        return adminApproved;
    }

    public Boolean getExpired() {
        return expired;
    }

    public Boolean getSold() {
        return sold;
    }

    public Boolean getVip() {
        return vip;
    }

    public Boolean getReraVerified() {
        return reraVerified;
    }

    public String getReraNumber() {
        return reraNumber;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getApprovedAt() {
        return approvedAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageObjectName() {
        return imageObjectName;
    }

    public Boolean getElevator() {
        return elevator;
    }

    public Boolean getWater24x7() {
        return water24x7;
    }

    public Boolean getGasPipeline() {
        return gasPipeline;
    }

    public Boolean getPetFriendly() {
        return petFriendly;
    }

    public Boolean getEmergencyExit() {
        return emergencyExit;
    }

    public Boolean getWheelchairFriendly() {
        return wheelchairFriendly;
    }

    public Boolean getVastuCompliant() {
        return vastuCompliant;
    }

    public Boolean getPoojaRoom() {
        return poojaRoom;
    }

    public Boolean getStudyRoom() {
        return studyRoom;
    }

    public Boolean getServantRoom() {
        return servantRoom;
    }

    public Boolean getStoreRoom() {
        return storeRoom;
    }

    public Boolean getModularKitchen() {
        return modularKitchen;
    }

    public Boolean getHighCeilingHeight() {
        return highCeilingHeight;
    }

    public Boolean getPark() {
        return park;
    }

    public Boolean getSwimmingPool() {
        return swimmingPool;
    }

    public Boolean getGym() {
        return gym;
    }

    public Boolean getClubhouseCommunityCenter() {
        return clubhouseCommunityCenter;
    }

    public Boolean getMunicipalCorporation() {
        return municipalCorporation;
    }

    public Boolean getInGatedSociety() {
        return inGatedSociety;
    }

    public Boolean getCornerProperty() {
        return cornerProperty;
    }

    public Boolean getMeetingRoom() {
        return meetingRoom;
    }

    public Boolean getWashroom() {
        return washroom;
    }

    public String getNearbyPlace() {
        return nearbyPlace;
    }

}

