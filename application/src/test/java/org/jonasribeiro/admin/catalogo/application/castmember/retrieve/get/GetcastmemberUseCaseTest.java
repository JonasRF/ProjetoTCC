package org.jonasribeiro.admin.catalogo.application.castmember.retrieve.get;

import org.jonasribeiro.admin.catalogo.domain.Fixture;
import org.jonasribeiro.admin.catalogo.application.UseCaseTest;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMember;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberGateway;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberID;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberType;
import org.jonasribeiro.admin.catalogo.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetcastmemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetCastMember_shouldReturnIt() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.DIRECTOR;

        final var aMember = CastMember.newMember(expectedName, expectedType);

        final var expectedId = aMember.getId();

        // when
        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.of(aMember));

        final var actualMember = useCase.execute(expectedId.getValue());

        // then
        Assertions.assertNotNull(actualMember);
        Assertions.assertEquals(expectedId.getValue(), actualMember.id());
        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType, actualMember.type());
        Assertions.assertEquals(aMember.getCreatedAt(), actualMember.createdAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), actualMember.updatedAt());

        verify(castMemberGateway).findById(expectedId);
    }

    @Test
    public void givenAnInvalidId_whenCallsGetCastMember_shouldReturnNotFoundException() {
        // given
        final var expectedId = CastMemberID.from("");
        final var expectedErrorMessage = "CastMember with ID %s was not found".formatted(expectedId.getValue());

        // when
        when(castMemberGateway.findById(expectedId))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(castMemberGateway).findById(eq(expectedId));
    }
}
