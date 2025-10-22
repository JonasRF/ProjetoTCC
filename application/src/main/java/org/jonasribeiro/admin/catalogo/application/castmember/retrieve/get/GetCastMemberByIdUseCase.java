package org.jonasribeiro.admin.catalogo.application.castmember.retrieve.get;

import org.jonasribeiro.admin.catalogo.application.UseCase;

public sealed abstract class GetCastMemberByIdUseCase
extends UseCase<String, CastMemberOutput>
permits DefaultGetCastMemberUseCase {
}
