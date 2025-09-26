package org.jonasribeiro.admin.catalogo.application.castmember.update;

import org.jonasribeiro.admin.catalogo.domain.castmember.CastMember;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberID;

public record UpdateCastMemberOutput(String id) {

    public static UpdateCastMemberOutput from(final CastMember aMember) {
        return from(aMember.getId());
    }

    public static UpdateCastMemberOutput from(final CastMemberID anId) {
        return new UpdateCastMemberOutput(anId.getValue());
    }
}