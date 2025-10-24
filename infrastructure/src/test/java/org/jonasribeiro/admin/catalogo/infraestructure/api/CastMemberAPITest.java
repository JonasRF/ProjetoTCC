package org.jonasribeiro.admin.catalogo.infraestructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jonasribeiro.admin.catalogo.ApiTest;
import org.jonasribeiro.admin.catalogo.ControllerTest;
import org.jonasribeiro.admin.catalogo.application.castmember.create.CreateCastMemberOutput;
import org.jonasribeiro.admin.catalogo.application.castmember.create.DefaultCreateCastMemberUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.retrieve.get.CastMemberOutput;
import org.jonasribeiro.admin.catalogo.application.castmember.retrieve.get.DefaultGetCastMemberUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.retrieve.list.CastMemberListOutput;
import org.jonasribeiro.admin.catalogo.application.castmember.retrieve.list.DefaultListCastMembersUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.update.DefaultUpdateCastMemberUseCase;
import org.jonasribeiro.admin.catalogo.domain.Fixture;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMember;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberID;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberType;
import org.jonasribeiro.admin.catalogo.domain.exceptions.NotFoundException;
import org.jonasribeiro.admin.catalogo.domain.exceptions.NotificationException;
import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.domain.validation.Error;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.models.CreateCastMemberRequest;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.models.UpdateCastMemberRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CastMemberAPI.class)
public class CastMemberAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private DefaultCreateCastMemberUseCase createCastMemberUseCase;

    @MockBean
    private DefaultGetCastMemberUseCase getCastMember;

    @MockBean
    private DefaultUpdateCastMemberUseCase UpdateCast;

    @MockBean
    private DefaultDeleteCastMemberUseCase deleteCastMemberUseCase;

    @MockBean
    private DefaultListCastMembersUseCase listCastMembersUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCastMember_shouldReturnItsIdentifier() throws Exception {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var expectedId = CastMemberID.from("o1i2u3i1o");

        final var aCommand =
                new CreateCastMemberRequest(expectedName, expectedType);

        when(createCastMemberUseCase.execute(any()))
                .thenReturn(CreateCastMemberOutput.from(expectedId));

        // when
        final var aRequest = post("/cast_members")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/cast_members/" + expectedId.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        verify(createCastMemberUseCase).execute(argThat(actualCmd ->
                Objects.equals(expectedName, actualCmd.name())
                        && Objects.equals(expectedType, actualCmd.type())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsCreateCastMember_thenShouldReturnNotification() throws Exception {
        // given
        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = new CreateCastMemberRequest(expectedName, expectedType);

        when(createCastMemberUseCase.execute(any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        // when
        final var request = post("/cast_members")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()", equalTo(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createCastMemberUseCase).execute(argThat(
                actualCmd -> Objects.equals(expectedName, actualCmd.name())
                        && Objects.equals(expectedType, actualCmd.type())));
    }

    @Test
    public void givenAValidatorId_whenCallsGetById_shouldReturnIt() throws Exception {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var aMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = aMember.getId().getValue();

        when(getCastMember.execute(any()))
                .thenReturn(CastMemberOutput.from(aMember));

        // when
        final var getRequest = get("/cast_members/{id}", expectedId)
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(getRequest);

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.type", equalTo(expectedType.name())))
                .andExpect(jsonPath("$.created_at", equalTo(aMember.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aMember.getUpdatedAt().toString())));

        verify(getCastMember).execute(eq(expectedId));
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateCastMember_thenShouldReturnNotification() throws Exception {
        // given
        final var aMember = CastMember.newMember("Vin Di", CastMemberType.DIRECTOR);
        final var expectedId = aMember.getId();

        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = new UpdateCastMemberRequest(expectedName, expectedType);

        when(UpdateCast.execute(any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        // when
        final var request = put("/cast_members/{id}", expectedId.getValue())
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()", equalTo(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(UpdateCast).execute(argThat(
                actualCmd -> Objects.equals(expectedName, actualCmd.name())
                        && Objects.equals(expectedType, actualCmd.type())
                        && Objects.equals(expectedId.getValue(), actualCmd.id())));
    }

    @Test
    public void givenAnInvalidId_whenCallsUpdateCastMember_thenShouldReturnNotFound() throws Exception {
        // given
        final var expectedErrorMessage = "CastMember with ID 123 was not found";
        final var expectedId = CastMemberID.from("123");

        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var aCommand = new UpdateCastMemberRequest(expectedName, expectedType);

        when(UpdateCast.execute(any()))
                .thenThrow(NotFoundException.with(CastMember.class, expectedId));

        // when
        final var request = put("/cast_members/{id}", expectedId.getValue())
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(UpdateCast).execute(argThat(
                actualCmd -> Objects.equals(expectedName, actualCmd.name())
                        && Objects.equals(expectedType, actualCmd.type())
                        && Objects.equals(expectedId.getValue(), actualCmd.id())));
    }

    @Test
    public void givenAValidId_whenCallsDeleteById_shouldDeleteIt() throws Exception {
        // given
        final var expectedId = "123";

       doNothing()
                .when(deleteCastMemberUseCase).execute(any());

        // when
        final var request = delete("/cast_members/{id}", expectedId)
            .with(ApiTest.CAST_MEMBERS_JWT);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNoContent());

        verify(deleteCastMemberUseCase).execute(argThat(
                actualId -> Objects.equals(expectedId, actualId)
        ));
    }

    @Test
    public void givenAValidParams_whenCallsListCastMembers_thenShouldReturn() throws Exception {
        // given
       final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Alg";
        final var expectedSort = "type";
        final var expectedDirection = "desc";
        final var expectedTotal = 1;
        final var expectedItemsCount = 1;

        final var expecteditems = List.of(
                CastMemberListOutput.from(aMember));

        when(listCastMembersUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expecteditems));

        // when
        final var request = get("/cast_members")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aMember.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(aMember.getName())))
                .andExpect(jsonPath("$.items[0].type", equalTo(aMember.getType().name())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aMember.getCreatedAt().toString())));

        verify(listCastMembersUseCase).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedTerms, query.terms())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedDirection, query.direction())
        ));
    }

    @Test
    public void givenEmptyParams_whenCallsListCastMembers_thenShouldUseDefaultsAndReturnIt() throws Exception {
        // given
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var expectedTotal = 1;
        final var expectedItemsCount = 1;

        final var expecteditems = List.of(
                CastMemberListOutput.from(aMember));

        when(listCastMembersUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expecteditems));

        // when
        final var request = get("/cast_members")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON);
        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aMember.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(aMember.getName())))
                .andExpect(jsonPath("$.items[0].type", equalTo(aMember.getType().name())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aMember.getCreatedAt().toString())));

        verify(listCastMembersUseCase).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedTerms, query.terms())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedDirection, query.direction())
        ));
    }
}
