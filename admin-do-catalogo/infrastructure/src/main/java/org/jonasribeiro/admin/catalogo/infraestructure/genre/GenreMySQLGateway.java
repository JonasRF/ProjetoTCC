package org.jonasribeiro.admin.catalogo.infraestructure.genre;

import org.jonasribeiro.admin.catalogo.domain.genre.Genre;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreGateway;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreID;
import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.domain.pagination.SearchQuery;
import org.jonasribeiro.admin.catalogo.infraestructure.genre.persistence.GenreJpaEntity;
import org.jonasribeiro.admin.catalogo.infraestructure.genre.persistence.GenreRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class GenreMySQLGateway implements GenreGateway {

    private final GenreRepository genreRepository;

    public GenreMySQLGateway(GenreRepository genreRepository) {
        this.genreRepository = Objects.requireNonNull(genreRepository);
    }

    @Override
    public Genre create(final Genre aGenre) {
        return save(aGenre);
    }

    @Override
    public void deleteById(GenreID anId) {
    }

    @Override
    public Optional<Genre> findById(GenreID anId) {
        return Optional.empty();
    }

    @Override
    public Genre update(Genre aGenre) {
        return null;
    }

    @Override
    public Pagination<Genre> findAll(SearchQuery aQuery) {
        return null;
    }

    private Genre save(final Genre aGenre) {
        return this.genreRepository.save(GenreJpaEntity.from(aGenre)).toAggregate();
    }
}
