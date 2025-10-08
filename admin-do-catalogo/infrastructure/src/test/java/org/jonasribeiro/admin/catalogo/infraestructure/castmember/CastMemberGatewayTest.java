package org.jonasribeiro.admin.catalogo.infraestructure.castmember;

import org.jonasribeiro.admin.catalogo.MySQLGatewayTest;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMember;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberID;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberType;
import org.jonasribeiro.admin.catalogo.domain.pagination.SearchQuery;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.persistence.CastMemberJpaEntity;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.jonasribeiro.admin.catalogo.Fixture.CastMember.type;
import static org.jonasribeiro.admin.catalogo.Fixture.name;

@MySQLGatewayTest
public class CastMemberGatewayTest {

    @Autowired
    private CastMemberMySQLGateway castMemberMySQLGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void testDependencies() {
        Assertions.assertNotNull(castMemberMySQLGateway);
        Assertions.assertNotNull(castMemberRepository);
    }

    @Test
    public void givenAValidId_whenCallsCreate_shouldPersistIt() {
        // given
        final var expectedName = name();
        final var expectedType = type();

        final var aMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = aMember.getId();

        Assertions.assertEquals(0, castMemberRepository.count());

        // when
        final var actualMember = castMemberMySQLGateway.create(aMember);

        //then
        Assertions.assertEquals(1, castMemberRepository.count());
        Assertions.assertNotNull(actualMember);
        Assertions.assertEquals(expectedId, actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), actualMember.getUpdatedAt());

        final var actualEntity = castMemberRepository.findById(expectedId.getValue()).get();

        Assertions.assertNotNull(actualEntity);
        Assertions.assertEquals(expectedId.getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedType, actualEntity.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), actualEntity.getUpdatedAt());
    }

    @Test
    public void givenAValidId_whenCallsUpdate_shouldPersistIt() {
        // given
        final var expectedName = name();
        final var expectedType = CastMemberType.ACTOR;

        final var aMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);
        final var expectedId = aMember.getId();


        final var currentMember = castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());
        Assertions.assertEquals("Vin Diesel", currentMember.getName());
        Assertions.assertEquals(CastMemberType.DIRECTOR, currentMember.getType());

        // when
        final var actualMember = castMemberMySQLGateway.update(CastMember.with(aMember)
                .update(expectedName, expectedType
        ));

        //then
        Assertions.assertEquals(expectedId, actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
       // Assertions.assertTrue(aMember.getUpdatedAt().isBefore(actualMember.getUpdatedAt()));

        final var actualEntity = castMemberRepository.findById(expectedId.getValue()).get();

        Assertions.assertNotNull(actualEntity);
        Assertions.assertEquals(expectedId.getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedType, actualEntity.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), actualEntity.getCreatedAt());
     //   Assertions.assertTrue(aMember.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
    }

    @Test
    public void givenAValidId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        final var aMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);
        final var expectedId = aMember.getId();

        Assertions.assertEquals(0, castMemberRepository.count());

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));
        Assertions.assertEquals(1, castMemberRepository.count());

        // when
        castMemberMySQLGateway.deleteById(expectedId);

        // then
        Assertions.assertEquals(0, castMemberRepository.count());
    }
    @Test
    public void givenAValidId_whenCallsFindById_shouldReturnIt() {
        // given
        final var expectedName = name();
        final var expectedType = type();

        final var aMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = aMember.getId();

        Assertions.assertEquals(0, castMemberRepository.count());

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));
        Assertions.assertEquals(1, castMemberRepository.count());

        // when
        final var actualMember = castMemberMySQLGateway.findById(expectedId).get();

        //then
        Assertions.assertNotNull(actualMember);
        Assertions.assertEquals(expectedId, actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), actualMember.getUpdatedAt());
    }

    @Test
    public void givenAnInvalidId_whenCallsFindById_shouldReturnEmpty() {
        // given
        final var aMember = CastMember.newMember(name(), type());

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());

        // when
        final var actualMember = castMemberMySQLGateway.findById(CastMemberID.from("123"));

        //then
        Assertions.assertTrue(actualMember.isEmpty());
        Assertions.assertEquals(0, actualMember.stream().count());
    }

    @Test
    public void givenEmptyCastMembers_whenCallsFindAll_shouldReturnEmpty() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        // when
        final var actualPAge = castMemberMySQLGateway.findAll(aQuery);

        //then
        Assertions.assertNotNull(actualPAge);
        Assertions.assertEquals(expectedPage, actualPAge.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPAge.perPage());
        Assertions.assertEquals(expectedTotal, actualPAge.total());
        Assertions.assertEquals(0, actualPAge.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "vin,0,10,1,1,Vin Diesel",
            "taran,0,10,1,1,Quentin Tarantino",
            "jas,0,10,1,1,Jason Momoa",
            "har,0,10,1,1,Kit Harington",
            "MAR,0,10,1,1,Martin Scorsese",

    })
    public void givenAValidTerms_whenCallsFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotal,
            final String expectedMemberName
    ) {
        // given
        mockMembers();

        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        // when
        final var actualPage = castMemberMySQLGateway.findAll(aQuery);

        //then
        Assertions.assertNotNull(actualPage);
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedMemberName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Jason Momoa",
            "name,desc,0,10,5,5,Vin Diesel",
            "createdAt,asc,0,10,5,5,Kit Harington"
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnSorted(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotal,
            final String expectedMemberName
    ){
        // given
        mockMembers();

        final var expectedTerms = "";

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        // when
        final var actualPage = castMemberMySQLGateway.findAll(aQuery);

        //then
        Assertions.assertNotNull(actualPage);
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedTotal, actualPage.total());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Jason Momoa;Kit Harington",
            "1,2,2,5,Martin Scorsese;Quentin Tarantino",
            "2,2,1,5,Vin Diesel",
    })
    public void givenAValidPagination_whenCallsFindAll_shouldReturnPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotal,
            final String expectedMemberName
    ){
        // given
        mockMembers();

        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        // when
        final var actualPage = castMemberMySQLGateway.findAll(aQuery);

        //then
        Assertions.assertNotNull(actualPage);
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedTotal, actualPage.total());

        final String[] expectedNames = expectedMemberName.split(";");
        for (int i = 0; i < expectedNames.length; i++) {
            Assertions.assertEquals(expectedNames[i], actualPage.items().get(i).getName());
        }
    }

    private void  mockMembers() {
        castMemberRepository.deleteAll(); // Limpa o repositório antes de inserir os membros
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(CastMember.newMember("Kit Harington", CastMemberType.ACTOR)));
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(CastMember.newMember("Vin Diesel", CastMemberType.ACTOR)));
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(CastMember.newMember("Quentin Tarantino", CastMemberType.DIRECTOR)));
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(CastMember.newMember("Jason Momoa", CastMemberType.ACTOR)));
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(CastMember.newMember("Martin Scorsese", CastMemberType.DIRECTOR)));
    }
}
