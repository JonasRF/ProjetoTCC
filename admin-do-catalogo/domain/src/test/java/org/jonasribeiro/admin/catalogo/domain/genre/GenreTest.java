package org.jonasribeiro.admin.catalogo.domain.genre;

import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;
import org.jonasribeiro.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GenreTest {

    @Test
    public void givenValidParams_whenCallNewGenre_thenShouldInstantiateGenre() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenValidParams_whenCallNewGenreAndValidate_thenShouldReceiveError() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidEmptyName_whenCallNewGenreAndValidate_thenShouldReceiveError() {
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNameWithLengthGreaterThan255_whenCallNewGenreAndValidate_thenShouldReceiveError() {
        final var expectedName = """    
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. 
                Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. 
                Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. 
                Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. 
                Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
                """;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must not be less than 1 and greater than 255 characters";

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenActiveGenre_whenCallDeactivate_thenShouldReceiveOk() {
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, true);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertTrue(actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());

        actualGenre.deactivate();

        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenInactiveGenre_whenCallActivate_thenShouldReceiveOk() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, false);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertFalse(actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.activate();

        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertFalse(actualGenre.getUpdatedAt().isAfter(actualUpdatedAt));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenValidUpdateParams_whenCallUpdate_thenShouldReceiveGenreUpdated() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));

        final var actualGenre = Genre.newGenre("Acão", false);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertFalse(actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        final var updatedGenre = actualGenre.update(expectedName, expectedIsActive, expectedCategories);

        Assertions.assertNotNull(updatedGenre);
        Assertions.assertEquals(actualGenre.getId(), updatedGenre.getId());
        Assertions.assertEquals(expectedName, updatedGenre.getName());
        Assertions.assertEquals(expectedIsActive, updatedGenre.isActive());
        Assertions.assertEquals(expectedCategories, updatedGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, updatedGenre.getCreatedAt());
        Assertions.assertFalse(updatedGenre.getUpdatedAt().isBefore(actualUpdatedAt));
        Assertions.assertNull(updatedGenre.getDeletedAt());
        Assertions.assertFalse(updatedGenre.getUpdatedAt().isAfter(actualGenre.getUpdatedAt()));
    }

    @Test
    public void givenInvalidEmptyName_whenCallUpdateAndValidate_thenShouldReceiveError() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualGenre.update(" ", expectedIsActive, expectedCategories);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNameWithLengthGreaterThan255_whenCallUpdateAndValidate_thenShouldReceiveError() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must not be less than 1 and greater than 255 characters";

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualGenre.update("""    
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit. 
                    Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. 
                    Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. 
                    Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. 
                    Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
                    """, expectedIsActive, expectedCategories);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenValidGenre_WhenCallUpdateNullName_ShouldReceiveNotificationException() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualGenre.update(null, expectedIsActive, expectedCategories);
        });

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenValidGenre_WhenCallUpdateNullCategories_ShouldReceiveOK() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<>();

        final var actualGenre = Genre.newGenre("acao", false);

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        Assertions.assertDoesNotThrow(() -> {
            actualGenre.update(expectedName, expectedIsActive, null);
        });

        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertFalse(actualGenre.getUpdatedAt().isAfter(actualUpdatedAt));
        Assertions.assertTrue(actualGenre.getCategories().isEmpty());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenValidEmptyCategoriesGenre_WhenCallAddCategory_ShouldReceiveOK() {
        final var seriesID = CategoryID.from("123");
        final var moviesID = CategoryID.from("456");

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(seriesID, moviesID);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.addCategory(seriesID);
        actualGenre.addCategory(moviesID);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertFalse(actualGenre.getUpdatedAt().isAfter(actualUpdatedAt));
        Assertions.assertFalse(actualGenre.getCategories().isEmpty());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenValidEmptyCategoriesGenre_WhenCallAddNullCategory_ShouldReceiveOK() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of();

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.addCategory(null);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertFalse(actualGenre.getUpdatedAt().isAfter(actualUpdatedAt));
        Assertions.assertTrue(actualGenre.getCategories().isEmpty());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenValidCategoriesGenre_WhenCallRemoveCategory_ShouldReceiveOK() {
        final var seriesID = CategoryID.from("123");
        final var moviesID = CategoryID.from("456");

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(moviesID);

        final var actualGenre = Genre.newGenre("acao", expectedIsActive);
        actualGenre.update(expectedName, expectedIsActive, List.of(seriesID, moviesID));

        Assertions.assertEquals(2, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.removeCategory(seriesID);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertFalse(actualGenre.getUpdatedAt().isAfter(actualUpdatedAt));
        Assertions.assertNull(actualGenre.getDeletedAt());

    }

    @Test
    public void givenValidCategoriesGenre_WhenCallRemoveNullCategory_ShouldReceiveOK() {
        final var seriesID = CategoryID.from("123");
        final var moviesID = CategoryID.from("456");

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(seriesID, moviesID);

        final var actualGenre = Genre.newGenre("acao", expectedIsActive);
        actualGenre.update(expectedName, expectedIsActive, List.of(seriesID, moviesID));

        Assertions.assertEquals(2, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.removeCategory(null);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertFalse(actualGenre.getUpdatedAt().isAfter(actualUpdatedAt));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }
}