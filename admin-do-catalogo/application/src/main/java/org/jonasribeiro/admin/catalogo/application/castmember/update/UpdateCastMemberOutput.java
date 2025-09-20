package org.jonasribeiro.admin.catalogo.application.castmember.update;

import org.jonasribeiro.admin.catalogo.domain.castmember.CastMember;

public record UpdateCastMemberOutput(
        String id
) {
    public static UpdateCastMemberOutput from(final CastMember aMember) {
        return new UpdateCastMemberOutput(aMember.getId().getValue());
    }
}