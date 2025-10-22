package org.jonasribeiro.admin.catalogo.domain.castmember;

import org.jonasribeiro.admin.catalogo.domain.validation.ValidationHandler;
import org.jonasribeiro.admin.catalogo.domain.validation.Validator;
import org.jonasribeiro.admin.catalogo.domain.validation.Error;

public class CastMemberValidator extends Validator {

    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 3;
    private final CastMember castMember;

    public CastMemberValidator(final CastMember castMember, final ValidationHandler handler) {
        super(handler);
        this.castMember = castMember;
    }

    @Override
    public void validate() {
        checkNameConstraints();
        checkTypeConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.castMember.getName();
        if (name == null || name.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }
        final int length = name.trim().length();
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            this.validationHandler().append(new Error("'name' must not be less than 3 and greater than 255 characters"));
        }
    }

    private void checkTypeConstraints() {
        final var type = this.castMember.getType();
        if (type == null) {
            this.validationHandler().append(new Error("'type' should not be null"));
        }
    }
}
