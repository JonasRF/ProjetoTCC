package org.jonasribeiro.admin.catalogo.application.genre.update;

import org.jonasribeiro.admin.catalogo.domain.Identifier;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryGateway;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;
import org.jonasribeiro.admin.catalogo.domain.exceptions.DomainException;
import org.jonasribeiro.admin.catalogo.domain.exceptions.NotFoundException;
import org.jonasribeiro.admin.catalogo.domain.exceptions.NotificationException;
import org.jonasribeiro.admin.catalogo.domain.genre.Genre;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreGateway;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreID;
import org.jonasribeiro.admin.catalogo.domain.validation.Error;
import org.jonasribeiro.admin.catalogo.domain.validation.ValidationHandler;
import org.jonasribeiro.admin.catalogo.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultUpdateGenreUseCase extends UpdateGenreUseCase {

    private final GenreGateway genreGateway;

    private final CategoryGateway categoryGateway;

    public DefaultUpdateGenreUseCase(GenreGateway genreGateway, CategoryGateway categoryGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public UpdateGenreOutput execute(final UpdateGenreCommand aCommand) {
        final var anId = GenreID.from(aCommand.id());
        final var aName = aCommand.name();
        final var isActive = aCommand.isActive();
        final var categories = toCategoryID(aCommand.categories());

        final var aGenre = this.genreGateway.findById(anId)
                .orElseThrow(notFound(anId));

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.validate(() -> aGenre.update(aName, isActive, categories));

        if (notification.hasErrors()) {
            throw new NotificationException(
                    "Could not update Aggregate Genre %s".formatted(aCommand.id()), notification
            );
        }

        return UpdateGenreOutput.from(this.genreGateway.update(aGenre));
    }


    private ValidationHandler validateCategories(List<CategoryID> ids) {
        final var notification = Notification.create();
        if (ids == null || ids.isEmpty()) {
            return notification;
        }
        final var existingIds = categoryGateway.existsByIds(ids);

        if (ids.size() != existingIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(existingIds);
            final var missingIdsMessage = missingIds.stream()
                    .map(Identifier::getValue)
                    .collect(Collectors.joining(", "));
            notification.append(new Error("Some categories could not be found: %s".formatted(missingIdsMessage)));
        }
        return notification;
    }

    private Supplier<DomainException> notFound(final Identifier anId) {
        return () -> NotFoundException.with(Genre.class, anId);
    }

    private List<CategoryID> toCategoryID(final List<String> ids) {
        return ids.stream()
                .map(CategoryID::from)
                .toList();
    }
}


