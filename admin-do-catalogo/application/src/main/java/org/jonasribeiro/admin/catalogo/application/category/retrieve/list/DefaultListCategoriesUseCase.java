package org.jonasribeiro.admin.catalogo.application.category.retrieve.list;

import org.jonasribeiro.admin.catalogo.domain.category.CategoryGateway;
import org.jonasribeiro.admin.catalogo.domain.category.CategorySearchQuery;
import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultListCategoriesUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Pagination<CategoryListOutput> execute(final CategorySearchQuery aQuery) {

        return this.categoryGateway.findAll(aQuery)
                .map(CategoryListOutput::from);
    }
}
