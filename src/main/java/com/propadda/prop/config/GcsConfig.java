package com.propadda.prop.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

/**
 * Google Cloud Storage config.
 * Exposes a Storage bean built from application environment credentials.
 * Make sure GOOGLE_APPLICATION_CREDENTIALS points to a valid service-account JSON file
 * or provide credentials some other secure way.
 */
@Configuration
public class GcsConfig {

    @Bean
    public Storage storage() {
        // Uses application-default credentials (GOOGLE_APPLICATION_CREDENTIALS env var)
        return StorageOptions.getDefaultInstance().getService();
    }
}
