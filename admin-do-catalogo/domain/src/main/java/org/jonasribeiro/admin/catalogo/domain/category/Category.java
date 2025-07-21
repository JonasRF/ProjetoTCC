package org.jonasribeiro.admin.catalogo.domain.category;

import java.time.Instant;
import java.util.UUID;

public class Category {
    private String id;
    private String name;
    private String description;
    private boolean isActive;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    private Category(final String id,
                    final String name,
                    final String description,
                    final boolean isActive,
                    final String createdAt,
                    final String updatedAt,
                    final String deletedAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static Category newCategory(final String aName,
                                       final String aDescription,
                                       final boolean isActive) {
        final var id = UUID.randomUUID().toString();
        final var now = Instant.now().toString();
        return new Category(
                id,
                aName,
                aDescription,
                isActive,
                now,
               now,
                null
        );
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }
}
