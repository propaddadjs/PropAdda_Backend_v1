package com.propadda.prop.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.propadda.prop.dto.UploadSessionFileRequest;
import com.propadda.prop.dto.UploadSessionFileResponse;
import com.propadda.prop.dto.UploadSessionRequest;
import com.propadda.prop.dto.UploadSessionResponse;
import com.propadda.prop.model.UploadSession;
import com.propadda.prop.repo.UploadSessionRepository;
import com.propadda.prop.service.GcsResumableService;
import com.propadda.prop.service.GcsService;
import com.propadda.prop.service.GcsService.SignedUrlInfo;

@RestController
@RequestMapping("/api/uploads")
public class UploadSessionController {

    @Autowired
    private GcsResumableService gcsResumableService;

    @Autowired
    private GcsService gcsService;

    @Autowired
    private UploadSessionRepository uploadSessionRepository;

    // @PostMapping("/sessions")
    // public ResponseEntity<UploadSessionResponse> createSession(@RequestBody UploadSessionRequest req) throws Exception {
    //     Instant expiresAt = Instant.now().plus(2, ChronoUnit.HOURS); // configurable
    //     UploadSession session = new UploadSession(req.userId, expiresAt);
    //     uploadSessionRepository.save(session);

    //     List<UploadSessionFileResponse> fileResponses = new ArrayList<>();
    //     int idx = 0;
    //     for (UploadSessionFileRequest f : req.files) {
    //         String objectName = gcsResumableService.buildTempObjectName(session.getUploadId(), f.name);
    //         String sessionUrl = gcsResumableService.startResumableSession(objectName, f.contentType, f.size);
    //         UploadSessionFileResponse fr = new UploadSessionFileResponse();
    //         fr.fileIndex = idx++;
    //         fr.objectName = objectName;
    //         fr.sessionUrl = sessionUrl;
    //         fr.contentType = f.contentType;
    //         fileResponses.add(fr);
    //     }

    //     UploadSessionResponse resp = new UploadSessionResponse();
    //     resp.uploadId = session.getUploadId();
    //     resp.files = fileResponses;
    //     return ResponseEntity.ok(resp);
    // }

    @PostMapping("/sessions")
    public ResponseEntity<UploadSessionResponse> createSession(@RequestBody UploadSessionRequest req) throws Exception {
        Instant expiresAt = Instant.now().plus(2, ChronoUnit.HOURS);
        UploadSession session = new UploadSession(req.userId, expiresAt);
        uploadSessionRepository.save(session);

        List<UploadSessionFileResponse> fileResponses = new ArrayList<>();
        int idx = 0;
        for (UploadSessionFileRequest f : req.files) {
            String objectName = gcsResumableService.buildTempObjectName(session.getUploadId(), f.name);

            // generate a signed POST URL that the browser can call with header "x-goog-resumable: start"
            long startUrlExpiry = 5 * 60; // 5 minutes
            SignedUrlInfo signed = gcsService.generateResumableStartSignedUrl(objectName, f.contentType, startUrlExpiry);

            UploadSessionFileResponse fr = new UploadSessionFileResponse();
            fr.fileIndex = idx++;
            fr.objectName = objectName;
            fr.sessionUrl = signed.signedUrl; // here sessionUrl is actually the "startUrl" (POST signed URL)
            fr.contentType = f.contentType;
            fileResponses.add(fr);
        }

        UploadSessionResponse resp = new UploadSessionResponse();
        resp.uploadId = session.getUploadId();
        resp.files = fileResponses;
        return ResponseEntity.ok(resp);
    }
}