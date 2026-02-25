package org.jonasribeiro.admin.catalogo.application.castmember.retrieve.list;

import org.jonasribeiro.admin.catalogo.application.UseCase;
import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.domain.pagination.SearchQuery;

public sealed abstract class ListCastMembersUseCase
extends UseCase<SearchQuery, Pagination<CastMemberListOutput>>
permits DefaultListCastMembersUseCase {
}
