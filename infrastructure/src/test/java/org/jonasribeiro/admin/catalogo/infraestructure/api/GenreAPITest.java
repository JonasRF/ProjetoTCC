package org.jonasribeiro.admin.catalogo.infraestructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.jonasribeiro.admin.catalogo.ControllerTest;
import org.jonasribeiro.admin.catalogo.application.genre.create.CreateGenreOutput;
import org.jonasribeiro.admin.catalogo.application.genre.create.CreateGenreUseCase;
import org.jonasribeiro.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import org.jonasribeiro.admin.catalogo.application.genre.retrieve.get.GetGenreIdOutput;
import org.jonasribeiro.admin.catalogo.application.genre.retrieve.get.GetGenreIdUseCase;
import org.jonasribeiro.admin.catalogo.application.genre.retrieve.list.ListGenreOutput;
import org.jonasribeiro.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import org.jonasribeiro.admin.catalogo.application.genre.update.UpdateGenreOutput;
import org.jonasribeiro.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;
import org.jonasribeiro.admin.catalogo.domain.exceptions.NotFoundException;
import org.jonasribeiro.admin.catalogo.domain.exceptions.NotificationException;
import org.jonasribeiro.admin.catalogo.domain.genre.Genre;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreID;
import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.domain.validation.Error;
import org.jonasribeiro.admin.catalogo.domain.validation.handler.Notification;
import org.jonasribeiro.admin.catalogo.infraestructure.genre.models.CreateGenreRequest;
import org.jonasribeiro.admin.catalogo.infraestructure.genre.models.UpdateGenreRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = GenreAPI.class)
public class GenreAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateGenreUseCase createGenreUseCase;

    @MockBean
    private GetGenreIdUseCase getGenreIdUseCase;

    @MockBean
    private UpdateGenreUseCase updateGenreUseCase;

    @MockBean
    private DeleteGenreUseCase deleteGenreUseCase;

    @MockBean
    private ListGenreUseCase listGenreUseCase;

    @Test
    public void givenACommand_whenCallsCreateGenre_shouldReturnGenreId() throws Exception {
        // given
        final var expectedName = "Ação";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedId = "123";

        final var aCommand = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        when(createGenreUseCase.execute(any()))
                .thenReturn(CreateGenreOutput.from(expectedId));

        // when
        final var aRequest =  MockMvcRequestBuilders.post("/genres")
                .contentType("application/json")
                .content(this.objectMapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/genres/" + expectedId))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(createGenreUseCase).execute(argThat(cmd ->
            Objects.equals(expectedName, cmd.name())
                    && Objects.equals(expectedCategories, cmd.categories())
                    && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsCreateGenre_thenShouldReturnNotification() throws Exception {
        // given
        final String expectedName = null;
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedMessage = "'name' should not be null";

        final var aCommand = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        when(createGenreUseCase.execute(any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedMessage))));

        // when
        final var request = MockMvcRequestBuilders.post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(aCommand));

        // then
        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(createGenreUseCase).execute(argThat(cmd ->
            Objects.equals(expectedName, cmd.name())
                    && Objects.equals(expectedCategories, cmd.categories())
                    && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAValidId_whenCallsGetGenreById_thenShouldReturnGenre() throws Exception {
        // given
        final var expectedName = "Ação";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = false;

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive)
                .addCategories(expectedCategories.stream()
                .map(CategoryID::from).toList());

        final var expectedId = aGenre.getId().getValue();

        when(getGenreIdUseCase.execute(any()))
                .thenReturn(GetGenreIdOutput.from(aGenre));

        // when
        final var aRequest =  MockMvcRequestBuilders.get("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                .andExpect(jsonPath("$.created_at", equalTo(aGenre.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aGenre.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(aGenre.getDeletedAt().toString())));
    }

    @Test
    public void givenAnInvalidId_whenCallsGetGenreById_thenShouldReturnNotFound() throws Exception {
        // given
        final var expectedId = GenreID.from("123");
        final var expectedErrorMessage = "Genre with ID 123 was not found";

        when(getGenreIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Genre.class, expectedId));

        // when
        final var aRequest =  MockMvcRequestBuilders.get("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenACommand_whenCallsUpdateGenre_shouldReturnGenreId() throws Exception {
        // given
        final var expectedName = "Ação";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;

        final var aGenre = Genre.newGenre(expectedName,expectedIsActive);
        final var expectedId = aGenre.getId().getValue();

        final var aCommand = new UpdateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        when(updateGenreUseCase.execute(any()))
                .thenReturn(UpdateGenreOutput.from(aGenre));

        // when
        final var aRequest =  MockMvcRequestBuilders.put("/genres/{id}", expectedId)
                .contentType("application/json")
                .content(this.objectMapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(updateGenreUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateGenre_thenShouldReturnNotification() throws Exception {
        // given
        final String expectedName = null;
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedMessage = "'name' should not be null";

        final var aGenre = Genre.newGenre("Ação",expectedIsActive);
        final var expectedId = aGenre.getId().getValue();

        final var aCommand = new UpdateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        when(updateGenreUseCase.execute(any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedMessage))));

        // when
        final var request = MockMvcRequestBuilders.put("/genres/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(aCommand));

        // then
        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(updateGenreUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAValidId_whenCallsDeleteGenreById_thenShouldReturnBeOk() throws Exception {
        // given
        final var aGenre = Genre.newGenre("Ação",true);
        final var expectedId = aGenre.getId().getValue();

        doNothing().when(deleteGenreUseCase).execute(any());

        // when
        final var aRequest =  MockMvcRequestBuilders.delete("/genres/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isNoContent());

        verify(deleteGenreUseCase).execute(eq(expectedId));
    }

    @Test
    public void givenAValidParams_whenCallsListGenres_thenShouldReturnGenres() throws Exception {
        // given
        final var aGenre = Genre.newGenre("Ação", false);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "ac";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(ListGenreOutput.from(aGenre));

        when(listGenreUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // when
        final var aRequest =  MockMvcRequestBuilders.get("/genres")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("search", expectedTerms)
                .queryParam("sort", expectedSort)
                .queryParam("direction", expectedDirection)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aGenre.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(aGenre.getName())))
                .andExpect(jsonPath("$.items[0].is_active", equalTo(aGenre.isActive())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aGenre.getCreatedAt().toString())));

        verify(listGenreUseCase).execute(argThat(query ->
            Objects.equals(expectedPage, query.page())
                    && Objects.equals(expectedPerPage, query.perPage())
                    && Objects.equals(expectedTerms, query.terms())
                    && Objects.equals(expectedSort, query.sort())
                    && Objects.equals(expectedDirection, query.direction())
        ));
    }

    @Test
    public void givenAValidParams_whenCallsListGenresAndResultIsEmpty_thenShouldReturnGenres() throws Exception {
        // given

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "ac";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var expectedItems = List.<ListGenreOutput>of();

        when(listGenreUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // when
        final var aRequest =  MockMvcRequestBuilders.get("/genres")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("search", expectedTerms)
                .queryParam("sort", expectedSort)
                .queryParam("direction", expectedDirection)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(expectedItemsCount)));

        verify(listGenreUseCase).execute(argThat(query ->
            Objects.equals(expectedPage, query.page())
                    && Objects.equals(expectedPerPage, query.perPage())
                    && Objects.equals(expectedTerms, query.terms())
                    && Objects.equals(expectedSort, query.sort())
                    && Objects.equals(expectedDirection, query.direction())
        ));
    }

    @Test
    public void givenAnInvalidParams_whenCallsListGenres_thenShouldReturnBadRequest() throws Exception {
        // given
        final var expectedPage = "invalid";
        final var expectedPerPage = "invalid";
        final var expectedTerms = "ac";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        // when
        final var aRequest =  MockMvcRequestBuilders.get("/genres")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("search", expectedTerms)
                .queryParam("sort", expectedSort)
                .queryParam("direction", expectedDirection)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isBadRequest());

        verify(listGenreUseCase, never()).execute(any());
    }
}
