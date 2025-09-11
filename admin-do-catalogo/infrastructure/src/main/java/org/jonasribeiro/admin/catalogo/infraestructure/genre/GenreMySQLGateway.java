package org.jonasribeiro.admin.catalogo.infraestructure.genre;

import org.jonasribeiro.admin.catalogo.domain.genre.Genre;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreGateway;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreID;
import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.domain.pagination.SearchQuery;
import org.jonasribeiro.admin.catalogo.infraestructure.genre.persistence.GenreJpaEntity;
import org.jonasribeiro.admin.catalogo.infraestructure.genre.persistence.GenreRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static org.jonasribeiro.admin.catalogo.infraestructure.utils.SpecificationUtils.like;

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
        this.genreRepository.deleteById(anId.getValue());
    }

    @Override
    public Optional<Genre> findById(GenreID anId) {
        return this.genreRepository.findById(anId.getValue()).map(GenreJpaEntity::toAggregate);
    }

    @Override
    public Genre update(Genre aGenre) {
        return save(aGenre);
    }

    @Override
    public Pagination<Genre> findAll(final SearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var whereClause = assembleSpecification(aQuery.terms());

        final var pageResult = this.genreRepository.findAll(whereClause, page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(GenreJpaEntity::toAggregate).toList()
        );
    }

    private Specification<GenreJpaEntity> assembleSpecification(final String str) {
        final Specification<GenreJpaEntity> name = like("name", str);
        final Specification<GenreJpaEntity> categoriesIdIn = (root, query, criteriaBuilder) -> {
            if (str.isBlank()) {
                return null;
            }
            query.distinct(true);
            final var join = root.join("categories");
            return criteriaBuilder.like(
                    criteriaBuilder.lower(join.get("id")),
                    "%" + str.toLowerCase() + "%"
            );
        };

        return name.or(categoriesIdIn);
    }

    private Genre save(final Genre aGenre) {
        return this.genreRepository.save(GenreJpaEntity.from(aGenre)).toAggregate();
    }
}
