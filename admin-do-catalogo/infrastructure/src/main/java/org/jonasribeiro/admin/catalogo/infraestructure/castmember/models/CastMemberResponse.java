package org.jonasribeiro.admin.catalogo.infraestructure.castmember.models;

import org.jonasribeiro.admin.catalogo.application.castmember.retrieve.get.CastMemberOutput;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberType;

public record CastMemberResponse(
        String id,
        String name,
        String type,
        String createdAt,
        String updatedAt
) {
}