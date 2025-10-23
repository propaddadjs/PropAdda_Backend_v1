package com.propadda.prop.dto;

public class UploadedMediaDto {
    public String mediaType; // IMAGE / VIDEO / BROCHURE / OTHER
    public String name;
    public String objectName; // path in bucket e.g. temp/{uploadId}/uuid-name.mp4
    public long size;
}