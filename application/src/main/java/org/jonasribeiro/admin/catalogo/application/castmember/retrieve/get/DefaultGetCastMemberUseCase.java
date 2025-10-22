package org.jonasribeiro.admin.catalogo.application.castmember.retrieve.get;

import org.jonasribeiro.admin.catalogo.domain.castmember.CastMember;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberGateway;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberID;
import org.jonasribeiro.admin.catalogo.domain.exceptions.NotFoundException;

public non-sealed class DefaultGetCastMemberUseCase extends  GetCastMemberByIdUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultGetCastMemberUseCase(CastMemberGateway castMemberGateway) {
        this.castMemberGateway = castMemberGateway;
    }

    @Override
    public CastMemberOutput execute(final String anIn) {
        final var aMemberId = CastMemberID.from(anIn);
        return this.castMemberGateway.findById(aMemberId)
            .map(CastMemberOutput::from)
            .orElseThrow(() -> NotFoundException.with(CastMember.class, aMemberId));
    }
}
