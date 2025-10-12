package com.propadda.prop.dto;

public class MediaProductionGraphicsRequest {
    
    private Boolean graphics;
    private String propertyCategory;
    private Integer propertyId;

    public Boolean getGraphics() {
        return graphics;
    }
    public void setGraphics(Boolean graphics) {
        this.graphics = graphics;
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
