package org.jonasribeiro.admin.catalogo.infraestructure.category.presenters;

import org.jonasribeiro.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import org.jonasribeiro.admin.catalogo.application.category.retrieve.list.CategoryListOutput;
import org.jonasribeiro.admin.catalogo.infraestructure.category.models.CategoryListResponse;
import org.jonasribeiro.admin.catalogo.infraestructure.category.models.CategoryResponse;

public interface CategoryApiPresenter {

    static CategoryResponse present(final CategoryOutput output) {
        return new CategoryResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt() != null ? output.createdAt().toString() : null,
                output.updatedAt() != null ? output.updatedAt().toString() : null,
                output.deletedAt() != null ? output.deletedAt().toString() : null
        );
    }

    static CategoryListResponse present(final CategoryListOutput output) {
        return new CategoryListResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }
}
