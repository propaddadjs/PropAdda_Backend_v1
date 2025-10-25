// Author-Hemant Arora
package com.propadda.prop.mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.propadda.prop.dto.MediaResponse;
import com.propadda.prop.model.CommercialPropertyMedia;
import com.propadda.prop.service.GcsService;

@Component
public class CommercialMediaMapper {

   private static GcsService gcsService;

    @Autowired
    public void setGcsService(GcsService s) {
        CommercialMediaMapper.gcsService = s;
    }

    // Maps a single CommercialPropertyMedia entity to a MediaResponse DTO
    public static MediaResponse toDto(CommercialPropertyMedia entity) {
        if (entity == null) {
            return null;
        }

        MediaResponse dto = new MediaResponse();
        if(entity.getObjectName()==null){
            dto.setUrl(entity.getUrl());
        } else {
            String signed = gcsService.generateV4GetSignedUrl(entity.getObjectName());
            dto.setUrl(signed);
        }
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
