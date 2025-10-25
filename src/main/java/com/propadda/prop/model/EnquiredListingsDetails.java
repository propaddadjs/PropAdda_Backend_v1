// Author-Hemant Arora
package com.propadda.prop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.propadda.prop.enumerations.EnquiryStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "enquired_listings_details")
public class EnquiredListingsDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="enquiry_id")
    private Integer enquiryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private Users enquiriesByBuyer;

    @Column(name="property_category")
    private String propertyCategory;

    @Column(name="property_id")
    private Integer propertyId;

    @Column(name="buyer_name")
    private String buyerName;

    @Column(name="buyer_phone_number")
    private String buyerPhoneNumber;

    @Column(name="buyer_type")
    private String buyerType;

    @Column(name="buyer_reason")
    private String buyerReason;

    @Column(name="buyer_reason_detail", columnDefinition = "TEXT")
    private String buyerReasonDetail;

    @Column(name="enquiry_status")
    @Enumerated(EnumType.STRING)
    private EnquiryStatus enquiryStatus;

    public Integer getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(Integer enquiryId) {
        this.enquiryId = enquiryId;
    }

    public Users getEnquiriesByBuyer() {
        return enquiriesByBuyer;
    }

    public void setEnquiriesByBuyer(Users enquiriesByBuyer) {
        this.enquiriesByBuyer = enquiriesByBuyer;
    }

    public String getPropertyCategory() {
        return propertyCategory;
    }

    public void setPropertyCategory(String propertyCategory) {
        this.propertyCategory = propertyCategory;
    }

    public Integer getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Integer propertyId) {
        this.propertyId = propertyId;
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

    
}
