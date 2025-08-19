package org.jonasribeiro.admin.catalogo.infraestructure.api.controllers;

import org.jonasribeiro.admin.catalogo.domain.category.CategoryGateway;
import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.infraestructure.api.CategoryAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController implements CategoryAPI {
    @Override
    public ResponseEntity<?> createCategory() {
        return null;
    }

    @Override
    public Pagination<?> listCategories(String search, int page, int perPage, String sort, String direction) {
        return null;
    }
}
