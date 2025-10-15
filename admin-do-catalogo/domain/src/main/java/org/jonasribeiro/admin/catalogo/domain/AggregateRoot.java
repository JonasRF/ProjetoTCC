package org.jonasribeiro.admin.catalogo.domain;

import org.jonasribeiro.admin.catalogo.domain.event.DomainEvent;
import org.jonasribeiro.admin.catalogo.domain.validation.ValidationHandler;

import java.util.Collections;
import java.util.List;

public class AggregateRoot<ID extends Identifier> extends Entity<ID> {

    protected AggregateRoot(final ID id) {
        super(id, Collections.emptyList());
    }

    protected AggregateRoot(final ID id, final List<DomainEvent> domainEvents) {
        super(id, domainEvents);
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("'handler' should not be null");
        }
    }
}
