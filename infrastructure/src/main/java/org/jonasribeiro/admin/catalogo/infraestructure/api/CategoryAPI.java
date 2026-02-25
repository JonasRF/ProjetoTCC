package org.jonasribeiro.admin.catalogo.infraestructure.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.infraestructure.category.models.CategoryListResponse;
import org.jonasribeiro.admin.catalogo.infraestructure.category.models.CategoryResponse;
import org.jonasribeiro.admin.catalogo.infraestructure.category.models.CreateCategoryRequest;
import org.jonasribeiro.admin.catalogo.infraestructure.category.models.UpdateCategoryRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "categories")
@Tag(name = "categories")
public interface CategoryAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "402", description = "Validation error, check the request body for more details"),
            @ApiResponse(responseCode = "500", description = "Internal server error, please try again later")
    })
    ResponseEntity<?> createCategory(@RequestBody CreateCategoryRequest input);

    @GetMapping
    @Operation(summary = "List categories with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories listed successfully"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter was received, check the request parameters for more details"),
            @ApiResponse(responseCode = "404", description = "No categories found for the given parameters"),
            @ApiResponse(responseCode = "400", description = "Bad request, check the request parameters for more details"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, you need to be authenticated to access this resource"),
            @ApiResponse(responseCode = "403", description = "Forbidden, you do not have permission to access this resource"),
            @ApiResponse(responseCode = "500", description = "Internal server error, please try again later")
    })
    Pagination<CategoryListResponse> listCategories(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") final String direction
    );

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE

    )
    @Operation(summary = "Get a category by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "A invalid parameter was received, check the request parameters for more details"),
            @ApiResponse(responseCode = "500", description = "An internal server error occurred, please try again later")

    })
    CategoryResponse getById(@PathVariable(name = "id") String id);

    @PutMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE

    )
    @Operation(summary = "Update a category by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category Updated successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found with the given identifier"),
            @ApiResponse(responseCode = "500", description = "An internal server error occurred, please try again later")

    })
    ResponseEntity<?> UpdateById(@PathVariable(name = "id") String id, @RequestBody UpdateCategoryRequest input);

    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a category by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found with the given identifier"),
            @ApiResponse(responseCode = "500", description = "An internal server error occurred, please try again later")
    })
    void deleteById(@PathVariable(name = "id") String id);


}