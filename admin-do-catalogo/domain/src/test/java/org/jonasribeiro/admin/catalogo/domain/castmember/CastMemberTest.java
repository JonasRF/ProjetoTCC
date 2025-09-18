package org.jonasribeiro.admin.catalogo.domain.castmember;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CastMemberTest {

    @Test
    public void givenValidParams_whenCallsNewCastMember_thenInstantiateACastMember() {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var actualCastMember = CastMember.newMember(expectedName, expectedType);

        Assertions.assertNotNull(actualCastMember);
        Assertions.assertNotNull(actualCastMember.getId());
        Assertions.assertEquals(expectedName, actualCastMember.getName());
        Assertions.assertEquals(expectedType, actualCastMember.getType());
        Assertions.assertNotNull(actualCastMember.getCreatedAt());
        Assertions.assertNotNull(actualCastMember.getUpdatedAt());
        Assertions.assertNull(actualCastMember.getCreatedAt(), actualCastMember.getUpdatedAt());
    }
}
