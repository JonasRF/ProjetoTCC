package org.jonasribeiro.admin.catalogo.application.genre.create;

import org.jonasribeiro.admin.catalogo.domain.category.CategoryGateway;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;
import org.jonasribeiro.admin.catalogo.domain.exceptions.NotificationException;
import org.jonasribeiro.admin.catalogo.domain.genre.Genre;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreGateway;
import org.jonasribeiro.admin.catalogo.domain.validation.Error;
import org.jonasribeiro.admin.catalogo.domain.validation.ValidationHandler;
import org.jonasribeiro.admin.catalogo.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultCreateGenreUseCase extends CreateGenreUseCase {

    private final GenreGateway genreGateway;

    private final CategoryGateway categoryGateway;

    public DefaultCreateGenreUseCase(
            final GenreGateway genreGateway,
            final CategoryGateway categoryGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CreateGenreOutput execute(CreateGenreCommand anIn) {

        final var aName = anIn.name();
        final var isActive = anIn.isActive();
        final var categories = toCategoryID(anIn.categories());

        final var notification = Notification.create();

        notification.append(validateCategories(categories));

        final var aGenre =  notification.validate(() -> Genre.newGenre(aName, isActive));

        if (notification.hasErrors()) {
            throw new NotificationException("Could not create Aggregate genre ", notification);
        }

        aGenre.addCategories(categories);

       return CreateGenreOutput.from(
               this.genreGateway.create(aGenre));
    }

    private ValidationHandler validateCategories(final List<CategoryID> ids) {
        final var notification = Notification.create();
        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final var existingCategories = this.categoryGateway.existsByIds(ids);
        if (ids.size() != existingCategories.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(existingCategories);

              final var missingIdsMessage = missingIds.stream()
                     .map(CategoryID::getValue)
                      .collect(Collectors.joining(", "));
                notification.append(new Error("Some categories could not be found: %s".formatted(missingIdsMessage)));
        }
        return notification;
    }

    private List<CategoryID> toCategoryID(final List<String> categories) {
        if (categories == null) {
            return List.of();
        }
        return categories.stream()
                .map(CategoryID::from)
                .toList();
    }
}
