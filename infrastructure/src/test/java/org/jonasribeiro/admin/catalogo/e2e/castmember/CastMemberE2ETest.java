package org.jonasribeiro.admin.catalogo.e2e.castmember;

import org.jonasribeiro.admin.catalogo.E2ETest;
import org.jonasribeiro.admin.catalogo.domain.Fixture;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberID;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberType;
import org.jonasribeiro.admin.catalogo.e2e.MockDsl;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class CastMemberE2ETest implements MockDsl {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:8.0")
            .withPassword("123456")
            .withUsername("root")
            .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
    }

    @Override
    public MockMvc mvc() {
        return this.mvc;
    }

    @Test
    public void asCatalogAdminIShouldBeAbleToCreateANewCastMemberWithVAlidValues() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var actualMemberId = givenCastMember(expectedName, expectedType);

        Assertions.assertEquals(1, castMemberRepository.count());

        final var actualMember = castMemberRepository.findById(actualMemberId.getValue()).get();

        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertNotNull(actualMember.getCreatedAt());
        Assertions.assertNotNull(actualMember.getUpdatedAt());
        Assertions.assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt()
        );
    }

    @Test
    public void asCatalogAdminIShouldBeAbleToSeeTreatedErrorByCreatingNewCastMemberWithInvalidValues() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorMessage = "'name' should not be null";

        givenCastMemberResult(expectedName, expectedType)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        Assertions.assertEquals(0, castMemberRepository.count());
    }

    @Test
    public void asCatalogAdminIShouldBeAbleToNavigateThruAllCastMembers() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        givenCastMember("Vin Diesel", CastMemberType.ACTOR);
        givenCastMember("Quentin Tarantino", CastMemberType.ACTOR);
        givenCastMember("Jason Mamoa", CastMemberType.ACTOR);

        Assertions.assertEquals(3, castMemberRepository.count());

        listCastMembers(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Jason Mamoa")));

        listCastMembers(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Quentin Tarantino")));

        listCastMembers(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Vin Diesel")));

        listCastMembers(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(3)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    public void asCatalogAdminIShouldBeAbleToSearchBetweenAllCastMembers() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        givenCastMember("Vin Diesel", CastMemberType.ACTOR);
        givenCastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
        givenCastMember("Jason Mamoa", CastMemberType.ACTOR);

        Assertions.assertEquals(3, castMemberRepository.count());

        listCastMembers(0, 1, "vin")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Vin Diesel")));

        listCastMembers(0, 1, "QUENTIN")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Quentin Tarantino")));

    }

    @Test
    public void asCatalogAdminIShouldBeAbleToSortAllCastMembersByNameDesc() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        givenCastMember("Vin Diesel", CastMemberType.ACTOR);
        givenCastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
        givenCastMember("Jason Mamoa", CastMemberType.ACTOR);

        Assertions.assertEquals(3, castMemberRepository.count());

        listCastMembers(0, 3, "", "name", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Vin Diesel")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Quentin Tarantino")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Jason Mamoa")));
    }

    @Test
    public void asCatalogAdminIShouldBeAbleToGetACastMemberByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        givenCastMember(Fixture.name(), Fixture.CastMembers.type());
        givenCastMember(Fixture.name(), Fixture.CastMembers.type());
        final var actualId = givenCastMember(expectedName, expectedType);

        Assertions.assertEquals(3, castMemberRepository.count());

       final var actualMember =  retrieveACastMember(actualId);

         Assertions.assertEquals(expectedName, actualMember.name());
         Assertions.assertEquals(expectedType.name(), actualMember.type());
         Assertions.assertNotNull(actualMember.createdAt());
         Assertions.assertNotNull(actualMember.updatedAt());
         Assertions.assertEquals(actualMember.createdAt(), actualMember.updatedAt());
    }

    @Test
    public void asCatalogAdminIShouldBeAbleToSeeTreatedErrorByGettingANotFoundCastMember() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

       givenCastMember(Fixture.name(), Fixture.CastMembers.type());
       givenCastMember(Fixture.name(), Fixture.CastMembers.type());

         retrieveACastMemberResult(CastMemberID.from("123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("CastMember with ID 123 was not found")));
    }

    @Test
    public void asCatalogAdminIShouldBeAbleToUpdateACastMemberItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        givenCastMember(Fixture.name(), Fixture.CastMembers.type());
        final var actualId = givenCastMember("vin d", CastMemberType.DIRECTOR);

        updateCastMember(actualId, expectedName, expectedType)
                .andExpect(status().isOk());

       final var actualMember = retrieveACastMember(actualId);

        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType.name(), actualMember.type());
        Assertions.assertNotNull(actualMember.createdAt());
        Assertions.assertNotNull(actualMember.updatedAt());
        Assertions.assertEquals(actualMember.updatedAt(), (actualMember.createdAt()));
    }

    @Test
    public void asCatalogAdminIShouldBeAbleToSeeTreatedErrorByUpdatingACastMemberWithInvalidValues () throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        final var expectedName = "";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorMessage = "'name' should not be null";

        givenCastMember(Fixture.name(), Fixture.CastMembers.type());
        final var actualId = givenCastMember("vin d", CastMemberType.DIRECTOR);

        updateCastMember(actualId, expectedName, expectedType)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void asCatalogAdminIShouldBeAbleToDeleteACastMemberByItsidentifier () throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        givenCastMember(Fixture.name(), Fixture.CastMembers.type());
        final var actualId = givenCastMember(Fixture.name(), Fixture.CastMembers.type());

        Assertions.assertEquals(2, castMemberRepository.count());

        deleteCastMember(actualId)
                .andExpect(status().isNoContent());

        Assertions.assertEquals(1, castMemberRepository.count());
        Assertions.assertFalse(castMemberRepository.existsById(actualId.getValue()));
    }

    @Test
    public void asCatalogAdminIShouldBeAbleToSeeTreatedErrorByDeletingAInvalidCastMember () throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        givenCastMember(Fixture.name(), Fixture.CastMembers.type());
        final var actualMember = givenCastMember(Fixture.name(), Fixture.CastMembers.type());

        deleteCastMember(actualMember)
                .andExpect(status().isNoContent());

        Assertions.assertEquals(1, castMemberRepository.count());
    }
}