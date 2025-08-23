package org.jonasribeiro.admin.catalogo.infraestructure.category.presenters;

import org.jonasribeiro.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import org.jonasribeiro.admin.catalogo.infraestructure.category.models.CreateCategoryApiOutput;

public interface CategoryApiPresenter {

    static CreateCategoryApiOutput present(final CategoryOutput output) {
        return new CreateCategoryApiOutput(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt() != null ? output.createdAt().toString() : null,
                output.updatedAt() != null ? output.updatedAt().toString() : null,
                output.deletedAt() != null ? output.deletedAt().toString() : null
        );
    }
}
