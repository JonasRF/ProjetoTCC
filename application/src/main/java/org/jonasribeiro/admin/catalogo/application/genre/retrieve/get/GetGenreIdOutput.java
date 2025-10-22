package org.jonasribeiro.admin.catalogo.application.genre.retrieve.get;

import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;
import org.jonasribeiro.admin.catalogo.domain.genre.Genre;

import java.time.Instant;
import java.util.List;

public record GetGenreIdOutput (
        String id,
        String name,
        boolean isActive,
        List<String> categories,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
){
    public static GetGenreIdOutput from(final Genre aGenre) {
        return new GetGenreIdOutput(
                aGenre.getId().getValue(),
                aGenre.getName(),
                aGenre.isActive(),
                aGenre.getCategories().stream()
                        .map(CategoryID::getValue)
                        .toList(),
                aGenre.getCreatedAt(),
                aGenre.getUpdatedAt(),
                aGenre.getDeletedAt()
        );
    }
}

