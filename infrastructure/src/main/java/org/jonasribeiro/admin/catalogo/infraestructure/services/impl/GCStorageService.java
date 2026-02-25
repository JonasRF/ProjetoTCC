package org.jonasribeiro.admin.catalogo.infraestructure.services.impl;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.jonasribeiro.admin.catalogo.domain.video.Resource;
import org.jonasribeiro.admin.catalogo.infraestructure.services.StorageService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class GCStorageService implements StorageService {

    private final String bucketName;
    private final Storage storage;

    public GCStorageService(String bucketName, Storage storage) {
        this.bucketName = bucketName;
        this.storage = storage;
    }

    @Override
    public void deleteAll(final Collection<String> names) {
        final var blobs = names.stream()
                .map(name -> BlobId.of(this.bucketName, name))
                .toList();
       this.storage.delete(blobs);
    }

    @Override
    public Optional<Resource> get(String name) {
   return Optional.ofNullable(this.storage.get(this.bucketName, name))
              .map(blob -> Resource.with(blob.getContent(), blob.getCrc32cToHexString(),blob.getContentType() , name));
    }

    @Override
    public List<String> list(String prefix) {
       final var blobs = this.storage.list(this.bucketName, Storage.BlobListOption.prefix(prefix)
        );
        return StreamSupport.stream(blobs.iterateAll().spliterator(), false)
                .map(BlobInfo::getBlobId)
                .map(BlobId::getName)
                .toList();
    }

    @Override
    public void store(String name, Resource resource) {
        final var blobInfo = BlobInfo.newBuilder(this.bucketName, name)
                .setContentType(resource.contentType())
                .setCrc32cFromHexString(resource.checksum())
                .build();
        this.storage.create(blobInfo, resource.content());
    }

    @Override
    public void clear() {
        this.storage.list(this.bucketName).iterateAll()
                .forEach(blob -> this.storage.delete(blob.getBlobId()));
    }
}
