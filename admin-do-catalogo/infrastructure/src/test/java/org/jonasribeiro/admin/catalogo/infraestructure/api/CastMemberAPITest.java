package org.jonasribeiro.admin.catalogo.infraestructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jonasribeiro.admin.catalogo.ControllerTest;
import org.jonasribeiro.admin.catalogo.Fixture;
import org.jonasribeiro.admin.catalogo.application.castmember.CreateCastMemberOutput;
import org.jonasribeiro.admin.catalogo.application.castmember.CreateCastMemberUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.delete.DeleteCastMemberUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.retrieve.list.ListCastMembersUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.update.UpdateCasMemberUseCase;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberID;
import org.jonasribeiro.admin.catalogo.infraestructure.api.controllers.CastMemberController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CastMemberController.class)
public class CastMemberAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCastMemberUseCase createCastMemberUseCase;

    @MockBean
    private DeleteCastMemberUseCase deleteCastMemberUseCase;

    @MockBean
    private GetCastMemberByIdUseCase getCastMemberByIdUseCase;

    @MockBean
    private ListCastMembersUseCase listCastMembersUseCase;

    @MockBean
    private UpdateCasMemberUseCase updateCasMemberUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCastMember_shouldReturnItsIdentifier() throws Exception {
        // given
      final var expectedName = Fixture.name();
      final var expectedType = Fixture.CastMember.type();
      final var expectedId = CastMemberID.from("wadasdwqdqwd");

      final var aCommand = new CreateCastMemberRequest(expectedName, expectedType);

      when(createCastMemberUseCase.execute(any()))
              .thenReturn(CreateCastMemberOutput.from(expectedId));

        // when
       final var request = post("/cast_members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/cast_members/" + expectedId.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath(
                        "$.id", equalTo(expectedId.getValue())));

        verify(createCastMemberUseCase).execute(argThat(
                actualCmd -> Objects.equals(expectedName, actualCmd.name())
                        && Objects.equals(expectedType, actualCmd.type())));
    }

    @Test
    public void givenAnInvalidName_whenCallsCreateCastMember_thenShouldReturnNotification() throws Exception {
        // given
        final String expectedName = null;
        final var expectedType = Fixture.CastMember.type();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = new CreateCastMemberRequest(expectedName, expectedType);

        when(createCastMemberUseCase.execute(any()))
                .thenThrow(new IllegalArgumentException(expectedErrorMessage));

        // when
        final var request = post("/cast_members")
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
}
