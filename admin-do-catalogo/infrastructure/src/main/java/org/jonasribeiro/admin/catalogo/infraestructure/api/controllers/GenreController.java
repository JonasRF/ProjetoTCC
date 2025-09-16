package org.jonasribeiro.admin.catalogo.infraestructure.api.controllers;

import org.jonasribeiro.admin.catalogo.application.genre.create.CreateGenreCommand;
import org.jonasribeiro.admin.catalogo.application.genre.create.CreateGenreUseCase;
import org.jonasribeiro.admin.catalogo.application.genre.retrieve.get.GetGenreIdUseCase;
import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.infraestructure.api.GenreAPI;
import org.jonasribeiro.admin.catalogo.infraestructure.genre.models.CreateGenreRequest;
import org.jonasribeiro.admin.catalogo.infraestructure.genre.models.GenreListResponse;
import org.jonasribeiro.admin.catalogo.infraestructure.genre.models.GenreResponse;
import org.jonasribeiro.admin.catalogo.infraestructure.genre.models.UpdateGenreRequest;
import org.jonasribeiro.admin.catalogo.infraestructure.genre.presenters.GenreApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class GenreController implements GenreAPI {

    private final CreateGenreUseCase createGenreUseCase;
    private final GetGenreIdUseCase GetGenreIdUseCase;

    public GenreController(CreateGenreUseCase createGenreUseCase, GetGenreIdUseCase getGenreIdUseCase) {
        this.createGenreUseCase = createGenreUseCase;
        GetGenreIdUseCase = getGenreIdUseCase;
    }

    @Override
    public ResponseEntity<?> create(final CreateGenreRequest input) {
        final var aCommand = CreateGenreCommand.with(
                input.name(),
                input.active(),
                input.categories()
        );

        final var output = this.createGenreUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/genres/" + output.id())).body(output);
    }

    @Override
    public Pagination<GenreListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction) {
        return null;
    }

    @Override
    public GenreResponse getById(final String id) {
        return GenreApiPresenter.present(this.GetGenreIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final  UpdateGenreRequest input) {
        return null;
    }

    @Override
    public void deleteById(final String id) {

    }
}
