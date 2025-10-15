package org.jonasribeiro.admin.catalogo.domain.event;

@FunctionalInterface
public interface DomainEventPublisher <T extends DomainEvent> {
    void publishEvent(final T event);
}
