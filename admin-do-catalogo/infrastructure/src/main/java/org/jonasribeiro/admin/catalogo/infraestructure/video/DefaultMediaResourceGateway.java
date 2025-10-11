package org.jonasribeiro.admin.catalogo.infraestructure.video;

import org.jonasribeiro.admin.catalogo.domain.resource.VideoResource;
import org.jonasribeiro.admin.catalogo.domain.video.*;
import org.jonasribeiro.admin.catalogo.infraestructure.configuration.properties.storage.StorageProperties;
import org.jonasribeiro.admin.catalogo.infraestructure.services.StorageService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DefaultMediaResourceGateway implements MediaResourceGateway {

    private final String fileNamePattern;
    private final String locationPattern;
    private final StorageService storageService;

    public DefaultMediaResourceGateway(final StorageProperties props, final StorageService storageService) {
        this.fileNamePattern = props.getFileNamePattern();
        this.locationPattern = props.getLocationPattern();
        this.storageService = storageService;
    }

    @Override
    public AudioVideoMedia storeAudioVideo(VideoID anId, VideoResource videoResource) {
        final var filePath = filePath(anId, videoResource);
        final var aResource = videoResource.resource();
        store(filePath, aResource);
        return AudioVideoMedia.with(aResource.checksum(), aResource.name(),filePath);
    }

    @Override
    public ImageMedia storeImage(VideoID anId, VideoResource videoResource) {
        final var filePath = filePath(anId, videoResource);
        final var aResource = videoResource.resource();
        store(filePath, aResource);
        return ImageMedia.with(aResource.checksum(), aResource.name(),filePath);
    }

    private String filePath(final VideoID anId, final VideoMediaType type) {
        return folder(anId)
                .concat("/")
                .concat(fileName(type));
    }

    @Override
    public Optional<Resource> getResource(VideoID anId, VideoMediaType type) {
        return this.storageService.get(filePath(anId, type));
    }

    @Override
    public void clearResources(VideoID anId) {
        final var ids = this.storageService.list(folder(anId));
        this.storageService.deleteAll(ids);
    }

    private String fileName(final VideoMediaType aType) {
        return fileNamePattern.replace("{type}", aType.name());
    }

    private String folder(final VideoID anId) {
        return locationPattern.replace("{videoId}", anId.getValue());
    }

    private String filePath(final VideoID anId, final VideoResource aResource) {
        return folder(anId)
                .concat("/")
                .concat(fileName(aResource.type()));
    }

    private void store(final String filePath, final Resource aResource) {
        this.storageService.store(filePath, aResource);
    }
}
