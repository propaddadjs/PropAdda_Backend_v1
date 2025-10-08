package com.propadda.prop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.propadda.prop.enumerations.HelpCategory;

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
@Table(name = "help_details")
public class HelpDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="help_id")
    private Integer helpId;

    @Enumerated(EnumType.STRING)
    @Column(name="help_category")
    private HelpCategory helpCategory;

    @Column(name="help_subcategory")
    private String helpSubcategory;

    @Column(name="help_detail")
    private String helpDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private Users helpUser;

    public Integer getHelpId() {
        return helpId;
    }

    public void setHelpId(Integer helpId) {
        this.helpId = helpId;
    }

    public HelpCategory getHelpCategory() {
        return helpCategory;
    }

    public void setHelpCategory(HelpCategory helpCategory) {
        this.helpCategory = helpCategory;
    }

    public String getHelpSubcategory() {
        return helpSubcategory;
    }

    public void setHelpSubcategory(String helpSubcategory) {
        this.helpSubcategory = helpSubcategory;
    }

    public String getHelpDetail() {
        return helpDetail;
    }

    public void setHelpDetail(String helpDetail) {
        this.helpDetail = helpDetail;
    }

    public Users getHelpUser() {
        return helpUser;
    }

    public void setHelpUser(Users helpUser) {
        this.helpUser = helpUser;
    }
    
}
