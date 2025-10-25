// Author-Hemant Arora
package com.propadda.prop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "media_production")
public class MediaProduction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="media_production_id")
    private Integer mediaProductionId;

    @Column(name="requester_user_id")
    private Integer requesterUserId;

    @Column(name = "graphics")
    private Boolean graphics;

    @Column(name = "photoshoot")
    private Boolean photoshoot;

    @Column(name="property_category")
    private String propertyCategory;

    @Column(name="property_id")
    private Integer propertyId;

    public Integer getMediaProductionId() {
        return mediaProductionId;
    }

    public void setMediaProductionId(Integer mediaProductionId) {
        this.mediaProductionId = mediaProductionId;
    }

    public Integer getRequesterUserId() {
        return requesterUserId;
    }

    public void setRequesterUserId(Integer requesterUserId) {
        this.requesterUserId = requesterUserId;
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

      public Boolean getGraphics() {
        return graphics;
    }

    public void setGraphics(Boolean graphics) {
        this.graphics = graphics;
    }

    public Boolean getPhotoshoot() {
        return photoshoot;
    }

    public void setPhotoshoot(Boolean photoshoot) {
        this.photoshoot = photoshoot;
    }

    
}
