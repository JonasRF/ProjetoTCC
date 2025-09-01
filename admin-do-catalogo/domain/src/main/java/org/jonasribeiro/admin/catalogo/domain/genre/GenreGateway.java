package org.jonasribeiro.admin.catalogo.domain.genre;

import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.domain.pagination.SearchQuery;

import java.util.Optional;

public interface GenreGateway {

    Genre create(Genre aGenre);

    void deleteById(GenreID anId);

    Genre update(Genre aGenre);

    Optional<Genre> findById(GenreID anId);

    Pagination<Genre> findAll(SearchQuery aQuery);
}
