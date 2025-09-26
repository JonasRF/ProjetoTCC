package org.jonasribeiro.admin.catalogo.infraestructure.castmember.presenter;

import org.jonasribeiro.admin.catalogo.application.castmember.retrieve.get.CastMemberOutput;
import org.jonasribeiro.admin.catalogo.application.castmember.retrieve.list.CastMemberListOutput;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.models.CastMemberListResponse;
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

    public static CastMemberListResponse present(final CastMemberListOutput aMember) {
        return new CastMemberListResponse(
                aMember.id(),
                aMember.name(),
                aMember.type().name(),
                aMember.createdAt().toString()
        );
    }
}
