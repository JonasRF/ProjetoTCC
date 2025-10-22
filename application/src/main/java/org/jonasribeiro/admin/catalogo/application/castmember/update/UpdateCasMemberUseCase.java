package org.jonasribeiro.admin.catalogo.application.castmember.update;

import org.jonasribeiro.admin.catalogo.application.UseCase;

public sealed  abstract class UpdateCasMemberUseCase
  extends UseCase<UpdateCastMemberCommand, UpdateCastMemberOutput>
permits DefaultUpdateCastMemberUseCase {
}
