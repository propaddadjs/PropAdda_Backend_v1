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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "residential_property_details")
public class ResidentialPropertyDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "listing_id", nullable = false)
    private Integer listingId;

    @Column(name="category", nullable = false)
    private String category;

    @Column(name="preference", nullable = false)
    private String preference;

    @Column(name = "property_type", nullable = false)
    private String propertyType;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name="price")
    private Integer price;

    @Column(name="maintenance")
    private Integer maintenance;

    @Column(name="area")
    private Double area;

    @Column(name="bedrooms")
    private Integer bedrooms;

    @Column(name="bathrooms")
    private Integer bathrooms;

    @Column(name="furnishing")
    private String furnishing;

    @Column(name="facing")
    private String facing;

    @Column(name="floor")
    private Integer floor;

    @Column(name="age")
    private String age;

    @Column(name="availability")
    private String availability;

    @Column(name = "rera_number")
    private String reraNumber;

    @Column(name = "rera_verified")
    private Boolean reraVerified;

    @Column(name="state")
    private String state;

    @Column(name="city")
    private String city;

    @Column(name="locality")
    private String locality;

    @Column(name="address")
    private String address;

    @Column(name="pincode")
    private Integer pincode;

    @Column(name="nearby_place")
    private String nearbyPlace;

    @Column(name = "total_floors")
    private Integer totalFloors;

    @Column(name = "security_deposit")
    private Integer securityDeposit;

    @Column(name="balconies")
    private Integer balconies;

    @Column(name = "power_backup")
    private String powerBackup;

    @Column(name="covered_parking")
    private Integer coveredParking ;

    @Column(name="open_parking")
    private Integer openParking ;

    @Column(name="admin_approved")
    private String adminApproved;

    @Column(name="expired")
    private Boolean expired;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="user_id")
    @JsonBackReference
    private Users residentialOwner;

    @Column(name="vip")
    private Boolean vip;

     @Column(name="sold")
    private Boolean sold;

    @OneToOne(mappedBy="residentialProperty", cascade=CascadeType.ALL, orphanRemoval=true)
    @JsonManagedReference
    private ResidentialPropertyAmenities amenities;
    
    @OneToMany(mappedBy="property", cascade=CascadeType.ALL, orphanRemoval=true)
    @JsonManagedReference
    private List<ResidentialPropertyMedia> residentialPropertyMediaFiles;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "approved_at")
    private OffsetDateTime approvedAt;

    // public ResidentialPropertyDetails(String address, String age, Double area, String availability, Integer balconies, Integer bathrooms, Integer bedrooms, String city, Integer coveredParking, String description, String facing, Integer floor, String furnishing, String locality, Integer maintenance, String nearbyPlace, Integer openParking, Integer pincode, String powerBackup, String preference, Integer price, String propertyType, String reraNumber, Boolean reraVerified, Integer securityDeposit, String state, String title, Integer totalFloors) {
    //     this.address = address;
    //     this.age = age;
    //     this.area = area;
    //     this.availability = availability;
    //     this.balconies = balconies;
    //     this.bathrooms = bathrooms;
    //     this.bedrooms = bedrooms;
    //     this.city = city;
    //     this.coveredParking = coveredParking;
    //     this.description = description;
    //     this.facing = facing;
    //     this.floor = floor;
    //     this.furnishing = furnishing;
    //     this.locality = locality;
    //     this.maintenance = maintenance;
    //     this.nearbyPlace = nearbyPlace;
    //     this.openParking = openParking;
    //     this.pincode = pincode;
    //     this.powerBackup = powerBackup;
    //     this.preference = preference;
    //     this.price = price;
    //     this.propertyType = propertyType;
    //     this.reraNumber = reraNumber;
    //     this.reraVerified = reraVerified;
    //     this.securityDeposit = securityDeposit;
    //     this.state = state;
    //     this.title = title;
    //     this.totalFloors = totalFloors;
    // }

    public ResidentialPropertyDetails() {
    }

    public ResidentialPropertyDetails(String address, String age, String adminApproved, Double area, String availability, Integer balconies, Integer bathrooms, Integer bedrooms, String city, Integer coveredParking, String description, Boolean expired, String facing, Integer floor, String furnishing, String locality, Integer maintenance, String nearbyPlace, Integer openParking, Integer pincode, String powerBackup, String preference, Integer price, String propertyType, String reraNumber, Boolean reraVerified, Integer securityDeposit, String state, String title, Integer totalFloors, Boolean vip) {
        this.address = address;
        this.age = age;
        this.adminApproved = adminApproved;
        this.area = area;
        this.availability = availability;
        this.balconies = balconies;
        this.bathrooms = bathrooms;
        this.bedrooms = bedrooms;
        this.city = city;
        this.coveredParking = coveredParking;
        this.description = description;
        this.expired = expired;
        this.facing = facing;
        this.floor = floor;
        this.furnishing = furnishing;
        this.locality = locality;
        this.maintenance = maintenance;
        this.nearbyPlace = nearbyPlace;
        this.openParking = openParking;
        this.pincode = pincode;
        this.powerBackup = powerBackup;
        this.preference = preference;
        this.price = price;
        this.propertyType = propertyType;
        this.reraNumber = reraNumber;
        this.reraVerified = reraVerified;
        this.securityDeposit = securityDeposit;
        this.state = state;
        this.title = title;
        this.totalFloors = totalFloors;
        this.vip = vip;
    }


    // Getters & Setters

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public List<ResidentialPropertyMedia> getResidentialPropertyMediaFiles() {
        return residentialPropertyMediaFiles;
    }

    public void setResidentialPropertyMediaFiles(List<ResidentialPropertyMedia> residentialPropertyMediaFiles) {
        this.residentialPropertyMediaFiles = residentialPropertyMediaFiles;
    }

    public ResidentialPropertyAmenities getAmenities() {
        return amenities;
    }

    public void setAmenities(ResidentialPropertyAmenities amenities) {
        this.amenities = amenities;
    }

    public Integer getOpenParking() {
        return openParking;
    }

    public void setOpenParking(Integer openParking) {
        this.openParking = openParking;
    }

    public Integer getCoveredParking() {
        return coveredParking;
    }

    public void setCoveredParking(Integer coveredParking) {
        this.coveredParking = coveredParking;
    }

    @Override
    public String toString() {
        return "ResidentialPropertyDetails [listingId=" + listingId + ", preference=" + preference + ", propertyType="
                + propertyType + ", title=" + title + ", description=" + description + ", price=" + price
                + ", maintenance=" + maintenance + ", area=" + area + ", bedrooms=" + bedrooms + ", bathrooms="
                + bathrooms + ", furnishing=" + furnishing + ", facing=" + facing + ", floor=" + floor + ", age=" + age
                + ", availability=" + availability + ", reraNumber=" + reraNumber + ", reraVerified=" + reraVerified
                + ", state=" + state + ", city=" + city + ", locality=" + locality + ", address=" + address
                + ", pincode=" + pincode + ", nearbyPlace=" + nearbyPlace + ", totalFloors=" + totalFloors
                + ", securityDeposit=" + securityDeposit + ", balconies=" + balconies + ", powerBackup=" + powerBackup
                + ", coveredParking=" + coveredParking + ", openParking=" + openParking + ", amenities=" + amenities
                + ", residentialPropertyMediaFiles=" + residentialPropertyMediaFiles + "]";
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

    public Users getResidentialOwner() {
        return residentialOwner;
    }

    public void setResidentialOwner(Users residentialOwner) {
        this.residentialOwner = residentialOwner;
    }

    public Boolean isVip() {
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
