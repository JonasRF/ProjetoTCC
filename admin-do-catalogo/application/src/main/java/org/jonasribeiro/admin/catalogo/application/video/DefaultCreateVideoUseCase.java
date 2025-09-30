package org.jonasribeiro.admin.catalogo.application.video;

import org.jonasribeiro.admin.catalogo.domain.exceptions.DomainException;
import org.jonasribeiro.admin.catalogo.domain.validation.Error;
import org.jonasribeiro.admin.catalogo.domain.video.Rating;

import java.util.function.Supplier;

public class DefaultCreateVideoUseCase extends CreateVideoUseCase {

    @Override
    public CreateVideoOutput execute(CreateVideoCommand aCommand) {
        Rating.of(aCommand.rating()).orElseThrow(invalidRating(aCommand.rating()));
        return null;
    }

    private Supplier<DomainException> invalidRating(final String rating) {
        return () -> DomainException.with(new Error("Rating not Found %s".formatted(rating)));
    }
}
