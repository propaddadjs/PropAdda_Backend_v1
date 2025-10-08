package com.propadda.prop.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "commercial_property_media")
public class CommercialPropertyMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "media_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @Column(name = "url", columnDefinition = "text", nullable = false)
    private String url;

    @Column(name = "filename")
    private String filename;

    @Column(name = "ord")
    private Integer ord;

    @Column(name = "size")
    private Long size;

    @Column(name = "uploaded_at")
    private Instant uploadedAt;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="listing_id")
    @JsonBackReference
    private CommercialPropertyDetails property;

    public CommercialPropertyMedia() {
        this.uploadedAt = Instant.now();
    }

    public enum MediaType {
        IMAGE, VIDEO, BROCHURE, OTHER
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getOrd() {
        return ord;
    }

    public void setOrd(Integer ord) {
        this.ord = ord;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Instant uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public CommercialPropertyDetails getProperty() {
        return property;
    }

    public void setProperty(CommercialPropertyDetails property) {
        this.property = property;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CommercialPropertyMedia{");
        sb.append("id=").append(id);
        sb.append(", mediaType=").append(mediaType);
        sb.append(", url=").append(url);
        sb.append(", filename=").append(filename);
        sb.append(", ord=").append(ord);
        sb.append(", size=").append(size);
        sb.append(", uploadedAt=").append(uploadedAt);
        sb.append(", property=").append(property);
        sb.append('}');
        return sb.toString();
    }

}

