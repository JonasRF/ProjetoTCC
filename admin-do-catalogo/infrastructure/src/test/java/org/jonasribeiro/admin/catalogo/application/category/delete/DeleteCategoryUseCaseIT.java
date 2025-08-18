package org.jonasribeiro.admin.catalogo.application.category.delete;

import org.jonasribeiro.admin.catalogo.IntegrationTest;
import org.jonasribeiro.admin.catalogo.domain.category.Category;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryGateway;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;
import org.jonasribeiro.admin.catalogo.infraestructure.category.persistence.CategoryJpaEntity;
import org.jonasribeiro.admin.catalogo.infraestructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class DeleteCategoryUseCaseIT {

    @Autowired
    private DeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenValidId_whenCallsDeleteCategory_shouldBeOK() {
        final var aCategory = Category.newCategory("Filmes", "categoria mais assitida", true);
        final var expectedId = aCategory.getId();

         save(aCategory);
         Assertions.assertEquals(1, categoryRepository.count());
         Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
         Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenInvalidId_whenCallsDeleteCategory_shouldReturnBeOK() {
        final var expectedId = "123";

        Assertions.assertEquals(0, categoryRepository.count());
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId));

    }

    @Test
    public void givenValidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedId = "123";

        doThrow(new IllegalStateException("Gateway error"))
                .when(categoryGateway).deleteById(eq(CategoryID.from(expectedId)));

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId));

        verify(categoryGateway, times(1)).deleteById(eq(CategoryID.from(expectedId)));

    }

    private void save(final Category... aCategory) {
        categoryRepository.saveAllAndFlush(
                Arrays.stream(aCategory)
                        .map(CategoryJpaEntity::from)
                        .toList()
        );
    }
}
