package org.jonasribeiro.admin.catalogo.application;

import com.github.javafaker.Faker;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMember;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberType;
import org.jonasribeiro.admin.catalogo.domain.category.Category;
import org.jonasribeiro.admin.catalogo.domain.genre.Genre;
import org.jonasribeiro.admin.catalogo.domain.utils.IdUtils;
import org.jonasribeiro.admin.catalogo.domain.video.*;

import java.time.Year;
import java.util.Set;
import java.util.UUID;

import static io.vavr.API.*;

public class Fixture {

    public static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static Integer year() {
        return FAKER.random().nextInt(2020,2030);
    }

    public static double duration() {
        return FAKER.options().option(120.0, 15.5, 35.5, 10.0, 2.0);
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

    public static String checksum() {
        return "03fe62de";
    }

    public static Video video() {
        return Video.newVideo(
                Fixture.title(),
                Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Videos.rating(),
                Set.of(Categories.aulas().getId()),
                Set.of(Genres.tech().getId()),
                Set.of(CastMembers.jonas().getId(), CastMembers.maria().getId())
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
            return Genre.with(TECH);
        }
    }

    public static final class Videos {

        private static final Video SYSTEM_DESIGN = Video.newVideo(
                "System Design no Mercado Livre na prática",
                description(),
                Year.of(2022),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                rating(),
                Set.of(Categories.aulas().getId()),
                Set.of(Genres.tech().getId()),
                Set.of(CastMembers.jonas().getId(), CastMembers.maria().getId())
            );

        public static Video systemDesign() {
            return Video.with(SYSTEM_DESIGN);
        }

        public static Rating rating() {
            return FAKER.options().option(Rating.values());
        }

        public static Resource resource(final VideoMediaType type) {
            final String contentType = Match(type).of(
                    Case($(List(VideoMediaType.VIDEO, VideoMediaType.TRAILER)::contains), "video/mp4"),
                    Case($(), "image/jpg")
            );

            final String checksum = IdUtils.uuid();
            final byte[] content = "Conteudo".getBytes();

            return Resource.with(content, checksum, contentType, type.name().toLowerCase());
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

        public static AudioVideoMedia audioVideo(final VideoMediaType type) {
            final var checksum = UUID.randomUUID().toString();
            return AudioVideoMedia.with(
                    checksum,
                    type.name().toLowerCase(),
                    "video/mp4" + checksum
            );
        }

        public static ImageMedia image(final VideoMediaType type) {
            final var checksum = UUID.randomUUID().toString();
            return ImageMedia.with(
                    checksum,
                    type.name().toLowerCase(),
                    "/images/" + checksum
            );
        }

    }
}
