package org.jonasribeiro.admin.catalogo.infraestructure.api.controllers;

import org.jonasribeiro.admin.catalogo.application.category.create.CreateCategoryCommand;
import org.jonasribeiro.admin.catalogo.application.category.create.CreateCategoryOutput;
import org.jonasribeiro.admin.catalogo.application.category.create.CreateCategoryUseCase;
import org.jonasribeiro.admin.catalogo.application.category.retrieve.get.GetCategoryByUseCase;
import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.domain.validation.handler.Notification;
import org.jonasribeiro.admin.catalogo.infraestructure.api.CategoryAPI;
import org.jonasribeiro.admin.catalogo.infraestructure.category.models.CategoryApiOutput;
import org.jonasribeiro.admin.catalogo.infraestructure.category.models.CreateCategoryApiInput;
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

    public CategoryController(
            final CreateCategoryUseCase createCategoryUseCase,
            final GetCategoryByUseCase getCategoryByUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByUseCase = Objects.requireNonNull(getCategoryByUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(CreateCategoryApiInput input) {
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
    public Pagination<?> listCategories(String search, int page, int perPage, String sort, String direction) {
        return null;
    }

    @Override
    public CategoryApiOutput getById(String id) {
        return CategoryApiPresenter.present(this.getCategoryByUseCase.execute(id));
    }
}
