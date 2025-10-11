package org.jonasribeiro.admin.catalogo.infraestructure.services.impl;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.jonasribeiro.admin.catalogo.domain.Fixture;
import org.jonasribeiro.admin.catalogo.domain.utils.IdUtils;
import org.jonasribeiro.admin.catalogo.domain.video.Resource;
import org.jonasribeiro.admin.catalogo.domain.video.VideoMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static com.google.cloud.storage.Storage.BlobListOption.prefix;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class GCStorageAPITest {

    private GCStorageService target;

    private Storage storage;

    private String bucket = "test";

    @BeforeEach
    public void setUp() {
        this.storage = mock(Storage.class);
        this.target = new GCStorageService(bucket, storage);
    }

    @Test
    public void givenValidResource_whenCallsStore_shouldStoreIt() {
        // given
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var expectedName = IdUtils.uuid();

        final var blob = mockBlob(expectedName, expectedResource);
        doReturn(blob).when(storage).create(any(BlobInfo.class), any());

       // when
        this.target.store(expectedName, expectedResource);

        // then
        final var capturer = ArgumentCaptor.forClass(BlobInfo.class);

        verify(storage, times(1)).create(capturer.capture(), eq(expectedResource.content()));

        final var actualBlob = capturer.getValue();
        Assertions.assertEquals(this.bucket, actualBlob.getBlobId().getBucket());
        Assertions.assertEquals(expectedName, actualBlob.getBlobId().getName());
        Assertions.assertEquals(expectedName, actualBlob.getName());
        Assertions.assertEquals(expectedResource.contentType(), actualBlob.getContentType());
        Assertions.assertEquals(expectedResource.checksum(), actualBlob.getCrc32cToHexString());
    }

    @Test
    public void givenResource_whenCallsGet_shouldRetrieveIt() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedName = expectedResource.name();

        final Blob blob = mockBlob(expectedName, expectedResource);
        doReturn(blob).when(storage).get(eq(bucket), eq(expectedName));

        final var actualContent = target.get(expectedName).get();

        Assertions.assertEquals(expectedResource.checksum(), actualContent.checksum());
        Assertions.assertEquals(expectedResource.name(), actualContent.name());
        Assertions.assertEquals(expectedResource.content(), actualContent.content());
        Assertions.assertEquals(expectedResource.contentType(), actualContent.contentType());
    }

    @Test
    public void givenInvalidResource_whenCallsGet_shouldRetrieveEmpty() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedName  = expectedResource.name();

        doReturn(null).when(storage).get(eq(bucket), eq(expectedName ));

        final var actualContent = target.get(expectedName );

        Assertions.assertTrue(actualContent.isEmpty());
    }

    @Test
    public void givenPrefix_whenCallsList_shouldRetrieveAll() {
        // given
        final var expectedPrefix = "media_";

        final var expectedNameVideo = expectedPrefix + IdUtils.uuid();
        final var expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var expectedNameBanner = expectedPrefix + IdUtils.uuid();
        final var expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);

        final var expectedResources = List.of(expectedNameBanner, expectedNameVideo);

        final var blobVideo = mockBlob(expectedNameVideo, expectedVideo);
        final var blobBanner = mockBlob(expectedNameBanner, expectedBanner);

        final var page = mock(Page.class);
        doReturn(List.of(blobVideo, blobBanner)).when(page).iterateAll();

        doReturn(page).when(storage).list(anyString(), any());

        // when
        final var actualResorce = this.target.list(expectedPrefix);

        // then
        verify(storage, times(1)).list(eq(this.bucket), eq(prefix(expectedPrefix)));

        Assertions.assertTrue(
                expectedResources.size() == actualResorce.size()
                        && expectedResources.containsAll(actualResorce)
        );
    }

    @Test
    public void givenPrefix_whenCallsList_shouldDeleteAll() {
        // given
        final var expectedPrefix = "media_";

        final var expectedNameVideo = expectedPrefix + IdUtils.uuid();
        final var expectedNameBanner = expectedPrefix + IdUtils.uuid();

        final var expectedResources = List.of(expectedNameBanner, expectedNameVideo);

        // when
        this.target.deleteAll(expectedResources);

        // then
        final var captor = ArgumentCaptor.forClass(List.class);

        verify(storage, times(1)).delete(captor.capture());

        final var actualResorces = ((List<BlobId>) captor.getValue())
                .stream()
                .map(BlobId::getName)
                .toList();

        Assertions.assertTrue(
                expectedResources.size() == actualResorces.size()
                        && expectedResources.containsAll(actualResorces)
        );
    }

    private Blob mockBlob(final String name, final Resource expectedResource) {
        final var blob = mock(Blob.class);
        when(blob.getBlobId()).thenReturn(BlobId.of(this.bucket, name));
        when(blob.getCrc32cToHexString()).thenReturn(expectedResource.checksum());
        when(blob.getContent()).thenReturn(expectedResource.content());
        when(blob.getContentType()).thenReturn(expectedResource.contentType());
        when(blob.getName()).thenReturn(name);
        return blob;
    }
}
