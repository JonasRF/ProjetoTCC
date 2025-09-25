package org.jonasribeiro.admin.catalogo.application.castmember.create;

import org.jonasribeiro.admin.catalogo.application.UseCase;

public sealed abstract class CreateCastMemberUseCase
 extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
 permits DefaultCreateCastMemberUseCase {
}
