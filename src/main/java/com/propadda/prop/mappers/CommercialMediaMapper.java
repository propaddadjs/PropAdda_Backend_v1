package com.propadda.prop.mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.propadda.prop.dto.MediaResponse;
import com.propadda.prop.model.CommercialPropertyMedia;

public class CommercialMediaMapper {

    // Maps a single CommercialPropertyMedia entity to a MediaResponse DTO
    public static MediaResponse toDto(CommercialPropertyMedia entity) {
        if (entity == null) {
            return null;
        }

        MediaResponse dto = new MediaResponse();
        dto.setUrl(entity.getUrl());
        dto.setFilename(entity.getFilename());
        dto.setOrd(entity.getOrd()); // Assuming CommercialPropertyMedia has getOrd()
        return dto;
    }

    // Maps a list of CommercialPropertyMedia entities to a list of MediaResponse DTOs
    public static List<MediaResponse> toDtoList(List<CommercialPropertyMedia> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(CommercialMediaMapper::toDto)
                .collect(Collectors.toList());
    }
}
