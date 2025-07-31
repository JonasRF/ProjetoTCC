package org.jonasribeiro.admin.catalogo.application.category.create;

import org.jonasribeiro.admin.catalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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

        final var useCase = new CreateCategoryUseCase(categoryGateway);

        // Act
        final var actualOutput = useCase.execute(aCommand);

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

    }
}
