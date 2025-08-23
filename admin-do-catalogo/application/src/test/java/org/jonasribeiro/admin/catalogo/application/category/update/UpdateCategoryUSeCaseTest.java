package org.jonasribeiro.admin.catalogo.application.category.update;

import org.jonasribeiro.admin.catalogo.domain.category.Category;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryGateway;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;
import org.jonasribeiro.admin.catalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUSeCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_ShouldReturnCategoryId() {
        final var aCategory = Category.newCategory("Film", null, true);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(aCategory.getId()))
                .thenReturn(java.util.Optional.of(aCategory.clone()));

        when(categoryGateway.update(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, times(1))
                .findById(Mockito.eq(aCategory.getId()));

        Mockito.verify(categoryGateway, times(1))
                .update(argThat(aCategoryUpdated -> {
                    return expectedName.equals(aCategoryUpdated.getName()) &&
                            expectedDescription.equals(aCategoryUpdated.getDescription()) &&
                            expectedIsActive == aCategoryUpdated.isActive() &&
                            aCategoryUpdated.getId().equals(aCategory.getId()) &&
                            aCategoryUpdated.getCreatedAt().equals(aCategory.getCreatedAt()) &&
                            aCategory.getUpdatedAt().isBefore(aCategoryUpdated.getUpdatedAt()) &&
                            aCategoryUpdated.getDeletedAt() == null;
                }));
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateCategory_ShouldReturnDomainException() {
    final var aCategory = Category.newCategory("Film", null, true);

        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(aCategory.getId()))
                .thenReturn(java.util.Optional.of(aCategory.clone()));

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(categoryGateway, times(0))
                .update(any());
    }

    @Test
    public void givenAValidInactiveCommand_whenCallsUpdateCategory_ShouldReturnInactiveCategoryId() {
        final var aCategory = Category.newCategory("Film", null, true);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(aCategory.getId()))
                .thenReturn(Optional.of(aCategory.clone()));

        when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, times(1))
                .findById(Mockito.eq(aCategory.getId()));

        Mockito.verify(categoryGateway, times(1))
                .update(argThat(aCategoryUpdated -> {
                    return expectedName.equals(aCategoryUpdated.getName()) &&
                            expectedDescription.equals(aCategoryUpdated.getDescription()) &&
                            expectedIsActive == aCategoryUpdated.isActive() &&
                            aCategoryUpdated.getId().equals(aCategory.getId()) &&
                            aCategoryUpdated.getCreatedAt().equals(aCategory.getCreatedAt()) &&
                            aCategory.getUpdatedAt().isBefore(aCategoryUpdated.getUpdatedAt()) &&
                            aCategoryUpdated.getDeletedAt() != null;
                }));
    }

    @Test
    public void givenAValidCommand_WhenGatewayThrowsRandomException_ShouldReturnException() {
        final var aCategory = Category.newCategory("Film", null, true);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(aCategory.getId()))
                .thenReturn(Optional.of(aCategory.clone()));

        when(categoryGateway.update(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());

        Mockito.verify(categoryGateway, times(1))
                .findById(Mockito.eq(aCategory.getId()));

        Mockito.verify(categoryGateway, times(1))
                .update(argThat(aCategoryUpdated -> {
                    return expectedName.equals(aCategoryUpdated.getName()) &&
                            expectedDescription.equals(aCategoryUpdated.getDescription()) &&
                            expectedIsActive == aCategoryUpdated.isActive() &&
                            aCategoryUpdated.getId().equals(aCategory.getId()) &&
                            aCategoryUpdated.getCreatedAt().equals(aCategory.getCreatedAt()) &&
                            aCategory.getUpdatedAt().isBefore(aCategoryUpdated.getUpdatedAt()) &&
                            aCategoryUpdated.getDeletedAt() == null;
                }));
    }

    @Test
    public void givenCommandWithInvalidID_whenCallsUpdateCategory_ShouldReturnNotFoundException() {
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(CategoryID.from(expectedId))))
                .thenReturn(Optional.empty());

        // Act
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> useCase.execute(aCommand)
        );

        // Assert
        Assertions.assertNotNull(actualException);
        Assertions.assertNotNull(actualException.getMessage());
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(categoryGateway, times(1))
                .findById(eq(CategoryID.from(expectedId)));

        Mockito.verify(categoryGateway, times(0))
                .update(any());
    }
}
