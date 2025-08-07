package org.jonasribeiro.admin.catalogo.domain.category;

import org.jonasribeiro.admin.catalogo.domain.AgreggateRoot;
import org.jonasribeiro.admin.catalogo.domain.validation.Error;
import org.jonasribeiro.admin.catalogo.domain.validation.ValidationHandler;
import org.jonasribeiro.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;

import java.time.Instant;
import java.util.UUID;

public class Category extends AgreggateRoot<CategoryID> implements Cloneable {
    private String name;
    private String description;
    private boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(final CategoryID anId,
                    final String aName,
                    final String aDescription,
                    final boolean isActive,
                    final Instant aCreationDate,
                    final Instant aupdatedDate,
                    final Instant aDeletedDate
    ) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.isActive = isActive;
        this.createdAt = aCreationDate;
        this.updatedAt = aupdatedDate;
        this.deletedAt = aDeletedDate;
    }

    public static Category newCategory(final String aName,
                                       final String aDescription,
                                       final boolean isActive) {
        final var id = CategoryID.unique();
        final var now = Instant.now();
        final var deletedAt = (isActive) ? null : now;
        return new Category(
                id,
                aName,
                aDescription,
                isActive,
                now,
                now,
                deletedAt
        );
    }

    @Override
    public void validate(ValidationHandler handler) {
       new CategoryValidator(this, handler).validate();
    }

    public Category deActivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = Instant.now();
        }
        this.isActive = false;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category activate() {
        this.isActive = true;
        this.deletedAt = null;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category update(final String aName,
                           final String aDescription,
                           final boolean isActive) {
        this.name = aName;
        this.description = aDescription;
        this.isActive = isActive;
        this.updatedAt = Instant.now();
        if (!isActive) {
            this.deletedAt = Instant.now();
        }
        return this;
    }

    public CategoryID getId() {
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    @Override
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
