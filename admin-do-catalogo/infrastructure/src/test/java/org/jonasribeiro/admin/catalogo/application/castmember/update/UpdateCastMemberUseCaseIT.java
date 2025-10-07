package org.jonasribeiro.admin.catalogo.application.castmember.update;

import org.jonasribeiro.admin.catalogo.Fixture;
import org.jonasribeiro.admin.catalogo.IntegrationTest;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMember;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberGateway;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberID;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberType;
import org.jonasribeiro.admin.catalogo.domain.exceptions.NotFoundException;
import org.jonasribeiro.admin.catalogo.domain.exceptions.NotificationException;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.persistence.CastMemberJpaEntity;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class UpdateCastMemberUseCaseIT {

    @Autowired
    private UpdateCasMemberUseCase useCase;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;


    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() {
        // given
        final var aMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var expectedId = aMember.getId();
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        assertNotNull(actualOutput);


        final var actualMember = this.castMemberRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, actualMember.getName());
        assertEquals(expectedType, actualMember.getType());
        assertEquals(aMember.getCreatedAt().truncatedTo(ChronoUnit.MILLIS), actualMember.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
      //  assertTrue(aMember.getUpdatedAt().isBefore(actualMember.getUpdatedAt()));

        verify(castMemberGateway).update(any());
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        // given
        final var aMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var expecteId = aMember.getId();
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCastMemberCommand.with(
                expecteId.getValue(),
                expectedName,
                expectedType
        );

        // when
        final var actualException = assertThrows(
                org.jonasribeiro.admin.catalogo.domain.exceptions.NotificationException.class,
                () -> useCase.execute(aCommand)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway).findById(any());
        verify(castMemberGateway, org.mockito.Mockito.never()).update(any());
    }

    @Test
    public void givenAInvalidType_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        // given
        final var aMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var expecteId = aMember.getId();
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;

        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCastMemberCommand.with(
                expecteId.getValue(),
                expectedName,
                expectedType
        );

        // when
        final var actualException = assertThrows(
                NotificationException.class,
                () -> useCase.execute(aCommand)
        );

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway).findById(any());
        verify(castMemberGateway, org.mockito.Mockito.never()).update(any());
    }

    @Test
    public void givenAInvalidId_whenCallsUpdateCastMember_shouldThrowsNotFoundException() {
        // given
        final var expecteId = CastMemberID.from("");
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var expectedErrorMessage = "CastMember with ID 123 was not found".formatted(expecteId.getValue());

        final var aCommand = UpdateCastMemberCommand.with(
                "123",
                expectedName,
                expectedType
        );

        // when
        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(castMemberGateway).findById(any());
    }
}
