package org.jonasribeiro.admin.catalogo.infraestructure.services.local;

import org.jonasribeiro.admin.catalogo.domain.Fixture;
import org.jonasribeiro.admin.catalogo.domain.utils.IdUtils;
import org.jonasribeiro.admin.catalogo.domain.video.VideoMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class InMemoryStorageServiceTest {


    private InMemoryStorageService target = new InMemoryStorageService();

    @BeforeEach
    public void setUp() {
        target.reset();
    }

    @Test
    public void givenValidResource_whenCallsStore_shouldStoreIt() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var expectedName = IdUtils.uuid();

        target.store(expectedName, expectedResource);

        Assertions.assertEquals(expectedResource, this.target.storage().get(expectedName));
    }

    @Test
    public void givenValidResource_whenCallsGet_shouldRetrieveIt() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var expectedName = IdUtils.uuid();

        target.storage().put(expectedName, expectedResource);

        final var actualResource = target.get(expectedName).get();

        Assertions.assertEquals(expectedResource, actualResource);
    }

    @Test
    public void givenValidResource_whenCallsGet_shouldBeEmpty() {

        final var expectedName = IdUtils.uuid();

        final var actualResource = target.get(expectedName);

        Assertions.assertTrue(actualResource.isEmpty());
    }

    @Test
    public void givenPrefix_whenCallsList_shouldRetrieveAll() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);

        final var expectedIds = List.of("item1", "item2");

        this.target.storage().put("item1", expectedResource);
        this.target.storage().put("item2", expectedResource);

        final var actualContent = target.list("it");

        Assertions.assertTrue(
                expectedIds.size() == actualContent.size()
                        && expectedIds.containsAll(actualContent)
        );
    }

    @Test
    public void givenResource_whenCallsDeleteAll_shouldEmptyStorage() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);

        final var expectedIds = List.of("item1", "item2");

        this.target.storage().put("item1", expectedResource);
        this.target.storage().put("item2", expectedResource);

        target.deleteAll(expectedIds);

        Assertions.assertTrue(this.target.storage().isEmpty());
    }
}