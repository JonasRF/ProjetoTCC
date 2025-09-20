package org.jonasribeiro.admin.catalogo.application;

import com.github.javafaker.Faker;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberType;

public class Fixture {

    public static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static final class CastMember {

        public static CastMemberType type() {
            return FAKER.options().option(CastMemberType.ACTOR, CastMemberType.DIRECTOR);
        }
    }
}
