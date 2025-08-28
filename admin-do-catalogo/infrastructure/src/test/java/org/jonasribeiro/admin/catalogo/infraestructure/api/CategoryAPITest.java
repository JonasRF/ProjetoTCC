package org.jonasribeiro.admin.catalogo.infraestructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jonasribeiro.admin.catalogo.ControllerTest;
import org.jonasribeiro.admin.catalogo.application.category.create.CreateCategoryOutput;
import org.jonasribeiro.admin.catalogo.application.category.create.DefaultCreateCategoryUseCase;
import org.jonasribeiro.admin.catalogo.application.category.delete.DefaultDeleteCategoryUseCase;
import org.jonasribeiro.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import org.jonasribeiro.admin.catalogo.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import org.jonasribeiro.admin.catalogo.application.category.retrieve.list.CategoryListOutput;
import org.jonasribeiro.admin.catalogo.application.category.retrieve.list.DefaultListCategoriesUseCase;
import org.jonasribeiro.admin.catalogo.application.category.update.DefaultUpdateCategoryUseCase;
import org.jonasribeiro.admin.catalogo.application.category.update.UpdateCategoryOutput;
import org.jonasribeiro.admin.catalogo.domain.category.Category;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;
import org.jonasribeiro.admin.catalogo.domain.exceptions.DomainException;
import org.jonasribeiro.admin.catalogo.domain.exceptions.NotFoundException;
import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.domain.validation.Error;
import org.jonasribeiro.admin.catalogo.domain.validation.handler.Notification;
import org.jonasribeiro.admin.catalogo.infraestructure.category.models.CreateCategoryRequest;
import org.jonasribeiro.admin.catalogo.infraestructure.category.models.UpdateCategoryRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DefaultCreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private DefaultGetCategoryByIdUseCase getCategoryByIdUseCase;

    @MockBean
    private DefaultUpdateCategoryUseCase updateCategoryUseCase;

    @MockBean
    private DefaultDeleteCategoryUseCase deleteCategoryUseCase;

    @MockBean
    private DefaultListCategoriesUseCase listCategoriesUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_thenShouldReturnCategoryId() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aInput = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(Right(CreateCategoryOutput.from(("123"))));

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType("application/json")
                .content(this.objectMapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/categories/123"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo("123")));

        verify(createCategoryUseCase, times(1))
                .execute(argThat(cmd ->
                        Objects.equals(expectedName, cmd.name()) &&
                                Objects.equals(expectedDescription, cmd.description()) &&
                                Objects.equals(expectedIsActive, cmd.isActive())
                ));
    }

    @Test
    public void givenAnInvalidName_whenCallsCreateCategory_thenShouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedMessage = "'Name' should not be null";

        final var aInput = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(createCategoryUseCase, times(1))
                .execute(argThat(cmd ->
                        Objects.equals(expectedName, cmd.name()) &&
                                Objects.equals(expectedDescription, cmd.description()) &&
                                Objects.equals(expectedIsActive, cmd.isActive())
                ));
    }

    @Test
    public void givenAnInvalidCommand_whenCallsCreateCategory_thenShouldReturnDomainException() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedMessage = "'Name' should not be null";

        final var aInput = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedMessage)));

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedMessage)))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(createCategoryUseCase, times(1))
                .execute(argThat(cmd ->
                        Objects.equals(expectedName, cmd.name()) &&
                                Objects.equals(expectedDescription, cmd.description()) &&
                                Objects.equals(expectedIsActive, cmd.isActive())
                ));
    }

    @Test
    public void givenAValidId_whenCallsCreateCategory_thenShouldReturnCategory() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectedId = aCategory.getId().getValue();

        when(getCategoryByIdUseCase.execute(any()))
                .thenReturn(CategoryOutput.from(aCategory));

        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                .andExpect(jsonPath("$.created_at", equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aCategory.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(aCategory.getDeletedAt())));

        verify(getCategoryByIdUseCase, times(1))
                .execute(eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsGetCategoryById_thenShouldReturnNotFound() throws Exception {
        //given
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = CategoryID.from("123");

        when(getCategoryByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, expectedId));

        //when
        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final  var response = this.mvc.perform(request)
                .andDo(print());

        //then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_thenShouldReturnCategoryId() throws Exception {
        //given
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Right(UpdateCategoryOutput.from((expectedId))));

        final var aCommand = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .contentType("application/json")
                .content(this.objectMapper.writeValueAsString(aCommand));

        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));


        verify(updateCategoryUseCase, times(1))
                .execute(argThat(cmd ->
                                Objects.equals(expectedName, cmd.name()) &&
                                Objects.equals(expectedDescription, cmd.description()) &&
                                Objects.equals(expectedIsActive, cmd.isActive())
                ));
    }

    @Test
    public void givenCommandWithInvalidID_whenCallsUpdateCategory_ShouldReturnDomainExceptionException() throws Exception {
        //given
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Category with ID 123 was not found";

        when(updateCategoryUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, CategoryID.from(expectedId)));

        final var aCommand = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        //when
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(aCommand));

       final var response =  this.mvc.perform(request)
                .andDo(print());

                response.andExpect(status().isNotFound());
                response.andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));
                response.andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateCategoryUseCase, times(1))
                .execute(argThat(cmd ->
                        Objects.equals(expectedName, cmd.name()) &&
                                Objects.equals(expectedDescription, cmd.description()) &&
                                Objects.equals(expectedIsActive, cmd.isActive())
                ));
    }
    @Test
    public void givenValidId_whenCallsDeleteCategory_shouldReturnNoCOntent() throws Exception {
        //given
        final var expectedId = "123";

        doNothing().when(deleteCategoryUseCase).execute(any());

        //when
        final var request = MockMvcRequestBuilders.delete("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        //then
                response.andExpect(status().isNoContent());

        verify(deleteCategoryUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenAValidParams_whenCallsListCategories_shouldReturnCategoriesFiltered() throws Exception {
        //given
        final var aCategory = Category.newCategory("Movies", null, true);
        final String expectedTerms = "movies";
        final int expectedPage = 0;
        final int expectedPerPage = 10;
        final String expectedSort = "description";
        final String expectedDirection = "asc";
        final int expectedItemsCount = 1;
        final int expectedTotal = 1;
        final var expectedItems = List.of(CategoryListOutput.from(aCategory));

        when(listCategoriesUseCase.execute(any()))
                .thenReturn(new Pagination<>(
                        expectedPage,
                        expectedPerPage,
                        expectedTotal,
                        expectedItems
                ));

        //when
        final var request = MockMvcRequestBuilders.get("/categories")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("search", expectedTerms)
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        //then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aCategory.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(aCategory.getName())))
                .andExpect(jsonPath("$.items[0].description", equalTo(aCategory.getDescription())))
                .andExpect(jsonPath("$.items[0].is_active", equalTo(aCategory.isActive())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(jsonPath("$.items[0].deleted_at", equalTo(aCategory.getDeletedAt())));

        verify(listCategoriesUseCase, times(1))
                .execute(argThat(query ->
                        Objects.equals(expectedTerms, query.terms()) &&
                                Objects.equals(expectedPage, query.page()) &&
                                Objects.equals(expectedPerPage, query.perPage()) &&
                                Objects.equals(expectedSort, query.sort()) &&
                                Objects.equals(expectedDirection, query.direction())
                ));
    }
}
