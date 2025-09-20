package org.jonasribeiro.admin.catalogo.application.castmember.retrieve.get;

import org.jonasribeiro.admin.catalogo.domain.castmember.CastMember;

import java.time.Instant;

public record CastMemberOutput(
    String id,
    String name,
    String type,
    Instant createdAt,
    Instant updatedAt
) {
    public static CastMemberOutput from(final CastMember aMember) {;
        return new CastMemberOutput(
            aMember.getId().getValue(),
            aMember.getName(),
            aMember.getType().name(),
            aMember.getCreatedAt(),
            aMember.getUpdatedAt()
        );
    }
}
