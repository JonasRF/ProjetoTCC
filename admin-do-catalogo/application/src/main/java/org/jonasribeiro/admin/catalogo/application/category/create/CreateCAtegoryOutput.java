package org.jonasribeiro.admin.catalogo.application.category.create;

import org.jonasribeiro.admin.catalogo.domain.category.Category;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;

public record CreateCAtegoryOutput(
        CategoryID id
) {

    public static CreateCAtegoryOutput from(
            final Category aCategory) {
        return new CreateCAtegoryOutput(aCategory.getId());
    }
}
