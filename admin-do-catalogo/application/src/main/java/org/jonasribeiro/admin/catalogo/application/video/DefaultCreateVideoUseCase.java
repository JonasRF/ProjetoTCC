package org.jonasribeiro.admin.catalogo.application.video;

import org.jonasribeiro.admin.catalogo.domain.Identifier;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberGateway;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberID;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryGateway;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;
import org.jonasribeiro.admin.catalogo.domain.exceptions.DomainException;
import org.jonasribeiro.admin.catalogo.domain.exceptions.NotificationException;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreGateway;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreID;
import org.jonasribeiro.admin.catalogo.domain.validation.Error;
import org.jonasribeiro.admin.catalogo.domain.validation.ValidationHandler;
import org.jonasribeiro.admin.catalogo.domain.validation.handler.Notification;
import org.jonasribeiro.admin.catalogo.domain.video.Rating;
import org.jonasribeiro.admin.catalogo.domain.video.Video;
import org.jonasribeiro.admin.catalogo.domain.video.VideoGateway;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultCreateVideoUseCase extends CreateVideoUseCase {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;
    private final CastMemberGateway castMemberGateway;
    private final VideoGateway videoGateway;


    public DefaultCreateVideoUseCase(
            final CategoryGateway categoryGateway,
            final GenreGateway genreGateway,
            final CastMemberGateway castMemberGateway,
            final VideoGateway videoGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }


    @Override
    public CreateVideoOutput execute(CreateVideoCommand aCommand) {
        final var aRating =  Rating.of(aCommand.rating()).orElseThrow(invalidRating(aCommand.rating()));
        final var categories = toIdentifier(aCommand.categories(), CategoryID::from);
        final var genres = toIdentifier(aCommand.genres(), GenreID::from);
        final var members = toIdentifier(aCommand.members(), CastMemberID::from);

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.append(validateGenres(genres));
        notification.append(validateMembers(members));

       final var aVideo = Video.newVideo(
                aCommand.title(),
                aCommand.description(),
                Year.of(aCommand.launchedAt()),
                aCommand.duration(),
                aCommand.opened(),
                aCommand.published(),
                aRating,
                categories,
                genres,
                members

        );
          aVideo.validate(notification);

          if (notification.hasErrors()) {
                throw new NotificationException("Could not create Aggregate Video", notification);
            }

        return CreateVideoOutput.from(create(aCommand, aVideo));
    }

    private Video create(final CreateVideoCommand aCommand,final Video aVideo) {
        return this.videoGateway.create(aVideo);
    }

    private ValidationHandler validateCategories(final Set<CategoryID> ids) {
        return validateAggregate("categories", ids, categoryGateway::existsByIds);
    }

    private ValidationHandler validateGenres(final Set<GenreID> genres) {
        return validateAggregate("genres", genres, genreGateway::existsByIds);
    }

    private ValidationHandler validateMembers(final Set<CastMemberID> members) {
        return validateAggregate("cast members", members, castMemberGateway::existsByIds);
    }

    private <T extends Identifier> ValidationHandler validateAggregate(
            final  String aggregate,
            final Set<T> ids,
            final Function<Set<T>, List<T>> existsByIds
    ) {
        final var notification = Notification.create();
        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = existsByIds.apply(ids);

        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(Identifier::getValue)
                    .collect(Collectors.joining(", "));
            notification.append(new Error("Some categories could not be found: %s".formatted(missingIdsMessage)));
        }
        return notification;
    }

    private <T> Set<T> toIdentifier(final Set<String> ids, final Function<String, T> mapper) {
        return ids.stream().map(mapper).collect(Collectors.toSet());
    }

    private Supplier<DomainException> invalidRating(final String rating) {
        return () -> DomainException.with(new Error("Rating not Found %s".formatted(rating)));
    }
}
