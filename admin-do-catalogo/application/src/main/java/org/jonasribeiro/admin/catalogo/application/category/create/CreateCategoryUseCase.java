package org.jonasribeiro.admin.catalogo.application.create;

import io.vavr.control.Either;
import org.jonasribeiro.admin.catalogo.application.UseCase;
import org.jonasribeiro.admin.catalogo.domain.validation.handler.Notification;

public abstract class CreateCategoryUseCase
    extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {
    }

