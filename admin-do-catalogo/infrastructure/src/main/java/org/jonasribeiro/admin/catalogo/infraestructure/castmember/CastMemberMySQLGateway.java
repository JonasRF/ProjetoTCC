package org.jonasribeiro.admin.catalogo.infraestructure.castmember;

import org.jonasribeiro.admin.catalogo.domain.castmember.CastMember;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberGateway;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberID;
import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.domain.pagination.SearchQuery;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.persistence.CastMemberJpaEntity;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.persistence.CastMemberRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CastMemberMySQLGateway implements CastMemberGateway {

    private final CastMemberRepository castMemberRepository;

    public CastMemberMySQLGateway(final CastMemberRepository castMemberRepository) {
        this.castMemberRepository = castMemberRepository;
    }

    @Override
    public CastMember create(final CastMember aCastMember) {
        return this.save(aCastMember);
    }

    @Override
    public CastMember update(final CastMember aCastMember) {
        return this.save(aCastMember);
    }

    @Override
    public void deleteById(final CastMemberID anId) {

    }

    @Override
    public Optional<CastMember> findById(final CastMemberID anId) {
        return Optional.empty();
    }

    @Override
    public Pagination<CastMember> findAll(final SearchQuery aQuery) {
        return null;
    }

    private CastMember save(final CastMember aCastMember) {
        return this.castMemberRepository.save(CastMemberJpaEntity.from(aCastMember)).toAggregate();
    }
}
