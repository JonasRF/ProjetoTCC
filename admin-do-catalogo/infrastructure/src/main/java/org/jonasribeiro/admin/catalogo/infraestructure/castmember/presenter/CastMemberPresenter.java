package org.jonasribeiro.admin.catalogo.infraestructure.castmember.presenter;

import org.jonasribeiro.admin.catalogo.application.castmember.retrieve.get.CastMemberOutput;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMember;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.models.CastMemberResponse;

public class CastMemberPresenter {
    public static CastMemberResponse present(final CastMemberOutput aMember) {
        return new CastMemberResponse(
                aMember.id(),
                aMember.name(),
                aMember.type().name(),
                aMember.createdAt().toString(),
                aMember.updatedAt().toString()
        );
    }
}
