package org.jonasribeiro.admin.catalogo.application.castmember.delete;

import org.jonasribeiro.admin.catalogo.application.UnitUseCase;

public sealed abstract class DeleteCastMemberUseCase
 extends UnitUseCase<String>
permits DefaultDeleteCastMemberUseCase {
}
