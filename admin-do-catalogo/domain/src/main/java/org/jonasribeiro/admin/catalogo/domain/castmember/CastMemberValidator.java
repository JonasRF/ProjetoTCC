package org.jonasribeiro.admin.catalogo.domain.castmember;

import org.jonasribeiro.admin.catalogo.domain.validation.ValidationHandler;
import org.jonasribeiro.admin.catalogo.domain.validation.Validator;

public class CastMemberValidator extends Validator {

    private final CastMember castMember;

    public CastMemberValidator(CastMember castMember, ValidationHandler handler) {
        super(handler);
        this.castMember = castMember;
    }

    @Override
    public void validate() {

    }
}
