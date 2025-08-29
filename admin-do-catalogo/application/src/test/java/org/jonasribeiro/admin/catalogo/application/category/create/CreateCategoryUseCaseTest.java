package org.jonasribeiro.admin.catalogo.application.category.create;

import org.jonasribeiro.admin.catalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_ShouldReturnCategoryId() {

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final CategoryGateway categoryGateway = Mockito.mock(CategoryGateway.class);

        when(categoryGateway.create(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        final var useCase = new DefaultCreateCategoryUseCase(categoryGateway);

        // Act
        final var actualOutput = useCase.execute(aCommand).get();

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .create(Mockito.argThat(aCategory -> {
                            return Objects.equals(expectedName, aCategory.getName()) &&
                                    Objects.equals(expectedDescription, aCategory.getDescription()) &&
                                    Objects.equals(expectedIsActive, aCategory.isActive()) &&
                                    aCategory.getId() != null &&
                                    aCategory.getCreatedAt() != null &&
                                    aCategory.getUpdatedAt() != null &&
                                    aCategory.getDeletedAt() == null;
                        }
                ));
    }

    @Test
    public void givenAnInvalidCommand_whenCallsCreateCategory_ShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var useCase = new DefaultCreateCategoryUseCase(categoryGateway);

        // Act & Assert
       final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(categoryGateway, Mockito.times(0))
                .create(Mockito.any());
    }

    @Test
    public void givenAValidCommandWithInactive_whenCallsCreateCategory_ShouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .create(argThat(aCategory -> {
                    return Objects.equals(expectedName, aCategory.getName()) &&
                            Objects.equals(expectedDescription, aCategory.getDescription()) &&
                            Objects.equals(expectedIsActive, aCategory.isActive()) &&
                            aCategory.getId() != null &&
                            aCategory.getCreatedAt() != null &&
                            aCategory.getUpdatedAt() != null &&
                            aCategory.getDeletedAt() != null;
                }));
    }

    @Test
   public void givenAValidCommand_WhenGatewayThrowsRandomException_ShouldReturnException() {

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.create(Mockito.any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(aCommand).getLeft();

        // Act & Assert
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .create(argThat(aCategory ->
                            Objects.equals(expectedName, aCategory.getName()) &&
                            Objects.equals(expectedDescription, aCategory.getDescription()) &&
                            Objects.equals(expectedIsActive, aCategory.isActive()) &&
                            aCategory.getId() != null &&
                            aCategory.getCreatedAt() != null &&
                            aCategory.getUpdatedAt() != null &&
                            aCategory.getDeletedAt() == null
                ));
        }
}

