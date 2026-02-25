package org.jonasribeiro.admin.catalogo.application.genre.delete;

import org.jonasribeiro.admin.catalogo.IntegrationTest;
import org.jonasribeiro.admin.catalogo.domain.genre.Genre;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreGateway;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreID;
import org.jonasribeiro.admin.catalogo.infraestructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.any;

@IntegrationTest
public class DeleteGenreUseCaseIT {

    @Autowired
    private DeleteGenreUseCase useCase;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenAValidId_whenCallsDeleteGenre_thenShouldDeleteGenre() {

        //given
        final var aGenre =genreGateway.create(Genre.newGenre("Ação", true));

        final var aGenreId = aGenre.getId();

        Assertions.assertEquals(1, genreRepository.count());

        //when
        Assertions.assertDoesNotThrow(() -> useCase.execute(aGenreId.getValue()));

        //then
        Assertions.assertEquals(0, genreRepository.count());

    }

    @Test
    public void givenAnInvalidGenreId_whenCallsDeleteGenre_thenShouldBeOk() {
        //given
        genreGateway.create(Genre.newGenre("Ação", true));

        final var expectedId = GenreID.from("123");

        Assertions.assertEquals(1, genreRepository.count());

        //when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        //then
        Assertions.assertEquals(1,  genreRepository.count());  }

}


