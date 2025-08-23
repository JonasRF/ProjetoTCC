package org.jonasribeiro.admin.catalogo.infraestructure.api.controllers;

import org.jonasribeiro.admin.catalogo.application.category.create.CreateCategoryCommand;
import org.jonasribeiro.admin.catalogo.application.category.create.CreateCategoryOutput;
import org.jonasribeiro.admin.catalogo.application.category.create.CreateCategoryUseCase;
import org.jonasribeiro.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import org.jonasribeiro.admin.catalogo.application.category.retrieve.get.GetCategoryByUseCase;
import org.jonasribeiro.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import org.jonasribeiro.admin.catalogo.application.category.update.UpdateCategoryCommand;
import org.jonasribeiro.admin.catalogo.application.category.update.UpdateCategoryOutput;
import org.jonasribeiro.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import org.jonasribeiro.admin.catalogo.domain.category.CategorySearchQuery;
import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.domain.validation.handler.Notification;
import org.jonasribeiro.admin.catalogo.infraestructure.api.CategoryAPI;
import org.jonasribeiro.admin.catalogo.infraestructure.category.models.CreateCategoryApiOutput;
import org.jonasribeiro.admin.catalogo.infraestructure.category.models.CreateCategoryApiInput;
import org.jonasribeiro.admin.catalogo.infraestructure.category.models.UpdateCategoryApiInput;
import org.jonasribeiro.admin.catalogo.infraestructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByUseCase getCategoryByUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(
            final CreateCategoryUseCase createCategoryUseCase,
            final GetCategoryByUseCase getCategoryByUseCase, UpdateCategoryUseCase updateCategoryUseCase, DeleteCategoryUseCase deleteCategoryUseCase, ListCategoriesUseCase listCategoriesUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByUseCase = Objects.requireNonNull(getCategoryByUseCase);
        this.updateCategoryUseCase = updateCategoryUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
        this.listCategoriesUseCase = listCategoriesUseCase;
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryApiInput input) {
       final var aCommand = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

       final Function<Notification, ResponseEntity<?>> onError = notification -> ResponseEntity
                .unprocessableEntity()
                .body(notification);

       final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
             ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return this.createCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public Pagination<?> listCategories(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        return this.listCategoriesUseCase.execute(
                new CategorySearchQuery(
                        page,
                        perPage,
                        search,
                        sort,
                        direction
                ));
    }

    @Override
    public CreateCategoryApiOutput getById(String id) {
        return CategoryApiPresenter.present(this.getCategoryByUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> UpdateById(final String id, final UpdateCategoryApiInput input) {
        final var aCommand = UpdateCategoryCommand.with(
                id,
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = notification -> ResponseEntity
                .unprocessableEntity()
                .body(notification);

        final Function<? super UpdateCategoryOutput, ? extends ResponseEntity<?>> onSuccess = ResponseEntity::ok;

        return this.updateCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public void deleteById(final String id) {
        this.deleteCategoryUseCase.execute(id);
    }
}
