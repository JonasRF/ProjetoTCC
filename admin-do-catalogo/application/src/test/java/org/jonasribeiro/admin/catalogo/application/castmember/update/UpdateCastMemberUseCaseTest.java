package org.jonasribeiro.admin.catalogo.application.castmember.update;

import org.jonasribeiro.admin.catalogo.application.Fixture;
import org.jonasribeiro.admin.catalogo.application.UseCaseTest;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMember;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberGateway;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberID;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberType;
import org.jonasribeiro.admin.catalogo.domain.exceptions.NotFoundException;
import org.jonasribeiro.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class UpdateCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnIt() {
        // given
        final var aMember = CastMember.newMember("vin diesel", CastMemberType.ACTOR);

        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;
        final var expectedId = aMember.getId();

        // when
        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );


        when(castMemberGateway.findById(expectedId))
                .thenReturn(Optional.of(CastMember.with(aMember)));

        when(castMemberGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);
        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        verify(castMemberGateway).findById(expectedId);

        verify(castMemberGateway).update(argThat(aMemberUpdated ->
                aMemberUpdated.getId().equals(expectedId)
                        && aMemberUpdated.getName().equals(expectedName)
                        && aMemberUpdated.getType() == expectedType
                        && aMemberUpdated.getCreatedAt().equals(aMember.getCreatedAt())
                        && aMemberUpdated.getUpdatedAt().isAfter(aMember.getUpdatedAt())
        ));
    }

    @Test
    public void givenAnInvalidId_whenCallsUpdateCastMember_shouldReturnNotificationException() {
        // given
        final var aMember = CastMember.newMember("vin diesel", CastMemberType.ACTOR);

        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;
        final var expectedId = aMember.getId();

        // when
        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );
        when(castMemberGateway.findById(expectedId))
                .thenReturn(Optional.empty());
        //then
        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(aCommand)
        );
    }

    @Test
    public void givenAValidName_whenCallsUpdateCastMember_shouldReturnNotificationException() {
        // given
        final var aMember = CastMember.newMember("vin diesel", CastMemberType.ACTOR);

        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedId = aMember.getId();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";


        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        when(castMemberGateway.findById(expectedId))
                .thenReturn(Optional.of(CastMember.with(aMember)));

        // when
        final var actualException = Assertions.assertThrows(
                NotificationException.class, () -> useCase.execute(aCommand)
        );

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway).findById(expectedId);

        verify(castMemberGateway,times(0)).update(any());
    }

    @Test
    public void givenAValidType_whenCallsUpdateCastMember_shouldReturnNotificationException() {
        // given
        final var aMember = CastMember.newMember("vin diesel", CastMemberType.DIRECTOR);

        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var expectedId = CastMemberID.from("123");

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        when(castMemberGateway.findById(expectedId))
                .thenReturn(Optional.empty());

        // when
        final var actualException = Assertions.assertThrows(
                NotFoundException.class, () -> useCase.execute(aCommand));

        // then
        Assertions.assertNotNull(actualException);

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(castMemberGateway).findById(expectedId);
        verify(castMemberGateway, times(0)).update(any());
    }
}
