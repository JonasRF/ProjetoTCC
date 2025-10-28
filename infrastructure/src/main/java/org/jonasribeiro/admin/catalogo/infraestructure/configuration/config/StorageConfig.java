package org.jonasribeiro.admin.catalogo.infraestructure.configuration.config;

import com.google.cloud.storage.Storage;
import org.jonasribeiro.admin.catalogo.infraestructure.configuration.properties.google.GoogleStorageProperties;
import org.jonasribeiro.admin.catalogo.infraestructure.configuration.properties.storage.StorageProperties;
import org.jonasribeiro.admin.catalogo.infraestructure.services.StorageService;
import org.jonasribeiro.admin.catalogo.infraestructure.services.impl.GCStorageService;
import org.jonasribeiro.admin.catalogo.infraestructure.services.local.InMemoryStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
    @Profile({"development", "test-integration", "test-e2e"})
    public StorageService localStorageAPI() {
        return new InMemoryStorageService();
    }

    @Bean
    @ConditionalOnMissingBean
    public StorageService gcStorageAPI(
            final GoogleStorageProperties props,
            final Storage storage
    ) {
        return new GCStorageService(props.getBucket(), storage);
    }
}
