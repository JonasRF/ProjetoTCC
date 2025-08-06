package org.jonasribeiro.admin.catalogo.application.category.update;

import org.jonasribeiro.admin.catalogo.domain.category.Category;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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

        final var aCommand = UpdateCategoryCommand.with(
                aCategory.getId(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(aCategory.getId()))
                .thenReturn(java.util.Optional.of(aCategory));

        when(categoryGateway.update(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .findById(Mockito.eq(aCategory.getId()));

        Mockito.verify(categoryGateway, Mockito.times(1))
                .update(Mockito.argThat(aCategoryUpdated -> {
                    return expectedName.equals(aCategoryUpdated.getName()) &&
                            expectedDescription.equals(aCategoryUpdated.getDescription()) &&
                            expectedIsActive == aCategoryUpdated.isActive() &&
                            aCategoryUpdated.getId().equals(aCategory.getId()) &&
                            aCategoryUpdated.getCreatedAt().equals(aCategory.getCreatedAt()) &&
                            aCategoryUpdated.getUpdatedAt() != null &&
                            aCategoryUpdated.getDeletedAt() == null;
                }));

    }
}
