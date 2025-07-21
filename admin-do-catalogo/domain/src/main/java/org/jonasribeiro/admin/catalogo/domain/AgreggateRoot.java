package org.jonasribeiro.admin.catalogo.domain;

public class AgreggateRoot<ID extends Identifier> extends Entity<ID> {

    protected AgreggateRoot(final ID id) {
        super(id);
    }
}
