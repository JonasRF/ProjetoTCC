package org.jonasribeiro.admin.catalogo.infraestructure.category;

import org.jonasribeiro.admin.catalogo.domain.category.Category;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;
import org.jonasribeiro.admin.catalogo.domain.category.CategorySearchQuery;
import org.jonasribeiro.admin.catalogo.infraestructure.anotation.MySQLGatewayTest;
import org.jonasribeiro.admin.catalogo.infraestructure.category.persistence.CategoryJpaEntity;
import org.jonasribeiro.admin.catalogo.infraestructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryMySQLGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenValidCategory_whenCallsCreate_shouldReturnNewCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryMySQLGateway.create(aCategory);

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

    }

    @Test
    public void givenValidCategory_whenCallsUpdate_shouldReturnCategoryUpdated() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory("Films", null, expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        Assertions.assertEquals(1, categoryRepository.count());

        final var actualInvalidEntity = categoryRepository.findById(aCategory.getId().getValue()).get();
        Assertions.assertEquals("Films", actualInvalidEntity.getName());
        Assertions.assertNull(actualInvalidEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualInvalidEntity.isActive());

        final var aUpdatedCategory = aCategory.clone().update(expectedName, expectedDescription, expectedIsActive);

        final var actualCategory = categoryMySQLGateway.update(aUpdatedCategory);

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenPersistedCategoryAndValidCategoryId_whenTryDeleteById_shouldDeleteCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        Assertions.assertEquals(1, categoryRepository.count());

        // Busca a categoria pelo ID
        final var foundCategory = categoryMySQLGateway.create(aCategory);

        final var actualEntity = categoryRepository.findById(foundCategory.getId().getValue());

        Assertions.assertTrue(actualEntity.isPresent());
        Assertions.assertEquals(expectedName, actualEntity.get().getName());
        Assertions.assertEquals(expectedDescription, actualEntity.get().getDescription());
        Assertions.assertEquals(expectedIsActive, actualEntity.get().isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.get().getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualEntity.get().getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualEntity.get().getDeletedAt());
        Assertions.assertNull(actualEntity.get().getDeletedAt());

        // Deleta a categoria
        categoryMySQLGateway.deleteById(aCategory.getId());

        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenInvalidCategoryId_whenTryDeleteById_shouldDeleteCategory() {
        Assertions.assertEquals(0, categoryRepository.count());

        final var invalidId = CategoryID.from("invalid");

        // Tenta deletar uma categoria inexistente
        categoryMySQLGateway.deleteById(invalidId);

        // Garante que nada foi deletado
        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenValidCategoryId_whenCallsFindById_shouldReturnCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Category Description";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        Assertions.assertEquals(1, categoryRepository.count());

        final var actualCategory = categoryMySQLGateway.findById(aCategory.getId()).get();

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenInvalidCategoryId_whenCallsFindById_shouldReturnEmpty() {
        Assertions.assertEquals(0, categoryRepository.count());
        final var invalidId = CategoryID.from("empty");
        final var actualCategory = categoryMySQLGateway.findById(invalidId);
        Assertions.assertTrue(actualCategory.isEmpty());
    }

    @Test
    public void givenPersistedCategories_whenCallsFindAll_shouldReturnPaginated() {
        final var category1 = Category.newCategory("Filmes", "Descrição 1", true);
        final var category2 = Category.newCategory("Séries", "Descrição 2", true);
        final var category3 = Category.newCategory("Documentários", "Descrição 3", true);

        categoryRepository.saveAll(
               List.of(
                        CategoryJpaEntity.from(category1),
                        CategoryJpaEntity.from(category2),
                        CategoryJpaEntity.from(category3)
                )
        );
        categoryRepository.flush();

        final var page = 0;
        final var perPage = 2;
        final var terms = "";
        final var sort = "name";
        final var direction = "asc";
        final var query = new CategorySearchQuery(page, perPage, terms, sort, direction);

        final var result = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(page, result.currentPage());
        Assertions.assertEquals(perPage, result.perPage());
        Assertions.assertEquals(3, result.total());
        Assertions.assertEquals(2, result.items().size());

        Assertions.assertEquals("Documentários", result.items().get(0).getName());
        Assertions.assertEquals("Filmes", result.items().get(1).getName());
    }

    @Test
    public void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated() {
        final var category1 = Category.newCategory("Filmes", "Descrição 1", true);
        final var category2 = Category.newCategory("Séries", "Descrição 2", true);
        final var category3 = Category.newCategory("Documentários", "Descrição 3", true);

        categoryRepository.saveAll(
                List.of(
                        CategoryJpaEntity.from(category1),
                        CategoryJpaEntity.from(category2),
                        CategoryJpaEntity.from(category3)
                )
        );
        categoryRepository.flush();

        final var page = 1;
        final var perPage = 2;
        final var terms = "";
        final var sort = "name";
        final var direction = "asc";
        final var query = new CategorySearchQuery(page, perPage, terms, sort, direction);

        final var result = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(page, result.currentPage());
        Assertions.assertEquals(perPage, result.perPage());
        Assertions.assertEquals(3, result.total());
        Assertions.assertEquals(1, result.items().size());

        Assertions.assertEquals("Séries", result.items().get(0).getName());
    }

    @Test
    public void givenEmptyCategories_whenCallsFindAll_shouldReturnEmptyPaginated() {
        final var page = 0;
        final var perPage = 10;
        final var terms = "";
        final var sort = "name";
        final var direction = "asc";
        final var query = new CategorySearchQuery(page, perPage, terms, sort, direction);

        final var result = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(page, result.currentPage());
        Assertions.assertEquals(perPage, result.perPage());
        Assertions.assertEquals(0, result.total());
        Assertions.assertTrue(result.items().isEmpty());
    }

    @Test
    public void givenPersistedCategoriesAndDocsAsTerms_whenCallsFindAll_shouldReturnPaginated() {
        final var category1 = Category.newCategory("Filmes", "Descrição 1", true);
        final var category2 = Category.newCategory("Séries", "Descrição 2", true);
        final var category3 = Category.newCategory("Documentários", "Descrição 3", true);

        categoryRepository.saveAll(
                List.of(
                        CategoryJpaEntity.from(category1),
                        CategoryJpaEntity.from(category2),
                        CategoryJpaEntity.from(category3)
                )
        );
        categoryRepository.flush();

        final var page = 0;
        final var perPage = 1;
        final var terms = "doc";
        final var sort = "name";
        final var direction = "asc";
        final var query = new CategorySearchQuery(page, perPage, terms, sort, direction);

        final var result = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(page, result.currentPage());
        Assertions.assertEquals(perPage, result.perPage());
        Assertions.assertEquals(1, result.total());
        Assertions.assertEquals(1, result.items().size());

        Assertions.assertEquals("Documentários", result.items().get(0).getName());
    }

    @Test
    public void givenPersistedCategoriesAndDocsAsTerms_whenCallsFindAllAndTermsMatchsCategoryName_shouldReturnPaginated() {
        final var category1 = Category.newCategory("Filmes", "Descrição 1", true);
        final var category2 = Category.newCategory("Séries", "Descrição 2", true);
        final var category3 = Category.newCategory("Documentários", "Descrição 3", true);

        categoryRepository.saveAll(
                List.of(
                        CategoryJpaEntity.from(category1),
                        CategoryJpaEntity.from(category2),
                        CategoryJpaEntity.from(category3)
                )
        );
        categoryRepository.flush();

        final var page = 0;
        final var perPage = 1;
        final var terms = "sér";
        final var sort = "name";
        final var direction = "asc";
        final var query = new CategorySearchQuery(page, perPage, terms, sort, direction);

        final var result = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(page, result.currentPage());
        Assertions.assertEquals(perPage, result.perPage());
        Assertions.assertEquals(1, result.total());
        Assertions.assertEquals(1, result.items().size());

        Assertions.assertEquals("Séries", result.items().get(0).getName());

    }

    @Test
    public void givenPersistedCategoriesAndMaisAssistidasAsTerms_whenCallsFindAllAndTermsMatchsCategoryDescription_shouldReturnPaginated() {
        final var category1 = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var category2 = Category.newCategory("Séries", "Uma categoria assistida", true);
        final var category3 = Category.newCategory("Documentários", "A categoria menos assistida", true);

        categoryRepository.saveAll(
                List.of(
                        CategoryJpaEntity.from(category1),
                        CategoryJpaEntity.from(category2),
                        CategoryJpaEntity.from(category3)
                )
        );
        categoryRepository.flush();

        final var page = 0;
        final var perPage = 1;
        final var terms = "mais assistida";
        final var sort = "name";
        final var direction = "asc";
        final var query = new CategorySearchQuery(page, perPage, terms, sort, direction);

        final var result = categoryMySQLGateway.findAll(query);

        Assertions.assertEquals(page, result.currentPage());
        Assertions.assertEquals(perPage, result.perPage());
        Assertions.assertEquals(1, result.total());
        Assertions.assertEquals(1, result.items().size());

        Assertions.assertEquals("Filmes", result.items().get(0).getName());

    }
}
