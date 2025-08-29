package org.jonasribeiro.admin.catalogo.domain;

import org.jonasribeiro.admin.catalogo.domain.validation.ValidationHandler;

public class AggregateRoot<ID extends Identifier> extends Entity<ID> {

    protected AggregateRoot(final ID id) {
        super(id);
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("'handler' should not be null");
        }
        // Default implementation does nothing, subclasses can override to provide validation logic
    }
}
