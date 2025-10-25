// Author-Hemant Arora
package com.propadda.prop.service;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.CopyRequest;
import com.google.cloud.storage.Storage.SignUrlOption;
import com.google.cloud.storage.StorageOptions;

@Service
public class GcsService {
    private final Storage storage;
    private final String bucket;
    private final long expirySeconds;
    private final String shareBucket;

    public GcsService(@Value("${gcs.bucket}") String bucket,
    @Value("${gcs.signed-url-expiry-seconds:300}") long expirySeconds,
    @Value("${gcs.share-bucket}") String shareBucket) {
        this.storage = StorageOptions.getDefaultInstance().getService();
        this.bucket = bucket;
        this.expirySeconds = expirySeconds;
        this.shareBucket = shareBucket;
    }

    public Storage getStorage() {
        return this.storage;
    }
    public String getBucketName() {
        return this.bucket;
    }

    public String uploadFile(MultipartFile file, String propertyType) throws IOException {
        
        String blobName;
        if(propertyType.equalsIgnoreCase("residential"))
            blobName = "uploads/residential/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        else
            blobName = "uploads/commercial/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        BlobId blobId = BlobId.of(bucket, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .setContentDisposition("attachment; filename=\"" + file.getOriginalFilename() + "\"")
                .build();

        storage.create(blobInfo, file.getBytes());

