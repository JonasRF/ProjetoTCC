package org.jonasribeiro.admin.catalogo.infraestructure.castmember.models;

import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberType;
import javax.validation.constraints.NotNull;

public record CreateCastMemberRequest(
    @NotNull String name,
    @NotNull CastMemberType type
) {}
