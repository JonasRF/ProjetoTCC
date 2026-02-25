package org.jonasribeiro.admin.catalogo.infraestructure.genre.presenters;

import org.jonasribeiro.admin.catalogo.application.genre.retrieve.get.GetGenreIdOutput;
import org.jonasribeiro.admin.catalogo.application.genre.retrieve.list.ListGenreOutput;
import org.jonasribeiro.admin.catalogo.infraestructure.genre.models.GenreListResponse;
import org.jonasribeiro.admin.catalogo.infraestructure.genre.models.GenreResponse;

public interface GenreApiPresenter {

    static GenreResponse present(final GetGenreIdOutput output) {
        return new GenreResponse(
                output.id(),
                output.name(),
                output.categories(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static GenreListResponse present(final ListGenreOutput output) {
        return new GenreListResponse(
                output.id(),
                output.name(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
