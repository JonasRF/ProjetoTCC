package org.jonasribeiro.admin.catalogo.application.category.retrieve.list;

import org.jonasribeiro.admin.catalogo.application.UseCase;
import org.jonasribeiro.admin.catalogo.domain.category.CategorySearchQuery;
import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
