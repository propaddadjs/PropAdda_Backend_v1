package com.propadda.prop.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.propadda.prop.enumerations.Kyc;
import com.propadda.prop.enumerations.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "propadda_users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name = "password")
    private String password;

    @Column(name = "reset_token")
    private String resetToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "kyc_verified", nullable=false)
    private Kyc kycVerified = Kyc.INAPPLICABLE;

    @Column(name = "rera_number")
    private String agentReraNumber;

    @Column(name = "propadda_verified")
    private Boolean propaddaVerified = false;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable=false)
    private Role role = Role.BUYER; // default when registering

    @Column(name = "aadhar_url")
    private String aadharUrl;     

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @OneToMany(mappedBy="residentialOwner", cascade=CascadeType.ALL, orphanRemoval=true)
    @JsonManagedReference
    private List<ResidentialPropertyDetails> residentialPropertyDetails;

    @OneToMany(mappedBy="commercialOwner", cascade=CascadeType.ALL, orphanRemoval=true)
    @JsonManagedReference
    private List<CommercialPropertyDetails> commercialPropertyDetails;

    @OneToMany(mappedBy="enquiriesByBuyer", cascade=CascadeType.ALL, orphanRemoval=true)
    @JsonManagedReference
    private List<EnquiredListingsDetails> enquiredListingsDetails;

    @OneToMany(mappedBy="favoritesOfBuyer", cascade=CascadeType.ALL, orphanRemoval=true)
    @JsonManagedReference
    private List<FavoriteListingsDetails> favoriteListingsDetails;

    @OneToMany(mappedBy="feedbackUser", cascade=CascadeType.ALL, orphanRemoval=true)
    @JsonManagedReference
    private List<FeedbackDetails> feedbackDetails;

    @OneToMany(mappedBy="helpUser", cascade=CascadeType.ALL, orphanRemoval=true)
    @JsonManagedReference
    private List<HelpDetails> helpDetails;

    public Users() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ResidentialPropertyDetails> getResidentialPropertyDetails() {
        return residentialPropertyDetails;
    }

    public void setResidentialPropertyDetails(List<ResidentialPropertyDetails> residentialPropertyDetails) {
        this.residentialPropertyDetails = residentialPropertyDetails;
    }

    public List<CommercialPropertyDetails> getCommercialPropertyDetails() {
        return commercialPropertyDetails;
    }

    public void setCommercialPropertyDetails(List<CommercialPropertyDetails> commercialPropertyDetails) {
        this.commercialPropertyDetails = commercialPropertyDetails;
    }

    public Kyc getKycVerified() {
        return kycVerified;
    }

    public void setKycVerified(Kyc kycVerified) {
        this.kycVerified = kycVerified;
    }
    public Boolean getPropaddaVerified() {
        return propaddaVerified;
    }

    public void setPropaddaVerified(Boolean propaddaVerified) {
        this.propaddaVerified = propaddaVerified;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getAgentReraNumber() {
        return agentReraNumber;
    }

    public void setAgentReraNumber(String agentReraNumber) {
        this.agentReraNumber = agentReraNumber;
    }

    public String getAadharUrl() {
        return aadharUrl;
    }

    public void setAadharUrl(String aadharUrl) {
        this.aadharUrl = aadharUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public List<EnquiredListingsDetails> getEnquiredListingsDetails() {
        return enquiredListingsDetails;
    }

    public void setEnquiredListingsDetails(List<EnquiredListingsDetails> enquiredListingsDetails) {
        this.enquiredListingsDetails = enquiredListingsDetails;
    }

    public List<FavoriteListingsDetails> getFavoriteListingsDetails() {
        return favoriteListingsDetails;
    }

    public void setFavoriteListingsDetails(List<FavoriteListingsDetails> favoriteListingsDetails) {
        this.favoriteListingsDetails = favoriteListingsDetails;
    }

    public List<FeedbackDetails> getFeedbackDetails() {
        return feedbackDetails;
    }

    public void setFeedbackDetails(List<FeedbackDetails> feedbackDetails) {
        this.feedbackDetails = feedbackDetails;
    }

    public List<HelpDetails> getHelpDetails() {
        return helpDetails;
    }

    public void setHelpDetails(List<HelpDetails> helpDetails) {
        this.helpDetails = helpDetails;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
