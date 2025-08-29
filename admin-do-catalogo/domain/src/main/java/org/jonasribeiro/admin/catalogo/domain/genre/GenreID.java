package org.jonasribeiro.admin.catalogo.domain.genre;

import org.jonasribeiro.admin.catalogo.domain.Identifier;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;

import java.util.Objects;
import java.util.UUID;

public class GenreID extends Identifier {
    private final String value;

    private GenreID(final String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static GenreID unique() {
        return GenreID.from(UUID.randomUUID());
    }

    public static GenreID from(final String anId) {
        if (anId == null || anId.isBlank()) {
            throw new IllegalArgumentException("Category ID cannot be null or blank");
        }
        return new GenreID(anId);
    }

    public static GenreID from(final UUID anId) {
        if (anId == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }
        return new GenreID(anId.toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryID that)) return false;
        return value.equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
