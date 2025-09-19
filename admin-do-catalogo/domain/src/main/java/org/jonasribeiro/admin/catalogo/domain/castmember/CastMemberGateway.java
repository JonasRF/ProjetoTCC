package org.jonasribeiro.admin.catalogo.domain.castmember;

import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.domain.pagination.SearchQuery;

public interface CastMemberGateway {

    CastMember create(CastMember aCastMember);

    CastMember update(CastMember aCastMember);

    void deleteById(CastMemberID anId);

    CastMember findById(CastMemberID anId);

    Pagination<CastMember> findAll(SearchQuery aQuery);
}
