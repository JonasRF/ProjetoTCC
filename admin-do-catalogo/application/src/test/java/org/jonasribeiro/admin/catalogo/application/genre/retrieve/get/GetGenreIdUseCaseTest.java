package org.jonasribeiro.admin.catalogo.application.genre.retrieve.get;

import org.jonasribeiro.admin.catalogo.application.UseCaseTest;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;
import org.jonasribeiro.admin.catalogo.domain.genre.Genre;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreGateway;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class GetGenreIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetGenreByIdUseCase useCase;

    @Mock
    private GenreGateway genreGateway;


    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetGenreById_shouldReturnGenre () {
        //given
        final var expectedId = "123";
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );

        final var genre = Genre.newGenre(expectedName, expectedIsActive);
        expectedCategories.forEach(genre::addCategory);

        when(genreGateway.findById(GenreID.from(expectedId))).thenReturn(Optional.of(genre));

        //when
        final var actualGenre = useCase.execute(expectedId);

        //then
        assertNotNull(actualGenre);
        assertEquals(expectedId, actualGenre.id());
        assertEquals(expectedName, actualGenre.name());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(asString(expectedCategories), actualGenre.categories());
        assertEquals(genre.getCreatedAt(), actualGenre.createdAt());
        assertEquals(genre.getUpdatedAt(), actualGenre.updatedAt());
        assertEquals(genre.getDeletedAt(), actualGenre.deletedAt());

        verify(genreGateway, times(1)).findById(GenreID.from(ArgumentMatchers.eq(expectedId)));

    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }

    @Test
    public void givenAInvalidId_whenCallsGetGenreAndDoesNotExists_shouldReturnNotFound () {
        //given
        final var expectedId = "123";
        final var expectedErrorMessage = "Genre with ID 123 was not found";

        when(genreGateway.findById(GenreID.from(expectedId))).thenReturn(Optional.empty());

        //when
        final var actualException = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(expectedId)
        );

        //then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(genreGateway, times(1)).findById(GenreID.from(ArgumentMatchers.eq(expectedId)));
    }
}
