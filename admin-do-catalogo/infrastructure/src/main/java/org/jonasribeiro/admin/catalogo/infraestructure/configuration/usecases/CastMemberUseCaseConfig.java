package org.jonasribeiro.admin.catalogo.infraestructure.configuration.usecases;

import org.jonasribeiro.admin.catalogo.application.castmember.CreateCastMemberUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.DefaultCreateCastMemberUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.delete.DeleteCastMemberUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.retrieve.get.DefaultGetCastMemberUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.retrieve.list.DefaultListCastMembersUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.retrieve.list.ListCastMembersUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.update.DefaultUpdateCastMemberUseCase;
import org.jonasribeiro.admin.catalogo.application.castmember.update.UpdateCasMemberUseCase;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class CastMemberUseCaseConfig {

    private final CastMemberGateway castMemberGateway;

    public CastMemberUseCaseConfig(CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Bean
    public CreateCastMemberUseCase createCastMemberUseCase() {
        return new DefaultCreateCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public DeleteCastMemberUseCase deleteCastMemberUseCase() {
        return new DefaultDeleteCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public GetCastMemberByIdUseCase getCastMemberByIdUseCase() {
        return new DefaultGetCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public ListCastMembersUseCase listCastMembersUseCase() {
        return new DefaultListCastMembersUseCase(castMemberGateway);
    }

    @Bean
    public UpdateCasMemberUseCase updateCastMemberUseCase() {
        return new DefaultUpdateCastMemberUseCase(castMemberGateway);
    }
}
