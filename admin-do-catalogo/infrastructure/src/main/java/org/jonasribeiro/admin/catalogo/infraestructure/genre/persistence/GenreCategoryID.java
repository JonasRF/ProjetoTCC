package org.jonasribeiro.admin.catalogo.infraestructure.genre.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class GenreCategoryID {

    @Column(name = "category_id", nullable = false)
    private String categoryId;

    @Column(name = "genre_id", nullable = false)
    private String genreId;

    public GenreCategoryID() {
    }

    private GenreCategoryID(final String genreId, final String categoryId) {
        this.categoryId = categoryId;
        this.genreId = genreId;
    }

    public static GenreCategoryID from(final String genreId, final String categoryId) {
        return new GenreCategoryID(genreId, categoryId);
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getGenreId() {
        return genreId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GenreCategoryID that = (GenreCategoryID) o;
        return Objects.equals(categoryId, that.categoryId) && Objects.equals(genreId, that.genreId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, genreId);
    }
}
