// Author-Hemant Arora
package com.propadda.prop.dto;

import com.propadda.prop.enumerations.EnquiryStatus;

public class LeadsResponse {

    private Integer enquiryId;
    private UserResponse user;
    private String buyerName;
    private String buyerPhoneNumber;
    private String buyerType;
    private String buyerReason;
    private String buyerReasonDetail;
    private EnquiryStatus enquiryStatus;

    private CommercialPropertyResponse comResponse;
    private ResidentialPropertyResponse resResponse;

    public Integer getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(Integer enquiryId) {
        this.enquiryId = enquiryId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerPhoneNumber() {
        return buyerPhoneNumber;
    }

    public void setBuyerPhoneNumber(String buyerPhoneNumber) {
        this.buyerPhoneNumber = buyerPhoneNumber;
    }

    public String getBuyerType() {
        return buyerType;
    }

    public void setBuyerType(String buyerType) {
        this.buyerType = buyerType;
    }

    public String getBuyerReason() {
        return buyerReason;
    }

    public void setBuyerReason(String buyerReason) {
        this.buyerReason = buyerReason;
    }

    public String getBuyerReasonDetail() {
        return buyerReasonDetail;
    }

    public void setBuyerReasonDetail(String buyerReasonDetail) {
        this.buyerReasonDetail = buyerReasonDetail;
    }

    public EnquiryStatus getEnquiryStatus() {
        return enquiryStatus;
    }

    public void setEnquiryStatus(EnquiryStatus enquiryStatus) {
        this.enquiryStatus = enquiryStatus;
    }

    public CommercialPropertyResponse getComResponse() {
        return comResponse;
    }

    public void setComResponse(CommercialPropertyResponse comResponse) {
        this.comResponse = comResponse;
    }

    public ResidentialPropertyResponse getResResponse() {
        return resResponse;
    }

    public void setResResponse(ResidentialPropertyResponse resResponse) {
        this.resResponse = resResponse;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
    
}
