package org.jonasribeiro.admin.catalogo.application.category.delete;

import org.jonasribeiro.admin.catalogo.domain.category.Category;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryGateway;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    public void CleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenValidId_whenCallsDeleteCategory_shouldBeOK() {
        final var aCategory = Category.newCategory("Filmes", "categoria mais assitida", true);
        final var expectedId = aCategory.getId();

        doNothing().when(categoryGateway).deleteById(eq(expectedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        verify(categoryGateway, times(1)).deleteById(Mockito.eq(expectedId));
    }

    @Test
    public void givenInvalidId_whenCallsDeleteCategory_shouldReturnBeOK() {
        final var expectedId = "123";

        doNothing().when(categoryGateway).deleteById(eq(CategoryID.from(expectedId)));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId));
        verify(categoryGateway, times(1)).deleteById(eq(CategoryID.from(expectedId)));
}

    @Test
    public void givenValidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedId = "123";

        doThrow(new IllegalStateException("Gateway error"))
                .when(categoryGateway).deleteById(eq(CategoryID.from(expectedId)));

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId));
        verify(categoryGateway, times(1)).deleteById(eq(CategoryID.from(expectedId)));

        verifyNoMoreInteractions(categoryGateway);
    }
}