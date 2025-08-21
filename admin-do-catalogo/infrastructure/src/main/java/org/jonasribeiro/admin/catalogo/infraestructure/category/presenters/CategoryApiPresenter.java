package org.jonasribeiro.admin.catalogo.infraestructure.category.presenters;

import org.jonasribeiro.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import org.jonasribeiro.admin.catalogo.infraestructure.category.models.CategoryApiOutput;

public interface CategoryApiPresenter {

    static CategoryApiOutput present(final CategoryOutput output) {
        return new CategoryApiOutput(
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
