package org.jonasribeiro.admin.catalogo.application.castmember.retrieve.get;

import org.jonasribeiro.admin.catalogo.IntegrationTest;
import org.jonasribeiro.admin.catalogo.Fixture;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMember;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberGateway;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberID;
import org.jonasribeiro.admin.catalogo.domain.exceptions.NotFoundException;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.persistence.CastMemberJpaEntity;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class GetCastMemberUseCaseIT {

    @Autowired
    private GetCastMemberByIdUseCase useCase;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void givenAValidId_whenCallsGetCastMember_shouldReturnIt() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var aMember = CastMember.newMember(expectedName, expectedType);

        final var expectedId = aMember.getId();

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        Assertions.assertEquals(1, this.castMemberRepository.count());

        // when
        final var actualOutput = useCase.execute(expectedId.getValue());

        // then
        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());
        assertEquals(expectedName, actualOutput.name());
        assertEquals(expectedType, actualOutput.type());
        assertEquals(aMember.getCreatedAt().truncatedTo(java.time.temporal.ChronoUnit.MILLIS), actualOutput.createdAt().truncatedTo(java.time.temporal.ChronoUnit.MILLIS));
        assertEquals(aMember.getUpdatedAt().truncatedTo(java.time.temporal.ChronoUnit.MILLIS), actualOutput.updatedAt().truncatedTo(java.time.temporal.ChronoUnit.MILLIS));

        verify(castMemberGateway).findById(expectedId);
    }

    @Test
    public void givenAInvalidId_whenCallsGetCastMember_shouldReturnNotFoundException() {
        // given
        final var expectedId = CastMemberID.from("123");

        final  var expectedErrorMessage = "CastMember with ID 123 was not found";

        // when
        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(castMemberGateway).findById(expectedId);
    }
}
