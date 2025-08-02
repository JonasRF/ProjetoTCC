package org.jonasribeiro.admin.catalogo.application.category.create;

import org.jonasribeiro.admin.catalogo.domain.category.Category;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;

public record CreateCategoryOutput(
        CategoryID id
) {

    public static CreateCategoryOutput from(
            final Category aCategory) {
        return new CreateCategoryOutput(aCategory.getId());
    }
}
