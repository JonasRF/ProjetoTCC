package org.jonasribeiro.admin.catalogo.infraestructure.genre;

import org.jonasribeiro.admin.catalogo.MySQLGatewayTest;
import org.jonasribeiro.admin.catalogo.domain.category.Category;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;
import org.jonasribeiro.admin.catalogo.domain.genre.Genre;
import org.jonasribeiro.admin.catalogo.infraestructure.category.CategoryMySQLGateway;
import org.jonasribeiro.admin.catalogo.infraestructure.genre.persistence.GenreJpaEntity;
import org.jonasribeiro.admin.catalogo.infraestructure.genre.persistence.GenreRepository;
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

        Assertions.assertEquals(expectedId.getValue(), actualGenre.getId().getValue());
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

        Assertions.assertEquals(expectedId.getValue(), actualGenre.getId().getValue());
        Assertions.assertEquals(expectedName, actualGenre.getName());
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
    public void givenAAValidGenreWithoutCategories_whenCallsUpdateGenreWithCategories_thenShouldPersistGenre(){
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var series = categoryGateway.create(Category.newCategory("Séries", null, true));

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(filmes.getId(), series.getId());

        final var aGenre = Genre.newGenre("ac", expectedIsActive);
        aGenre.addCategories(List.of(filmes.getId(), series.getId()));

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertEquals("ac", aGenre.getName());
        Assertions.assertEquals(0, aGenre.getCategories().size());

        final var actualGenre =
                genreGateway.update(Genre.with(aGenre).update(expectedName, expectedIsActive, expectedCategories));

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId.getValue(), actualGenre.getId().getValue());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId.getValue(), persistedGenre.getId());
        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        Assertions.assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAAValidGenreInactive_whenCallsUpdateGenreAsActivating_thenShouldPersistGenre(){

        final var  expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, false);

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertFalse(aGenre.isActive());
        Assertions.assertNotNull(aGenre.getDeletedAt());

        final var actualGenre =
                genreGateway.update(Genre.with(aGenre).update(expectedName, expectedIsActive, expectedCategories));

        Assertions.assertEquals(expectedId.getValue(), actualGenre.getId().getValue());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(aGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedId.getValue(), persistedGenre.getId());
        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        Assertions.assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAAValidGenreActive_whenCallsUpdateGenreAsInactivating_thenShouldPersistGenre(){

        final var  expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, true);

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertTrue(aGenre.isActive());
        Assertions.assertNull(aGenre.getDeletedAt());

        final var actualGenre =
                genreGateway.update(Genre.with(aGenre).update(expectedName, expectedIsActive, expectedCategories));

        Assertions.assertEquals(expectedId.getValue(), actualGenre.getId().getValue());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedId.getValue(), persistedGenre.getId());
        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        Assertions.assertNotNull(persistedGenre.getDeletedAt());

    }

    @Test
    public void givenAValidGenre_whenCallsDeleteById_thenShouldDeleteGenre(){
        final var aGenre = Genre.newGenre("Ação", true);
        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertEquals(1, genreRepository.count());

        genreGateway.deleteById(expectedId);

        Assertions.assertEquals(0, genreRepository.count());
    }
}
