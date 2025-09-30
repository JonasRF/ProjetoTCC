package org.jonasribeiro.admin.catalogo.application.video;

import org.jonasribeiro.admin.catalogo.application.Fixture;
import org.jonasribeiro.admin.catalogo.application.UseCaseTest;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberGateway;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryGateway;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreGateway;
import org.jonasribeiro.admin.catalogo.domain.video.Resource;
import org.jonasribeiro.admin.catalogo.domain.video.VideoGateway;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CreateVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return of();
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideo_shouldReturnVideoId() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished =  Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
        final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
        final var expectedMembers = Set.of(
                Fixture.CastMembers.jonas().getId(),
                Fixture.CastMembers.maria().getId());
        final Resource expectedVideo = Fixture.Videos.resource(Resource.Type.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Resource.Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Resource.Type.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(Resource.Type.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(Resource.Type.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        when(videoGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualResult = useCase.execute(aCommand);

        // then
        assertNotNull(actualResult);
        assertNotNull(actualResult.id());

        verify(videoGateway).create(argThat(aVideo ->
                Objects.equals(expectedTitle, aVideo.getTitle()) &&
                Objects.equals(expectedDescription, aVideo.getDescription()) &&
                Objects.equals(expectedLaunchYear, aVideo.getLaunchedAt()) &&
                Objects.equals(expectedDuration, aVideo.getDuration()) &&
                Objects.equals(expectedOpened, aVideo.getOpened()) &&
                Objects.equals(expectedPublished, aVideo.getPublished()) &&
                Objects.equals(expectedRating, aVideo.getRating()) &&
                Objects.equals(expectedCategories, aVideo.getCategories()) &&
                Objects.equals(expectedGenres, aVideo.getGenres()) &&
                Objects.equals(expectedMembers, aVideo.getCastMembers()) &&
                Objects.equals(expectedVideo, aVideo.getVideo().get()) &&
                Objects.equals(expectedTrailer, aVideo.getTrailer().get()) &&
                Objects.equals(expectedBanner, aVideo.getBanner().get()) &&
                Objects.equals(expectedThumb, aVideo.getThumbnail().get()) &&
                Objects.equals(expectedThumbHalf, aVideo.getThumbnailHalf().get())
                        && aVideo.getVideo().isPresent()
                        && aVideo.getTrailer().isPresent()
                        && aVideo.getBanner().isPresent()
                        && aVideo.getThumbnail().isPresent()
                        && aVideo.getThumbnailHalf().isPresent()
        ));
    }
}
