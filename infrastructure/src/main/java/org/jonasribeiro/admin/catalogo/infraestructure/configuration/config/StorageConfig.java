package org.jonasribeiro.admin.catalogo.infraestructure.configuration.config;

import com.google.cloud.storage.Storage;
import org.jonasribeiro.admin.catalogo.infraestructure.configuration.properties.google.GoogleStorageProperties;
import org.jonasribeiro.admin.catalogo.infraestructure.configuration.properties.storage.StorageProperties;
import org.jonasribeiro.admin.catalogo.infraestructure.services.StorageService;
import org.jonasribeiro.admin.catalogo.infraestructure.services.impl.GCStorageService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class StorageConfig {

    @Bean
    @ConfigurationProperties(value = "storage.catalogo-videos")
    public StorageProperties storageProperties() {
        return new StorageProperties();
    }

    @Bean
    @Profile({"development", "production", "sandbox"})
    public StorageService gcStorageService(
            final Storage storage,
            final GoogleStorageProperties props
    ) {
        return new GCStorageService(props.getBucket(), storage);
    }
}