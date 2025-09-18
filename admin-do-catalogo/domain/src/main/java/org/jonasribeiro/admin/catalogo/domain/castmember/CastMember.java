package org.jonasribeiro.admin.catalogo.domain.castmember;

import org.jonasribeiro.admin.catalogo.domain.AggregateRoot;
import org.jonasribeiro.admin.catalogo.domain.exceptions.NotificationException;
import org.jonasribeiro.admin.catalogo.domain.validation.ValidationHandler;
import org.jonasribeiro.admin.catalogo.domain.validation.handler.Notification;

import java.time.Instant;

public class CastMember  extends AggregateRoot<CastMemberID> {

    private String name;
    private CastMemberType type;
    private Instant createdAt;
    private Instant updatedAt;

    protected CastMember(
            final CastMemberID anId,
            final String aName,
            final CastMemberType aType,
            final Instant aCreationDate,
            final Instant aUpdateDate
    ) {
        super(anId);
        this.name = aName;
        this.type = aType;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdateDate;
        selfValidate();
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CastMemberValidator(this, handler).validate();
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);
        if (notification.hasErrors()) {
            throw new NotificationException("Failed to create a Aggregate CastMember", notification);
        }
    }
}