// Author-Hemant Arora
package com.propadda.prop.dto;

import java.util.List;

public class DetailedFilterRequest {
    public String category;
    public List<String> propertyType;
    public String preference;
    public Integer priceMin;
    public Integer priceMax;
    public String furnishing;
    public String state;
    public String city;
    public List<String> amenities;
    public String availability;
    public Double areaMin;
    public Double areaMax;
    public List<String> age;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DetailedFilterRequest{");
        sb.append("category=").append(category);
        sb.append(", propertyType=").append(propertyType);
        sb.append(", preference=").append(preference);
        sb.append(", priceMin=").append(priceMin);
        sb.append(", priceMax=").append(priceMax);
        sb.append(", furnishing=").append(furnishing);
        sb.append(", state=").append(state);
        sb.append(", city=").append(city);
        sb.append(", amenities=").append(amenities);
        sb.append(", availability=").append(availability);
        sb.append(", areaMin=").append(areaMin);
        sb.append(", areaMax=").append(areaMax);
        sb.append(", age=").append(age);
        sb.append('}');
        return sb.toString();
    }


}
