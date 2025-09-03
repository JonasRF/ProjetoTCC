package org.jonasribeiro.admin.catalogo.application.genre.delete;

import org.jonasribeiro.admin.catalogo.application.UseCaseTest;
import org.jonasribeiro.admin.catalogo.domain.genre.Genre;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreGateway;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.*;

public class DeleteGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteGenre_thenShouldDeleteGenre() {
        //given
        final var aGenre = Genre.newGenre("Ação", true);

        final var aGenreId = aGenre.getId();

        doNothing().when(genreGateway).deleteById(any());
        //when
        Assertions.assertDoesNotThrow(() -> useCase.execute(aGenreId.getValue()));
        //then

        verify(genreGateway, times(1)).deleteById(argThat(id -> Objects.equals(aGenreId.getValue(), id.getValue())));
    }

    @Test
    public void givenAnInvalidGenreId_whenCallsDeleteGenre_thenShouldBeOk() {
    //given
        final var expectedId = GenreID.from("123");

        doNothing().when(genreGateway).deleteById(any());

    //when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
    //then
        verify(genreGateway, times(1)).deleteById(argThat(id -> Objects.equals(expectedId.getValue(), id.getValue())));
    }

    @Test
    public void givenAValidId_whenCallsDeleteGenreAndGatewayThrowsException_thenShouldReceiveException() {
        //given
        final var aGenre = Genre.newGenre("Ação", true);
        final var aGenreId = aGenre.getId().getValue();

        doThrow(new IllegalStateException("Gateway error")).when(genreGateway).deleteById(any());

        //when
        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> {
            useCase.execute(aGenreId);
        });

        //then
        Assertions.assertEquals("Gateway error", actualException.getMessage());

        verify(genreGateway, times(1)).deleteById(argThat(id -> Objects.equals(aGenreId, id.getValue())));
    }
}
