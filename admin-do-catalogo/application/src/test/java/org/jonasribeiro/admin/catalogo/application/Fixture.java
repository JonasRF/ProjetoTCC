package org.jonasribeiro.admin.catalogo.application;

import com.github.javafaker.Faker;
import io.vavr.API;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMember;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberType;
import org.jonasribeiro.admin.catalogo.domain.category.Category;
import org.jonasribeiro.admin.catalogo.domain.genre.Genre;
import org.jonasribeiro.admin.catalogo.domain.video.Rating;

import java.lang.reflect.Type;
import java.util.ResourceBundle;

import static io.vavr.API.*;

public class Fixture {

    public static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static Integer year() {
        return FAKER.random().nextInt(2020,2030);
    }

    public static Rating duration() {
        return FAKER.options().option(
                Rating.values());
    }

    public static boolean bool(){
        return FAKER.bool().bool();
    }

    public static String title() {
        return FAKER.options().option(
                "The Shawshank Redemption",
                "The Godfather",
                "The Dark Knight"
        );
    }

    public static final class Categories {
        private  static final Category AULAS = Category.newCategory("Aulas", "Some description", true);

        public static Category aulas() {
            return AULAS.clone();
        }
    }

    public static final class CastMembers {

        private static final CastMember JONAS = CastMember.newMember("Jonas Ribeiro", CastMemberType.DIRECTOR);

        private static final CastMember MARIA = CastMember.newMember("Maria Silva", CastMemberType.ACTOR);

        public static CastMemberType type() {
            return FAKER.options().option(CastMemberType.values());
        }

        public static CastMember jonas() {
            return CastMember.with(JONAS);
        }

        public static CastMember maria() {
            return CastMember.with(MARIA);
        }
    }

    public static final class Genres {
        private static final Genre TECH = Genre.newGenre("Tech",  true);

        public static Genre tech() {
            return TECH.with(TECH);
        }
    }

    public static final class Videos {

        public static Rating rating() {
            return FAKER.options().option(
                    Rating.values()
            );
        }
        public static Rating resource(final Resource.Type type) {
            final String contentType = Match(type).of(
                    Case($(List(Type.VIDEO, Type.TRAILER)), "video/mp4"),
                    Case($(), "image/jpg")
            );
            final byte[] content = "Conteudo".getBytes();

            return Resource.of(content, contentType, type.name().toLowerCase(), type);
        }

        public static String description() {
            return FAKER.options().option(
                    """
                            Disclaimer: The following description contains spoilers for the movie "Inception." 
                            If you haven't seen the film yet, proceed with caution. 
                            Disclaimer: The following description contains spoilers for the movie "Inception." 
                            If you haven't seen the film yet, proceed with caution.
                             Disclaimer: The following description contains spoilers for the movie "Inception." 
                            If you haven't seen the film yet, proceed with caution.
                            """,
            """
                    Neste vídeo você entenderá o conceito de Inception,
                    um thriller de ficção científica dirigido por Christopher Nolan.
                    A trama gira em torno de Dom Cobb, um ladrão especializado em extrair segredos
                    do subconsciente durante o estado de sonho.
                    Cobb é oferecido uma chance de redenção: plantar uma ideia na mente de alguém,
                    um processo conhecido como "inception".
                    """
            );
        }
    }
}
