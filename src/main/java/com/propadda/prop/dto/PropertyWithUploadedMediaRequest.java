package com.propadda.prop.dto;

import java.util.List;

public class PropertyWithUploadedMediaRequest<T> {
    public T property;               // ResidentialPropertyRequest or CommercialPropertyRequest
    public String uploadId;         // optional
    public List<UploadedMediaDto> media;
}
