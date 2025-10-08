package com.propadda.prop.dto;

public class AgentResponse {

    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String state;
    private String city;
    private String role;
    private String profileImageUrl;
    private String agentReraNumber;
    private Boolean propaddaVerified;
    private String aadharUrl;
    private String kycVerified;
    private String address;

    public AgentResponse() {
    }

    public AgentResponse(String agentReraNumber, String city, String email, String firstName, String kycVerified, String lastName, String phoneNumber, Boolean propaddaVerified, String role, String state, Integer userId) {
        this.agentReraNumber = agentReraNumber;
        this.city = city;
        this.email = email;
        this.firstName = firstName;
        this.kycVerified = kycVerified;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.propaddaVerified = propaddaVerified;
        this.role = role;
        this.state = state;
        this.userId = userId;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getAgentReraNumber() {
        return agentReraNumber;
    }

    public void setAgentReraNumber(String agentReraNumber) {
        this.agentReraNumber = agentReraNumber;
    }

    public Boolean getPropaddaVerified() {
        return propaddaVerified;
    }

    public void setPropaddaVerified(Boolean propaddaVerified) {
        this.propaddaVerified = propaddaVerified;
    }

    public String getAadharUrl() {
        return aadharUrl;
    }

    public void setAadharUrl(String aadharUrl) {
        this.aadharUrl = aadharUrl;
    }

    public String getKycVerified() {
        return kycVerified;
    }

    public void setKycVerified(String kycVerified) {
        this.kycVerified = kycVerified;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
