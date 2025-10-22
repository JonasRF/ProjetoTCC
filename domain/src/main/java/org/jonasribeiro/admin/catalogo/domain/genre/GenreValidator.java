package org.jonasribeiro.admin.catalogo.domain.genre;

import org.jonasribeiro.admin.catalogo.domain.validation.Error;
import org.jonasribeiro.admin.catalogo.domain.validation.ValidationHandler;
import org.jonasribeiro.admin.catalogo.domain.validation.Validator;

public class GenreValidator extends Validator {

    private final  Genre genre;
    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 3;

    protected GenreValidator(final Genre aGenre, ValidationHandler aHandler) {
        super(aHandler);
        this.genre = aGenre;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        if (this.genre.getName() == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
        } else if (this.genre.getName().isBlank()) {
            this.validationHandler().append(new Error("'name' should not be empty"));
        } else if (this.genre.getName().length() > NAME_MAX_LENGTH || this.genre.getName().length() < NAME_MIN_LENGTH) {
            this.validationHandler().append(new Error("'name' must not be less than 1 and greater than 255 characters"));
        }
    }
}
