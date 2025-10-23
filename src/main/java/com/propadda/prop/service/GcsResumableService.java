package com.propadda.prop.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class GcsResumableService {

    private final Storage storage;
    private final String bucket;
    private final String tempPrefix; // e.g. "temp"

    public GcsResumableService(@Value("${gcs.bucket}") String bucket,
                               @Value("${gcs.temp-prefix:temp}") String tempPrefix) {
        this.storage = StorageOptions.getDefaultInstance().getService();
        this.bucket = bucket;
        this.tempPrefix = tempPrefix;
    }

    /**
     * Start a resumable upload session for objectName in bucket.
     * Returns the session URI (Location header) that the browser will PUT chunks to.
     */
    public String startResumableSession(String objectName, String contentType, Long totalSize) throws IOException {
        // Use application default credentials to obtain an OAuth access token
        GoogleCredentials creds = GoogleCredentials.getApplicationDefault()
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/devstorage.full_control"));
        creds.refreshIfExpired();
        String accessToken = creds.getAccessToken().getTokenValue();

        String url = String.format("https://www.googleapis.com/upload/storage/v1/b/%s/o?uploadType=resumable&name=%s",
                URLEncoder.encode(bucket, StandardCharsets.UTF_8),
                URLEncoder.encode(objectName, StandardCharsets.UTF_8));

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("X-Upload-Content-Type", contentType);
        if (totalSize != null) {
            conn.setRequestProperty("X-Upload-Content-Length", String.valueOf(totalSize));
        }
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        // Optionally send metadata as JSON (empty is fine)
        try (OutputStream os = conn.getOutputStream()) {
            os.write("{}".getBytes(StandardCharsets.UTF_8));
        }

        int code = conn.getResponseCode();
        if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_CREATED) {
            String sessionUri = conn.getHeaderField("Location");
            if (sessionUri == null) {
                throw new IOException("No Location header returned for resumable session");
            }
            return sessionUri;
        } else {
            String resp = readStream(conn.getErrorStream());
            throw new IOException("Failed to start resumable session: " + code + " - " + resp);
        }
    }

    private String readStream(InputStream is) throws IOException {
        if (is == null) return "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }

    /**
     * Verify a blob exists and return its metadata (size, contentType). Returns null if not found.
     */
    public Blob getBlobMetadata(String objectName) {
        BlobId id = BlobId.of(bucket, objectName);
        return storage.get(id, Storage.BlobGetOption.fields(Storage.BlobField.SIZE, Storage.BlobField.CONTENT_TYPE));
    }

    /**
     * List and delete all objects under the given prefix (e.g. "temp/{uploadId}/")
     */
    public void deletePrefix(String prefix) {
        Page<Blob> blobs = storage.list(bucket, Storage.BlobListOption.prefix(prefix));
        List<BlobId> ids = new ArrayList<>();
        for (Blob b : blobs.iterateAll()) {
            ids.add(b.getBlobId());
        }
        if (!ids.isEmpty()) {
            storage.delete(ids);
        }
    }

    /**
     * Helper: generate temp object name for upload
     */
    public String buildTempObjectName(String uploadId, String originalFilename) {
        String sanitized = originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");
        return String.format("%s/%s/%s-%s", tempPrefix, uploadId, UUID.randomUUID().toString(), sanitized);
    }
}