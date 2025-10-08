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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.propadda.prop.dto.CommercialPropertyRequest;
import com.propadda.prop.model.CommercialPropertyDetails;
import com.propadda.prop.security.CustomUserDetails;
import com.propadda.prop.service.CommercialPropertyDetailsService;

@RestController
@PreAuthorize("hasAnyRole('AGENT','ADMIN') and @kycGuard.isApproved(authentication)")
@RequestMapping("/commercial-properties")
public class CommercialPropertyDetailsController {

    private final CommercialPropertyDetailsService service;

    public CommercialPropertyDetailsController(CommercialPropertyDetailsService service) {
        this.service = service;
    }

    @PostMapping(value="/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommercialPropertyDetails> createProperty(
            @RequestPart("property") CommercialPropertyRequest property, @RequestPart("files") List<MultipartFile> files) throws IOException {
        System.out.println("Received DTO: " + property);
        return ResponseEntity.ok(service.saveProperty(property,files));
    }

    @PutMapping(value="/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateProperty(
            @RequestPart("property") CommercialPropertyRequest property, @RequestPart(value="files", required = false) List<MultipartFile> files, @AuthenticationPrincipal CustomUserDetails cud) throws IOException {
                Integer agentId = cud.getUser().getUserId();
        System.out.println("Received DTO: " + property);
        return ResponseEntity.ok(service.updateProperty(property,files,agentId));
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
