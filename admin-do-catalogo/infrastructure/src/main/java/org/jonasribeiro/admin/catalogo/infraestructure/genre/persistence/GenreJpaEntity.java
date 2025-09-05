package org.jonasribeiro.admin.catalogo.infraestructure.genre.persistence;

import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;
import org.jonasribeiro.admin.catalogo.domain.genre.Genre;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreID;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Entity(name = "Genre")
@Table(name = "genres")
public class GenreJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active", nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "genre", cascade = ALL, fetch = EAGER, orphanRemoval = true)
    private Set<GenreCategoryJpaEntity> categories;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    private Instant updatedAt;

    @Column(name = "deleted_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    private Instant deletedAt;

    public GenreJpaEntity() {
    }

    private GenreJpaEntity(
            final String id,
            final String name,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.categories = new HashSet<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }



    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public Set<GenreCategoryJpaEntity> getCategories() {
        return categories;
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

    public void setId(final String id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public static GenreJpaEntity with(final Genre aGenre) {
        final var entity = new GenreJpaEntity(
                aGenre.getId().getValue(),
                aGenre.getName(),
                aGenre.isActive(),
                aGenre.getCreatedAt(),
                aGenre.getUpdatedAt(),
                aGenre.getDeletedAt()
        );
        aGenre.getCategories()
                .forEach(entity::addCategory);

        return entity;
    }
    public static GenreJpaEntity from(final Genre aGenre) {
        return new GenreJpaEntity(
                aGenre.getId().getValue(),
                aGenre.getName(),
                aGenre.isActive(),
                aGenre.getCreatedAt(),
                aGenre.getUpdatedAt(),
                aGenre.getDeletedAt()
        );
    }

    public Genre toAggregate() {
        return Genre.with(
                GenreID.from(getId()),
                getName(),
                isActive(),
                getCategoryIDs(),
                getCreatedAt(),
                getUpdatedAt(),
                getDeletedAt()
        );
    }

    public List<CategoryID> getCategoryIDs() {
       return getCategories().stream()
                .map(it -> CategoryID.from(it.getId().getCategoryId()))
                .toList();
}


    public void addCategory(final CategoryID anId) {
        this.categories.add(GenreCategoryJpaEntity.from(this, anId));
    }

    public void removeCategory(final CategoryID anId) {
        this.categories.remove(GenreCategoryJpaEntity.from(this, anId));
    }

    public void setCategories(final Set<GenreCategoryJpaEntity> categories) {
        this.categories = categories;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDeletedAt(final Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
