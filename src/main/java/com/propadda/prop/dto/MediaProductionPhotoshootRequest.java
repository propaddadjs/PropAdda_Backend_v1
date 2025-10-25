// Author-Hemant Arora
package com.propadda.prop.dto;

public class MediaProductionPhotoshootRequest {
    
    private Boolean photoshoot;
    private String propertyCategory;
    private Integer propertyId;

    public Boolean getPhotoshoot() {
        return photoshoot;
    }
    public void setPhotoshoot(Boolean photoshoot) {
        this.photoshoot = photoshoot;
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

}
