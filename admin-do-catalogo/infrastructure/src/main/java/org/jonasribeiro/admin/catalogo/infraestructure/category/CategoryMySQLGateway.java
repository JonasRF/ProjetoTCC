package org.jonasribeiro.admin.catalogo.infraestructure.category;

import org.jonasribeiro.admin.catalogo.domain.category.Category;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryGateway;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;
import org.jonasribeiro.admin.catalogo.domain.pagination.SearchQuery;
import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.infraestructure.category.persistence.CategoryJpaEntity;
import org.jonasribeiro.admin.catalogo.infraestructure.category.persistence.CategoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.hibernate.criterion.Restrictions.like;
import static org.jonasribeiro.admin.catalogo.infraestructure.utils.SpecificationUtils.like;

@Service
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository categoryRepository;

    public CategoryMySQLGateway(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(Category aCategory) {
        return save(aCategory);
    }

    @Override
    public void deleteById(final CategoryID anId) {
        if (this.categoryRepository.existsById(anId.getValue())) {
            this.categoryRepository.deleteById(anId.getValue());
        }
    }

    @Override
    public Optional<Category> findById(CategoryID anId) {
        return this.categoryRepository
                .findById(anId.getValue())
                .map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Category update(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public Pagination<Category> findAll(SearchQuery aQuery) {
        //Paginação
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        //Busca dinâmica pelo critério terms(name ou descripition)
       final var specification = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(str -> {
                        final Specification<CategoryJpaEntity> nameLike = like("name", str);
                        final Specification<CategoryJpaEntity> descriptionLike = like("description", str);
                        return Specification.where(nameLike).or(descriptionLike);
                })
                .orElse(null);

        final var pageResult = this.categoryRepository.findAll(Specification.where(specification), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAggregate).toList()
        );
    }

    private Category save(Category aCategory) {
        return this.categoryRepository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }
}
