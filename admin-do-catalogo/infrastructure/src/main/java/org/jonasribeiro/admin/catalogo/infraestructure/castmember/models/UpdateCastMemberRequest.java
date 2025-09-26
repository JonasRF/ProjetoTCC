package org.jonasribeiro.admin.catalogo.infraestructure.castmember.models;

import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberType;

public record UpdateCastMemberRequest(String name, CastMemberType type
) {
}
