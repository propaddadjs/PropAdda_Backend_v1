// Author-Hemant Arora
package com.propadda.prop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "feedback_details")
public class FeedbackDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="feedback_id")
    private Integer feedbackId;

    // @Enumerated(EnumType.STRING)
    @Column(name="feedback_category")
    private String feedbackCategory;

    @Column(name="feedback_subcategory")
    private String feedbackSubcategory;

    @Column(name="feedback_detail")
    private String feedbackDetail;

    @Column(name="rating")
    private Integer rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private Users feedbackUser;

    public Integer getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getFeedbackCategory() {
        return feedbackCategory;
    }

    public void setFeedbackCategory(String feedbackCategory) {
        this.feedbackCategory = feedbackCategory;
    }

    public String getFeedbackSubcategory() {
        return feedbackSubcategory;
    }

    public void setFeedbackSubcategory(String feedbackSubcategory) {
        this.feedbackSubcategory = feedbackSubcategory;
    }

    public String getFeedbackDetail() {
        return feedbackDetail;
    }

    public void setFeedbackDetail(String feedbackDetail) {
        this.feedbackDetail = feedbackDetail;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Users getFeedbackUser() {
        return feedbackUser;
    }

    public void setFeedbackUser(Users feedbackUser) {
        this.feedbackUser = feedbackUser;
    }

}
