package com.propadda.prop.mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.propadda.prop.dto.MediaResponse;
import com.propadda.prop.model.ResidentialPropertyMedia;

public class ResidentialMediaMapper {
    // Helper for ResidentialPropertyMedia
    public static MediaResponse toDto(ResidentialPropertyMedia entity) {
        if (entity == null) {
            return null;
        }

        MediaResponse dto = new MediaResponse();
        dto.setUrl(entity.getUrl());
        dto.setFilename(entity.getFilename());
        dto.setOrd(entity.getOrd()); // Assuming getOrd() is available
        return dto;
    }

    // Maps a list of ResidentialPropertyMedia entities to a list of MediaResponse DTOs
    public static List<MediaResponse> toDtoList(List<ResidentialPropertyMedia> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(ResidentialMediaMapper::toDto)
                .collect(Collectors.toList());
    }
    
    // NOTE: You can combine this with the previous Commercial Media Mapper 
    // by making a single static method that takes a common Media interface/superclass 
    // or by making the media mapping logic generic in your shared utility class.
}
