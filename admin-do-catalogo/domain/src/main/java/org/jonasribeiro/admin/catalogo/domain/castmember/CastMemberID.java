package org.jonasribeiro.admin.catalogo.domain.castmember;

import org.jonasribeiro.admin.catalogo.domain.Identifier;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class CastMemberID extends Identifier {

    private final String value;

    private CastMemberID(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static CastMemberID unique() {
        return new CastMemberID(java.util.UUID.randomUUID().toString());
    }

    public static CastMemberID from(final String anId) {
        return new CastMemberID(anId);
    }

    public static CastMemberID from(final UUID anId) {
        return new CastMemberID(anId.toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CastMemberID that = (CastMemberID) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
