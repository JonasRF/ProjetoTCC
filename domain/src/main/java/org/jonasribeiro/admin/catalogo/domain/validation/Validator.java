package org.jonasribeiro.admin.catalogo.domain.validation;

public abstract class Validator {

    protected final ValidationHandler aHandler;

    public Validator(final ValidationHandler handler) {
        this.aHandler = handler;
    }

    public abstract void validate();

   protected ValidationHandler validationHandler(){
        return this.aHandler;
   }
}
