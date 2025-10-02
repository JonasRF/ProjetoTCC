package org.jonasribeiro.admin.catalogo.domain.video;

import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberID;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;
import org.jonasribeiro.admin.catalogo.domain.exceptions.DomainException;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreID;
import org.jonasribeiro.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

public class VideoValidatorTest {

    @Test
    public void givenNullTitle_whenCallsValidate_shouldReceiveError() {
        //given
        final String expectedTitle = null;
        final var expectedDescription = """
                Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised.
                """;
        final var expectedLaunchedAt = Year.of(2020);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMembers = Set.of(CastMemberID.unique());
        final var expectedErrorMessage = "'title' should not be null";
        final var expectedErrorCount = 1;

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        //when
        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> actualVideo.validate(new ThrowsValidationHandler())
        );

        //then
        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

    }
    @Test
    public void givenEmptyTitle_whenCallsValidate_shouldReceiveError() {
        //given
        final String expectedTitle = "";
        final var expectedDescription = """
                Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised.
                """;
        final var expectedLaunchedAt = Year.of(2020);
        final var expectedDuration = 90.0;
        final var expectedOpened = true;
        final var expectedPublished = true;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMembers = Set.of(CastMemberID.unique());
        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        //when
        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> actualVideo.validate(new ThrowsValidationHandler())
        );

        //then
        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenTitleWithLengthGreaterThan255_whenCallsValidate_shouldReceiveError() {
        //given
        final String expectedTitle = """ 
                Disclaimer: This video is for entertainment purposes only.
                All characters and events depicted in this video are fictional and any resemblance to real persons,
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised.
        """;
final var expectedDescription = """
                Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised.
                """;
        final var expectedLaunchedAt = Year.of(2020);
        final var expectedDuration = 90.0;
        final var expectedOpened = true;
        final var expectedPublished = true;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMembers = Set.of(CastMemberID.unique());
        final var expectedErrorMessage = "'title' must be between 1 and 255 characters";
        final var expectedErrorCount = 1;

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        //when
        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> actualVideo.validate(new ThrowsValidationHandler())
        );

        //then
        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

        @Test
    public void givenEmptyDescription_whenCallsValidate_shouldReceiveError() {
            //given
            final String expectedTitle = "System Design Interviews";
            final var expectedDescription = "";
            final var expectedLaunchedAt = Year.of(2020);
            final var expectedDuration = 90.0;
            final var expectedOpened = true;
            final var expectedPublished = true;
            final var expectedRating = Rating.L;
            final var expectedCategories = Set.of(CategoryID.unique());
            final var expectedGenres = Set.of(GenreID.unique());
            final var expectedCastMembers = Set.of(CastMemberID.unique());
            final var expectedErrorMessage = "'description' should not be empty";
            final var expectedErrorCount = 1;

            final var actualVideo = Video.newVideo(
                    expectedTitle,
                    expectedDescription,
                    expectedLaunchedAt,
                    expectedDuration,
                    expectedOpened,
                    expectedPublished,
                    expectedRating,
                    expectedCategories,
                    expectedGenres,
                    expectedCastMembers
            );

            //when
            final var actualError = Assertions.assertThrows(
                    DomainException.class,
                    () -> actualVideo.validate(new ThrowsValidationHandler())
            );

            //then
            Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
            Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenDescriptionWithLengthGreaterThan4000_whenCallsValidate_thenshouldReceiveError() {
        //given
        final String expectedTitle = """ 
                Disclaimer: This video is for entertainment purposes only.
                All characters and events depicted in this video are fictional and any resemblance to real persons,
                living or dead, is purely coincidental. Viewer discretion is advised.
        """;
        final var expectedDescription = """
                Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised.v Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised.v Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised. Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised.
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 90.0;
        final var expectedOpened = true;
        final var expectedPublished = true;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMembers = Set.of(CastMemberID.unique());
        final var expectedErrorMessage = "'description' must be between 1 and 4000 characters";
        final var expectedErrorCount = 1;

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        //when
        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> actualVideo.validate(new ThrowsValidationHandler())
        );
        //then
        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenNullLaunchedAt_whenCallsValidate_shouldReceiveError() {
        //given
        final String expectedTitle = "System Design Interviews";
    final var expectedDescription = "A Description";
        final Year expectedLaunchedAt = null;
        final var expectedDuration = 90.0;
        final var expectedOpened = true;
        final var expectedPublished = true;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMembers = Set.of(CastMemberID.unique());
        final var expectedErrorMessage = "'launchedAt' should not be null";
        final var expectedErrorCount = 1;

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        //when
        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> actualVideo.validate(new ThrowsValidationHandler())
        );

        //then
        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

    }

    @Test
    public void givenNullRating_whenCallsValidate_shouldReceiveError() {
        //given
        final String expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                Disclaimer: This video is for entertainment purposes only. 
                All characters and events depicted in this video are fictional and any resemblance to real persons, 
                living or dead, is purely coincidental. Viewer discretion is advised.
                """;
        final var expectedLaunchedAt = Year.of(2020);
        final var expectedDuration = 90.0;
        final var expectedOpened = true;
        final var expectedPublished = true;
        final Rating expectedRating = null;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMembers = Set.of(CastMemberID.unique());
        final var expectedErrorMessage = "'rating' should not be null";
        final var expectedErrorCount = 1;

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        //when
        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> actualVideo.validate(new ThrowsValidationHandler())
        );

        //then
        Assertions.assertEquals(expectedErrorCount, actualError.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }
}

