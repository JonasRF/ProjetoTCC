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

    public CastMember update(final String aName, final CastMemberType aType) {
        this.name = aName;
        this.type = aType;
        this.updatedAt = Instant.now();
        selfValidate();
        return this;
    }

    public static CastMember with(
            final CastMemberID anId,
            final String aName,
            final CastMemberType aType,
            final Instant aCreationDate,
            final Instant aUpdateDate
    ) {
        return new CastMember(anId, aName, aType, aCreationDate, aUpdateDate);
    }

    public static CastMember with( final CastMember aMember) {
        return new CastMember(
                aMember.id,
                aMember.name,
                aMember.type,
                aMember.createdAt,
                aMember.updatedAt
        );
    }

    public static CastMember newMember(final String aName, final CastMemberType aType) {
        final var id = CastMemberID.unique();
        final var now = Instant.now();
        return new CastMember(id, aName, aType, now, now);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CastMemberValidator(this, handler).validate();
    }

    public String getName() {
        return name;
    }

    public CastMemberType getType() {
        return type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);
        if (notification.hasErrors()) {
            throw new NotificationException("Failed to create a Aggregate CastMember", notification);
        }
    }
}