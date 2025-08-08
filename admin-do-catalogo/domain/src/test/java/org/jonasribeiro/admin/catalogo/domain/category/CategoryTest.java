package org.jonasribeiro.admin.catalogo.domain.category;

import org.jonasribeiro.admin.catalogo.domain.exceptions.DomainException;
import org.jonasribeiro.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class CategoryTest {

    @Test
    public void givenValidParams_whenCallNewCategory_thenInstantiateCategory() {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final Category category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertNotNull(category);
        Assertions.assertNotNull(category.getId());
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertTrue(category.isActive());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNull(category.getDeletedAt());
    }

    @Test
    public void givenInValidNullName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(null, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> {
            actualCategory.validate(new ThrowsValidationHandler());
        });

        Assertions.assertEquals( expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals( expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInValidEmptyName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = " ";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> {
            actualCategory.validate(new ThrowsValidationHandler());
        });

        Assertions.assertEquals( expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals( expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInValidNameLengthLessThan3_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "Fi";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must not be less than 3 and greater than 255 characters";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> {
            actualCategory.validate(new ThrowsValidationHandler());
        });

        Assertions.assertEquals( expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals( expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInValidNameLengthMoreThan255_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final var expectedName = """
        cima de tudo, é fundamental ressaltar que o consenso sobre a necessidade de qualificação estende o alcance e a importância 
        da inteligência coletiva mobilizada. Vale destacar que o julgamento imparcial das eventualidades modifica 
        os parâmetros tradicionais de análise das direções preferenciais no sentido do progresso;
        """;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must not be less than 3 and greater than 255 characters";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> {
            actualCategory.validate(new ThrowsValidationHandler());
        });

        Assertions.assertEquals( expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals( expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenValidEmptyDescription_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "Filmes";
        final String expectedDescription = " ";
        final boolean expectedIsActive = true;

        final Category category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(category);
        Assertions.assertNotNull(category.getId());
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertTrue(category.isActive());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNull(category.getDeletedAt());
    }

    @Test
    public void givenValidFalseIsActive_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = false;

        final Category category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler(){
        }));
        Assertions.assertNotNull(category);
        Assertions.assertNotNull(category.getId());
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription, category.getDescription());
        Assertions.assertEquals(expectedIsActive,category.isActive());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNotNull(category.getDeletedAt());
    }

    @Test
    public void givenAValidActiveCategory_whenCallDeactivate_thenReturnCategoryInactivated() {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = false;

        final Category category = Category.newCategory(expectedName, expectedDescription, true);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var updatedAt = category.getUpdatedAt();
        final var createdAt = category.getCreatedAt();

       Assertions.assertTrue(category.isActive());
       Assertions.assertNull(category.getDeletedAt());

       final var actualCategory = category.deActivate();

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive,actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidInactiveCategory_whenCallActivate_thenReturnCategoryActivated() {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final Category category = Category.newCategory(expectedName, expectedDescription, false);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var updatedAt = category.getUpdatedAt();
        final var createdAt = category.getCreatedAt();

        Assertions.assertFalse(category.isActive());
        Assertions.assertNotNull(category.getDeletedAt());

        final var actualCategory = category.activate();

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive,actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_WhenCallUpdate_thenReturnCategoryUpdated() {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final Category category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var updatedAt = category.getUpdatedAt();
        final var createdAt = category.getCreatedAt();

        final String newName = "Séries";
        final String newDescription = "A categoria mais assistida de séries";

        try {
            Field nameField = Category.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(category, newName);

            Field descriptionField = Category.class.getDeclaredField("description");
            descriptionField.setAccessible(true);
            descriptionField.set(category, newDescription);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        final var actualCategory = category.update(newName, newDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(newName, actualCategory.getName());
        Assertions.assertEquals(newDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive,actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
    }

    @Test
    public void givenAValidCategory_WhenCallUpdateToInactive_thenReturnCategoryUpdated() {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = false;

        final Category category = Category.newCategory(expectedName, expectedDescription, true);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        Assertions.assertTrue(category.isActive());
        Assertions.assertNull(category.getDeletedAt());

        final var updatedAt = category.getUpdatedAt();
        final var createdAt = category.getCreatedAt();

        final String newName = "Séries";
        final String newDescription = "A categoria mais assistida de séries";

        try {
            Field nameField = Category.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(category, newName);

            Field descriptionField = Category.class.getDeclaredField("description");
            descriptionField.setAccessible(true);
            descriptionField.set(category, newDescription);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        final var actualCategory = category.update(newName, newDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(newName, actualCategory.getName());
        Assertions.assertEquals(newDescription, actualCategory.getDescription());
        Assertions.assertFalse(category.isActive());
        Assertions.assertNotNull(category.getDeletedAt());
        Assertions.assertEquals(expectedIsActive,actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
    }

    @Test
    public void givenAValidCategory_WhenCallUpdateWithInvalidParams_thenReturnCategoryUpdated() {
        final String expectedName = null;
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final Category category = Category.newCategory("Films", expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var updatedAt = category.getUpdatedAt();
        final var createdAt = category.getCreatedAt();

        final String newName = "Séries";
        final String newDescription = "A categoria mais assistida de séries";

        try {
            Field nameField = Category.class.getDeclaredField("name");
            nameField.setAccessible(true);
            nameField.set(category, newName);

            Field descriptionField = Category.class.getDeclaredField("description");
            descriptionField.setAccessible(true);
            descriptionField.set(category, newDescription);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        final var actualCategory = category.update(expectedName, newDescription, expectedIsActive);

        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(newDescription, actualCategory.getDescription());
        Assertions.assertTrue(category.isActive());
        Assertions.assertNull(category.getDeletedAt());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
    }
}