        // Option A: Signed URL (expires after given duration)
        URL signedUrl = storage.signUrl(blobInfo, 365, TimeUnit.DAYS);
        return signedUrl.toString();
    }

    // public String uploadKYCFiles(MultipartFile file, String fileType) throws IOException {
        
    //     String blobName;
    //         if(fileType.equalsIgnoreCase("aadhar")){
    //             blobName = "uploads/KYC/aadhar/" + UUID.randomUUID() + "-" + file.getOriginalFilename();}
    //         else
    //         if(fileType.equalsIgnoreCase("profileImage")){
    //             blobName = "uploads/KYC/profile/" + UUID.randomUUID() + "-" + file.getOriginalFilename();}
    //         else{
    //             blobName = null;}

    //     BlobId blobId = BlobId.of(bucket, blobName);
    //     BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
    //             .setContentType(file.getContentType())
    //             .setContentDisposition("attachment; filename=\"" + file.getOriginalFilename() + "\"")
    //             .build();

    //     storage.create(blobInfo, file.getBytes());

    //     // Option A: Signed URL (expires after given duration)
    //     URL signedUrl = storage.signUrl(blobInfo, 365, TimeUnit.DAYS);
    //     return signedUrl.toString();
        
    //     // Option B: If bucket is public, you can directly return public URL:
    //     // return String.format("https://storage.googleapis.com/%s/%s", bucket, blobName);

    // }

    public String uploadKYCFiles(MultipartFile file, String fileType) throws IOException {
        String blobName;
        if (fileType.equalsIgnoreCase("aadhar")) {
            blobName = "uploads/KYC/aadhar/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        } else if (fileType.equalsIgnoreCase("profileImage")) {
            blobName = "uploads/KYC/profile/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        } else {
            throw new IllegalArgumentException("Unknown fileType: " + fileType);
        }

        BlobId blobId = BlobId.of(bucket, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .setContentDisposition("attachment; filename=\"" + file.getOriginalFilename() + "\"")
                .build();

        storage.create(blobInfo, file.getBytes());

        // return the object name, store this in DB
        return blobName;
    }

    public String generateSignedUrl(String blobName) {
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucket, blobName)).build();
        URL signedUrl = storage.signUrl(
            blobInfo,
            15, TimeUnit.MINUTES,
            SignUrlOption.withV4Signature(),
            SignUrlOption.httpMethod(HttpMethod.GET)
        );
        return signedUrl.toString();
    }

    /**
     * Create a V4 signed POST URL that the browser can call with header:
     *   x-goog-resumable: start
     * The POST returns a Location header containing the resumable session URI.
     */
    public SignedUrlInfo generateResumableStartSignedUrl(String objectName, String contentType, long expirySeconds) {
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucket, objectName))
                .setContentType(contentType)
                .build();

        // Required: include the resumable header in the signature so the browser can send it.
        Map<String, String> extHeaders = Map.of("x-goog-resumable", "start");

        URL signedUrl = storage.signUrl(
                blobInfo,
                expirySeconds,
                TimeUnit.SECONDS,
                SignUrlOption.httpMethod(HttpMethod.POST),
                SignUrlOption.withV4Signature(),
                SignUrlOption.withExtHeaders(extHeaders),
                SignUrlOption.withContentType()
        );

        String publicUrl = String.format("https://storage.googleapis.com/%s/%s", bucket, objectName);
        return new SignedUrlInfo(signedUrl.toString(), publicUrl);
    }

    public String generateV4GetSignedUrl(String objectName) {
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucket, objectName)).build();
        URL signed = storage.signUrl(blobInfo, 7, TimeUnit.DAYS,
                SignUrlOption.withV4Signature(),
                SignUrlOption.httpMethod(HttpMethod.GET));
        return signed.toString();
    }

    public void moveObject(String sourceObject, String destinationObject) {
        BlobId source = BlobId.of(bucket, sourceObject);
        BlobId target = BlobId.of(bucket, destinationObject);

        // Copy
        storage.copy(com.google.cloud.storage.Storage.CopyRequest.newBuilder()
                .setSource(source)
                .setTarget(BlobInfo.newBuilder(target).build())
                .build());

        // Delete source
        storage.delete(source);
    }

    public void deleteFile(String fileUrl) {
        // Parse blob name from URL
        String blobName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        storage.delete(bucket, blobName);
    }

    public void deletePrefix(String prefix) {
        Page<Blob> blobs = storage.list(bucket, Storage.BlobListOption.prefix(prefix));
        List<BlobId> ids = new ArrayList<>();
        for (Blob b : blobs.iterateAll()) {
            ids.add(b.getBlobId());
        }
        if (!ids.isEmpty()) storage.delete(ids);
    }

    public SignedUrlInfo generateV4UploadSignedUrl(String objectName, String contentType) {
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucket, objectName)).setContentType(contentType).build();
        URL signedUrl = storage.signUrl(blobInfo, expirySeconds, TimeUnit.SECONDS,
                Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
                Storage.SignUrlOption.withV4Signature());
        String publicUrl = String.format("https://storage.googleapis.com/%s/%s", bucket, objectName);
        return new SignedUrlInfo(signedUrl.toString(), publicUrl);
    }

    public static class SignedUrlInfo {
        public final String signedUrl;
        public final String publicUrl;
        public SignedUrlInfo(String signedUrl, String publicUrl) {
            this.signedUrl = signedUrl; this.publicUrl = publicUrl;
        }
    }

    public String uploadShareImage(MultipartFile file, Integer agentId) throws IOException {
        String blobName = "shares/agent-" + agentId + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        BlobId blobId = BlobId.of(shareBucket, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        // Upload bytes
        storage.create(blobInfo, file.getBytes());

        // Make the object public to everyone (read)
        storage.createAcl(blobId, Acl.of(User.ofAllUsers(), Role.READER));

        // Public URL (works well for social platforms)
        return "https://storage.googleapis.com/" + shareBucket + "/" + blobName;
    }

    /**
     * Copy an object from a source bucket/object into propadda_share under profile/agent-<id>/...
     * Returns public URL of the copied object.
     */
    public String copyProfileToShareBucket(String sourceBucket, String sourceObject, Integer agentId) {
        // Create destination name
        String filename = Paths.get(sourceObject).getFileName().toString();
        String destName = String.format("profile/agent-%d/%s-%s", agentId, UUID.randomUUID(), filename);

        // Copy request
        CopyRequest copyRequest = CopyRequest.newBuilder()
            .setSource(sourceBucket, sourceObject)
            .setTarget(BlobInfo.newBuilder(BlobId.of(shareBucket, destName)).build())
            .build();

        storage.copy(copyRequest);

        // Make public read
        BlobId targetBlobId = BlobId.of(shareBucket, destName);
        storage.createAcl(targetBlobId, Acl.of(User.ofAllUsers(), Role.READER));

        // Return public URL
        return "https://storage.googleapis.com/" + shareBucket + "/" + destName;
    }
}
