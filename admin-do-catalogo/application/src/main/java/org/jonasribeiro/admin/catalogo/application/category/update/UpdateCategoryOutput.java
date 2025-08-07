package org.jonasribeiro.admin.catalogo.application.category.update;

import org.jonasribeiro.admin.catalogo.domain.category.Category;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;

public record UpdateCategoryOutput(
    CategoryID id
) {
    public static UpdateCategoryOutput from(
            final Category aCategory
            ) {
        return new UpdateCategoryOutput(aCategory.getId());
    }
}
