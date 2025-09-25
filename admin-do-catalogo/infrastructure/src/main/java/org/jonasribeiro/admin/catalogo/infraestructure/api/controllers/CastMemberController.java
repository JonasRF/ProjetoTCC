package org.jonasribeiro.admin.catalogo.infraestructure.api.controllers;

import org.jonasribeiro.admin.catalogo.application.castmember.create.CreateCastMemberCommand;
import org.jonasribeiro.admin.catalogo.application.castmember.create.CreateCastMemberUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import org.jonasribeiro.admin.catalogo.infraestructure.api.CastMemberAPI;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.models.CastMemberResponse;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.models.CreateCastMemberRequest;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.presenter.CastMemberPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {

    private final CreateCastMemberUseCase useCase;
    private final GetCastMemberByIdUseCase getUseCase;

    public CastMemberController(final CreateCastMemberUseCase useCase, GetCastMemberByIdUseCase getUseCase) {
        this.useCase = Objects.requireNonNull(useCase);
        this.getUseCase = Objects.requireNonNull(getUseCase);
    }

    @Override
    public ResponseEntity<?> create(@org.springframework.web.bind.annotation.RequestBody final CreateCastMemberRequest input) {
        final var aCommand = CreateCastMemberCommand.with(input.name(), input.type());

        final var output = this.useCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/cast_members/" + output.id())).body(output);
    }

    @Override
    public CastMemberResponse getById(final String id) {
        return CastMemberPresenter.present(this.getUseCase.execute(id));
    }
}
