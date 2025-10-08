package org.jonasribeiro.admin.catalogo.application.video.retrieve.list;

import org.jonasribeiro.admin.catalogo.application.Fixture;
import org.jonasribeiro.admin.catalogo.application.UseCaseTest;
import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.domain.video.VideoGateway;
import org.jonasribeiro.admin.catalogo.domain.video.VideoPreview;
import org.jonasribeiro.admin.catalogo.domain.video.VideoSearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ListVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListVideosUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListVideos_thenShouldReturnAllVideos() {
        // given
       final var videos = List.of(
                Fixture.video(),
                Fixture.video());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedTotal = 3;
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedItems = videos.stream()
                .map(VideoListOutput::from)
                .toList();

        final var videoPreviews = videos.stream()
                .map(VideoPreview::new)
                .toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                videoPreviews
        );

        when(videoGateway.findAll(any())).thenReturn(expectedPagination);

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );

        // when
        final var actualOutput = useCase.execute(aQuery);

        // then
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(videoGateway, times(1)).findAll(eq(aQuery));
    }



    @Test
    public void givenAValidQuery_whenCallsListVideosAndGatewayThrowsException_thenShouldReturnException() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Gateway error";

        when(videoGateway.findAll(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );

        // when
        final var actualException =
                Assertions.assertThrows(IllegalStateException.class, () -> {
                    useCase.execute(aQuery);
                });

        // then
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(videoGateway, times(1)).findAll(eq(aQuery));
    }
}
