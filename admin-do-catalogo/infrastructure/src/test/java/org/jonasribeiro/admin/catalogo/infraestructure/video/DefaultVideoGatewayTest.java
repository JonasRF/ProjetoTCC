package org.jonasribeiro.admin.catalogo.infraestructure.video;

import org.jonasribeiro.admin.catalogo.IntegrationTest;
import org.jonasribeiro.admin.catalogo.application.video.create.CreateVideoCommand;
import org.jonasribeiro.admin.catalogo.domain.Fixture;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMember;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberGateway;
import org.jonasribeiro.admin.catalogo.domain.category.Category;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryGateway;
import org.jonasribeiro.admin.catalogo.domain.genre.Genre;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreGateway;
import org.jonasribeiro.admin.catalogo.domain.video.*;
import org.jonasribeiro.admin.catalogo.infraestructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

import static org.jonasribeiro.admin.catalogo.domain.Fixture.CastMembers.jonas;
import static org.jonasribeiro.admin.catalogo.domain.Fixture.Categories.aulas;
import static org.jonasribeiro.admin.catalogo.domain.Fixture.Genres.tech;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@IntegrationTest
public class DefaultVideoGatewayTest {

    @Autowired
    private DefaultVideoGateway videoGateway;

    @Autowired
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private VideoRepository videoRepository;

    private CastMember jonas;
    private CastMember maria;

    private Category aulas;
    private Category lives;

    private Genre tech;
    private Genre business;

    @Test
    public void testInjection() {
        Assertions.assertNotNull(this.videoGateway);
        Assertions.assertNotNull(this.castMemberGateway);
        Assertions.assertNotNull(this.categoryGateway);
        Assertions.assertNotNull(this.genreGateway);
        Assertions.assertNotNull(this.videoRepository);
    }

    @Test
    public void givenAValidVideo_whenCallsCreate_shouldPersistIt() {
        // given
       final var jonas = castMemberGateway.create(Fixture.CastMembers.jonas());
       final var maria = categoryGateway.create(Fixture.Categories.aulas());
       final var tech = genreGateway.create(Fixture.Genres.tech());

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas().getId());
        final var expectedGenres = Set.of(tech().getId());
        final var expectedMembers = Set.of(jonas().getId());


        final AudioVideoMedia expectedVideo = AudioVideoMedia.with("123", "video", "/media/video");
        final AudioVideoMedia expectedTrailer = AudioVideoMedia.with("123", "trailer", "/media/trailer");
        final ImageMedia expectedBanner = ImageMedia.with("123", "banner", "/media/banner");
        final ImageMedia expectedThumb = ImageMedia.with("123", "thumb", "/media/thumb");
        final ImageMedia expectedThumbHalf = ImageMedia.with("123", "thumbHalf", "/media/thumbHalf");

        final var aVideo = Video.newVideo(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedOpened,
                        expectedPublished,
                        expectedRating,
                        expectedCategories,
                        expectedGenres,
                        expectedMembers
                )
                .setVideo(expectedVideo)
                .setTrailer(expectedTrailer)
                .setBanner(expectedBanner)
                .setThumbnail(expectedThumb)
                .setThumbnailHalf(expectedThumbHalf);

        // when
        final var actualVideo = videoGateway.create(aVideo);

        // then
        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());

       Assertions.assertEquals(expectedTitle, aVideo.getTitle());
       Assertions.assertEquals(expectedDescription, aVideo.getDescription());
       Assertions.assertEquals(expectedLaunchYear, aVideo.getLaunchedAt());
       Assertions.assertEquals(expectedDuration, aVideo.getDuration());
       Assertions.assertEquals(expectedOpened, aVideo.getOpened());
       Assertions.assertEquals(expectedPublished, aVideo.getPublished());
       Assertions.assertEquals(expectedRating, aVideo.getRating());
       Assertions.assertEquals(expectedCategories, aVideo.getCategories());
       Assertions.assertEquals(expectedGenres, aVideo.getGenres());
       Assertions.assertEquals(expectedMembers, aVideo.getCastMembers());
       Assertions.assertEquals(expectedVideo, actualVideo.getVideo().get().name());
       Assertions.assertEquals(expectedTrailer, actualVideo.getTrailer().get().name());
       Assertions.assertEquals(expectedBanner, actualVideo.getBanner().get().name());
       Assertions.assertEquals(expectedThumb, actualVideo.getThumbnail().get().name());
       Assertions.assertEquals(expectedThumbHalf, actualVideo.getThumbnailHalf().get().name());

       final var persistedVideo = videoRepository.findById(actualVideo.getId().getValue()).get();

         Assertions.assertEquals(expectedTitle, persistedVideo.getTitle());
            Assertions.assertEquals(expectedDescription, persistedVideo.getDescription());
            Assertions.assertEquals(expectedLaunchYear, persistedVideo.getYearLaunched());
            Assertions.assertEquals(expectedDuration, persistedVideo.getDuration());
            Assertions.assertEquals(expectedOpened, persistedVideo.isOpened());
            Assertions.assertEquals(expectedPublished, persistedVideo.isPublished());
            Assertions.assertEquals(expectedRating, persistedVideo.getRating());
            Assertions.assertEquals(expectedCategories, persistedVideo.getCategoryIDs());
            Assertions.assertEquals(expectedGenres, persistedVideo.getGenreIDs());
            Assertions.assertEquals(expectedMembers, persistedVideo.getCastMemberIDs());
            Assertions.assertEquals(expectedVideo.name(), persistedVideo.getVideo().getName());
            Assertions.assertEquals(expectedTrailer.name(), persistedVideo.getTrailer().getName());
            Assertions.assertEquals(expectedBanner.name(), persistedVideo.getBanner().getName());
            Assertions.assertEquals(expectedThumb.name(), persistedVideo.getThumbnail().getName());
            Assertions.assertEquals(expectedThumbHalf.name(), persistedVideo.getThumbnailHalf().getName());
    }
}
