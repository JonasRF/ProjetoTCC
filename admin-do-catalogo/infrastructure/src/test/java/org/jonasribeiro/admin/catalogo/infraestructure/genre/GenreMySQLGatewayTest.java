package org.jonasribeiro.admin.catalogo.infraestructure.genre;

import org.jetbrains.annotations.NotNull;
import org.jonasribeiro.admin.catalogo.MySQLGatewayTest;
import org.jonasribeiro.admin.catalogo.domain.category.Category;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;
import org.jonasribeiro.admin.catalogo.domain.genre.Genre;
import org.jonasribeiro.admin.catalogo.infraestructure.category.CategoryMySQLGateway;
import org.jonasribeiro.admin.catalogo.infraestructure.genre.persistence.GenreCategoryJpaEntity;
import org.jonasribeiro.admin.catalogo.infraestructure.genre.persistence.GenreRepository;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private GenreMySQLGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;


    @Test
    public void testDependencies(){
        Assertions.assertNotNull(categoryGateway);
        Assertions.assertNotNull(genreGateway);
        Assertions.assertNotNull(genreRepository);
    }

    @Test
    public void givenAValidGenre_whenCallsCreateGenre_thenShouldPersistGenre(){

        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes.getId());

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        aGenre.addCategories(expectedCategories);

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        final var actualGenre = genreGateway.create(aGenre);

        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var actualEntity = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId.getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(expectedCategories, actualEntity.getCategoryIDs());
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithoutCategories_whenCallsCreateGenre_thenShouldPersistGenre(){

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        aGenre.addCategories(expectedCategories);

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        final var actualGenre = genreGateway.create(aGenre);

        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var actualEntity = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId.getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(expectedCategories, actualEntity.getCategoryIDs());
        Assertions.assertNotNull(actualEntity.getCreatedAt());
        Assertions.assertNotNull(actualEntity.getUpdatedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }
}
