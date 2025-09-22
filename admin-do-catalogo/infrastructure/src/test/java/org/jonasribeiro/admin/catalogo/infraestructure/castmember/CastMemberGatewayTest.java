package org.jonasribeiro.admin.catalogo.infraestructure.castmember;

import org.jonasribeiro.admin.catalogo.Fixture;
import org.jonasribeiro.admin.catalogo.MySQLGatewayTest;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMember;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberType;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.persistence.CastMemberJpaEntity;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
        final var actualMember = castMemberMySQLGateway.update(CastMember.with(aMember));

        //then
        Assertions.assertEquals(expectedId, actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
        Assertions.assertTrue(aMember.getUpdatedAt().isBefore(actualMember.getUpdatedAt()));

        final var actualEntity = castMemberRepository.findById(expectedId.getValue()).get();

        Assertions.assertNotNull(actualEntity);
        Assertions.assertEquals(expectedId.getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedType, actualEntity.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertTrue(actualMember.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
    }

}
