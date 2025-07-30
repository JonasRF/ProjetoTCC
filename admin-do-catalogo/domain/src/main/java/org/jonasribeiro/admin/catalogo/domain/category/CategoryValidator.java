package org.jonasribeiro.admin.catalogo.domain.category;

import org.jonasribeiro.admin.catalogo.domain.validation.Error;
import org.jonasribeiro.admin.catalogo.domain.validation.ValidationHandler;
import org.jonasribeiro.admin.catalogo.domain.validation.Validator;

import java.security.PrivateKey;

public class CategoryValidator extends Validator {

    private Category category;
    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 3;

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
        } else if (this.category.getName().length() > NAME_MAX_LENGTH || this.category.getName().length() < NAME_MIN_LENGTH) {
            this.validationHandler().append(new Error("'name' must not be less than 3 and greater than 255 characters"));
        }
    }
}
