package org.jonasribeiro.admin.catalogo.application.category.create;

import org.jonasribeiro.admin.catalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Objects;

public class CreateCategoryUseCaseTest {

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_ShouldReturnCategoryId() {

        final var expectedName = "Filmnes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;


        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final CategoryGateway categoryGateway = Mockito.mock(CategoryGateway.class);

        Mockito.when(categoryGateway.create(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        final var useCase = new DefaultCreateCategoryUseCase(categoryGateway);

        // Act
        final var actualOutput = useCase.execute(aCommand);

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .create(Mockito.argThat(aCategory ->
                        Objects.equals(expectedName, aCategory.getName()) &&
                                Objects.equals(expectedDescription, aCategory.getDescription())
                                && Objects.equals( expectedIsActive == aCategory.isActive()

    }
                ));
        Mockito.verifyNoMoreInteractions(categoryGateway);

    }
}
