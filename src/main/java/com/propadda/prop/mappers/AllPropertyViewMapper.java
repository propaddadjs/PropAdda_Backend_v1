package com.propadda.prop.mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.propadda.prop.dto.AllPropertyViewResponse;
import com.propadda.prop.model.AllPropertyView;
import com.propadda.prop.model.AllPropertyViewFilter;
import com.propadda.prop.service.GcsService;

@Component
public class AllPropertyViewMapper {

    private final GcsService gcsService;

    public AllPropertyViewMapper(GcsService gcsService) {
        this.gcsService = gcsService;
    }

    public AllPropertyViewResponse toDto(AllPropertyView entity){
        if (entity == null) {
            return null;
        }
        AllPropertyViewResponse dto = new AllPropertyViewResponse();
        dto.setGlobalId(entity.getGlobalId());
        dto.setListingId(entity.getListingId());
        dto.setPreference(entity.getPreference());
        dto.setPropertyType(entity.getPropertyType());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setArea(entity.getArea());
        dto.setBedrooms(entity.getBedrooms());
        dto.setBathrooms(entity.getBathrooms());
        dto.setCabins(entity.getCabins());
        dto.setMeetingRoom(entity.getMeetingRoom());
        dto.setWashroom(entity.getWashroom());
        dto.setState(entity.getState());
        dto.setCity(entity.getCity());
        dto.setLocality(entity.getLocality());
        dto.setAddress(entity.getAddress());
        dto.setAdminApproved(entity.getAdminApproved());
        dto.setExpired(entity.getExpired());
        dto.setVip(entity.getVip());
        dto.setSold(entity.getSold());
        dto.setCategory(entity.getCategory());
        dto.setReraVerified(entity.getReraVerified());
        dto.setReraNumber(entity.getReraNumber());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setApprovedAt(entity.getApprovedAt());
        dto.setUserId(entity.getUserId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());

        if(entity.getImageObjectName()==null){
            dto.setMediaUrl(entity.getImageUrl());
        } else {
            dto.setMediaUrl(
                gcsService.generateV4GetSignedUrl(entity.getImageObjectName())
            );
        }
        return dto;
    }

    public List<AllPropertyViewResponse> toDtoList(List<AllPropertyView> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public AllPropertyViewResponse toDtoFiltered(AllPropertyViewFilter entity){
        if (entity == null) {
            return null;
        }
        AllPropertyViewResponse dto = new AllPropertyViewResponse();
        dto.setGlobalId(entity.getGlobalId());
        dto.setListingId(entity.getListingId());
        dto.setPreference(entity.getPreference());
        dto.setPropertyType(entity.getPropertyType());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setArea(entity.getArea());
        dto.setBedrooms(entity.getBedrooms());
        dto.setBathrooms(entity.getBathrooms());
        dto.setCabins(entity.getCabins());
        dto.setMeetingRoom(entity.getMeetingRoom());
        dto.setWashroom(entity.getWashroom());
        dto.setState(entity.getState());
        dto.setCity(entity.getCity());
        dto.setLocality(entity.getLocality());
        dto.setAddress(entity.getAddress());
        dto.setAdminApproved(entity.getAdminApproved());
        dto.setExpired(entity.getExpired());
        dto.setVip(entity.getVip());
        dto.setSold(entity.getSold());
        dto.setCategory(entity.getCategory());
        dto.setReraVerified(entity.getReraVerified());
        dto.setReraNumber(entity.getReraNumber());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setApprovedAt(entity.getApprovedAt());
        dto.setUserId(entity.getUserId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());

        if(entity.getImageObjectName()==null){
            dto.setMediaUrl(entity.getImageUrl());
        } else {
            dto.setMediaUrl(
                gcsService.generateV4GetSignedUrl(entity.getImageObjectName())
            );
        }
        return dto;
    }

    public List<AllPropertyViewResponse> toDtoFilteredList(List<AllPropertyViewFilter> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDtoFiltered)
                .collect(Collectors.toList());
    }
}