package org.jonasribeiro.admin.catalogo.domain.category;

import org.jonasribeiro.admin.catalogo.domain.validation.Error;
import org.jonasribeiro.admin.catalogo.domain.validation.ValidationHandler;
import org.jonasribeiro.admin.catalogo.domain.validation.Validator;

public class CategoryValidator extends Validator {

    private Category category;

    public CategoryValidator( final Category aCategory, ValidationHandler aHandler) {
        super(aHandler);
        this.category = aCategory;
    }

    @Override
    public void validate() {
    if (this.category.getName() == null) {
            this.validationHandler().append( new Error("'name' should not be null"));
        }
    }
}
