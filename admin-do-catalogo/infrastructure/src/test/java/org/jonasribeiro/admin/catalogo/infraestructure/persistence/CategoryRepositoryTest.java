package org.jonasribeiro.admin.catalogo.infraestructure.persistence;

import org.hibernate.PropertyValueException;
import org.jonasribeiro.admin.catalogo.domain.category.Category;
import org.jonasribeiro.admin.catalogo.MySQLGatewayTest;
import org.jonasribeiro.admin.catalogo.infraestructure.category.persistence.CategoryJpaEntity;
import org.jonasribeiro.admin.catalogo.infraestructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenInvalidNullName_whenCallsSave_ShouldReturnError() {
        final var expectedPropertyName = "name";
        final var expectedMessage = "not-null property references a null or transient value : org.jonasribeiro.admin.catalogo.infraestructure.category.persistence.CategoryJpaEntity.name";

        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setName(null); // Setting name to null to trigger the DataIntegrityViolationException

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            this.categoryRepository.save(anEntity);
        });

        final var actualCause = Assertions.assertInstanceOf(
                PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    public void givenInvalidNullCreatedAt_whenCallsSave_ShouldReturnError() {
        final var expectedPropertyName = "createdAt";
        final var expectedMessage = "not-null property references a null or transient value : org.jonasribeiro.admin.catalogo.infraestructure.category.persistence.CategoryJpaEntity.createdAt";

        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setCreatedAt(null); // Setting name to null to trigger the DataIntegrityViolationException

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            this.categoryRepository.save(anEntity);
        });

        final var actualCause = Assertions.assertInstanceOf(
                PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    public void givenInvalidNullUpdatedAt_whenCallsSave_ShouldReturnError() {
        final var expectedPropertyName = "updatedAt";
        final var expectedMessage = "not-null property references a null or transient value : org.jonasribeiro.admin.catalogo.infraestructure.category.persistence.CategoryJpaEntity.updatedAt";

        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setUpdatedAt(null); // Setting name to null to trigger the DataIntegrityViolationException

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            this.categoryRepository.save(anEntity);
        });

        final var actualCause = Assertions.assertInstanceOf(
                PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }
}
