package org.jonasribeiro.admin.catalogo.domain.validation.handler;

import org.jonasribeiro.admin.catalogo.domain.exceptions.DomainException;
import org.jonasribeiro.admin.catalogo.domain.validation.Error;
import org.jonasribeiro.admin.catalogo.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {
    @Override
    public ValidationHandler append(final Error anError) {
        throw DomainException.with(anError);
    }

    @Override
    public ValidationHandler append(ValidationHandler anHandler) {
       throw DomainException.with(anHandler.getErrors());
    }

    @Override
    public <T> T validate(final Validation<T> aValidation) {
        try{
           return aValidation.validate();
        } catch (final DomainException ex) {
           throw DomainException.with(List.of(new Error(ex.getMessage())));
        }
    }

    @Override
    public List<Error> getErrors() {
        return List.of();
    }
}
