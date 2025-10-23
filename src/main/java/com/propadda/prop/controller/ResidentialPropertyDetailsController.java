package com.propadda.prop.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.propadda.prop.dto.PropertyWithUploadedMediaRequest;
import com.propadda.prop.dto.ResidentialPropertyRequest;
import com.propadda.prop.model.ResidentialPropertyDetails;
import com.propadda.prop.security.CustomUserDetails;
import com.propadda.prop.service.ResidentialPropertyDetailsService;

import jakarta.mail.MessagingException;

@RestController
@PreAuthorize("hasAnyRole('AGENT','ADMIN') and @kycGuard.isApproved(authentication)")
@RequestMapping("/residential-properties")
public class ResidentialPropertyDetailsController {

    private final ResidentialPropertyDetailsService service;

    public ResidentialPropertyDetailsController(ResidentialPropertyDetailsService service) {
        this.service = service;
    }

    @PostMapping(value="/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResidentialPropertyDetails> createOrUpdateProperty(@RequestPart("property") ResidentialPropertyRequest property, @RequestPart(value="files", required = false) List<MultipartFile> files) throws IOException, MessagingException {
        System.out.println("Received DTO: " + property); // quick debug - prints all fields via toString()
        return ResponseEntity.ok(service.saveProperty(property,files));
    }

    @PostMapping(value = "/add/claimed", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResidentialPropertyDetails> createPropertyWithUploadedMedia(
            @RequestBody PropertyWithUploadedMediaRequest<ResidentialPropertyRequest> req)
            throws IOException, MessagingException {
        System.out.println("Create via uploaded media; uploadId=" + req.uploadId + ", mediaCount=" + (req.media == null ? 0 : req.media.size()));
        return ResponseEntity.ok(service.savePropertyWithUploadedObjects(req.property, req.media));
    }

    @PutMapping(value="/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateProperty(
            @RequestPart("property") ResidentialPropertyRequest property, @RequestPart(value="files", required = false) List<MultipartFile> files, @AuthenticationPrincipal CustomUserDetails cud) throws IOException, MessagingException {
        Integer agentId = cud.getUser().getUserId();
        System.out.println("Received DTO: " + property);
        return ResponseEntity.ok(service.updateProperty(property,files,agentId));
    }

    @PutMapping(value = "/update/claimed", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updatePropertyWithUploadedMedia(
            @RequestBody PropertyWithUploadedMediaRequest<ResidentialPropertyRequest> req,
            @AuthenticationPrincipal CustomUserDetails cud) throws IOException, MessagingException {
        Integer agentId = cud.getUser().getUserId();
        System.out.println("Update via uploaded media; uploadId=" + req.uploadId + ", mediaCount=" + (req.media == null ? 0 : req.media.size()));
        return ResponseEntity.ok(service.updatePropertyWithUploadedObjects(req.property, req.media, agentId));
    }

    @DeleteMapping("/deleteMedia/{listingId}")
    public ResponseEntity<Void> deletePropertyMedia(@PathVariable Integer listingId, @AuthenticationPrincipal CustomUserDetails cud) {
        Integer agentId = cud.getUser().getUserId();
        service.deletePropertyMedia(listingId, agentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteProperty/{listingId}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Integer listingId, @AuthenticationPrincipal CustomUserDetails cud) {
        Integer agentId = cud.getUser().getUserId();
        service.deleteProperty(listingId,agentId);
        return ResponseEntity.noContent().build();
    }

}
