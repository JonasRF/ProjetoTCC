package org.jonasribeiro.admin.catalogo.domain.category;

import org.jonasribeiro.admin.catalogo.domain.Identifier;
import org.jonasribeiro.admin.catalogo.domain.utils.IdUtils;

import java.util.Objects;

public class CategoryID extends Identifier {
    private final String value;

    private CategoryID(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static CategoryID unique() {
        return CategoryID.from(IdUtils.uuid());
    }

    public static CategoryID from(final String anId) {
        return new CategoryID(anId);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryID that = (CategoryID) o;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
