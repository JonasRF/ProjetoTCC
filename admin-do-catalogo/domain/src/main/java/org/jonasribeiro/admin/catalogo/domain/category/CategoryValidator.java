package org.jonasribeiro.admin.catalogo.domain.category;

import org.jonasribeiro.admin.catalogo.domain.validation.Error;
import org.jonasribeiro.admin.catalogo.domain.validation.ValidationHandler;
import org.jonasribeiro.admin.catalogo.domain.validation.Validator;

public class CategoryValidator extends Validator {

    private Category category;

    public CategoryValidator(final Category aCategory, ValidationHandler aHandler) {
        super(aHandler);
        this.category = aCategory;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        if (this.category.getName() == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
        } else if (this.category.getName().isBlank()) {
            this.validationHandler().append(new Error("'name' should not be empty"));
        } else if (this.category.getName().length() > 255 || this.category.getName().length() < 3) {
            this.validationHandler().append(new Error("'name' must not be less than 3 and greater than 255 characters"));
        }
    }
}
