package org.jonasribeiro.admin.catalogo.infraestructure.api.controllers;

import org.jonasribeiro.admin.catalogo.application.castmember.create.CreateCastMemberCommand;
import org.jonasribeiro.admin.catalogo.application.castmember.create.CreateCastMemberUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.delete.DeleteCastMemberUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.retrieve.list.ListCastMembersUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.update.UpdateCasMemberUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.update.UpdateCastMemberCommand;
import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.domain.pagination.SearchQuery;
import org.jonasribeiro.admin.catalogo.infraestructure.api.CastMemberAPI;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.models.CastMemberListResponse;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.models.CastMemberResponse;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.models.CreateCastMemberRequest;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.models.UpdateCastMemberRequest;
import org.jonasribeiro.admin.catalogo.infraestructure.castmember.presenter.CastMemberPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {

    private final CreateCastMemberUseCase useCase;
    private final GetCastMemberByIdUseCase getUseCase;
    private final UpdateCasMemberUseCase updateUseCase;
    private final DeleteCastMemberUseCase deleteUseCase;
    private final ListCastMembersUseCase listUseCase;

    public CastMemberController(final CreateCastMemberUseCase useCase,
                                final GetCastMemberByIdUseCase getUseCase,
                                final UpdateCasMemberUseCase updateUseCase,
                                final DeleteCastMemberUseCase deleteUseCase,
                                final ListCastMembersUseCase listUseCase
    ) {
        this.useCase = Objects.requireNonNull(useCase);
        this.getUseCase = Objects.requireNonNull(getUseCase);
        this.updateUseCase = Objects.requireNonNull(updateUseCase);
        this.deleteUseCase = Objects.requireNonNull(deleteUseCase);
        this.listUseCase = Objects.requireNonNull(listUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateCastMemberRequest input) {
        final var aCommand = CreateCastMemberCommand.with(input.name(), input.type());

        final var output = this.useCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/cast_members/" + output.id())).body(output);
    }

    @Override
    public CastMemberResponse getById(final String id) {
        return CastMemberPresenter.present(this.getUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> Update(final String id, final UpdateCastMemberRequest input) {
        final var aCommand = UpdateCastMemberCommand.with(id, input.name(), input.type());

        final var output = this.updateUseCase.execute(aCommand);

        return ResponseEntity.ok(output);
    }

    @Override
    public Pagination<CastMemberListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        return this.listUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(CastMemberPresenter::present);
    }

    @Override
    public void deleteById(String id) {
        this.deleteUseCase.execute(id);
    }
}
