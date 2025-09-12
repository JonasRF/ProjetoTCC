package org.jonasribeiro.admin.catalogo.infraestructure.configuration.usecases;

import org.jonasribeiro.admin.catalogo.application.category.create.CreateCategoryUseCase;
import org.jonasribeiro.admin.catalogo.application.category.create.DefaultCreateCategoryUseCase;
import org.jonasribeiro.admin.catalogo.application.category.delete.DefaultDeleteCategoryUseCase;
import org.jonasribeiro.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import org.jonasribeiro.admin.catalogo.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import org.jonasribeiro.admin.catalogo.application.category.retrieve.get.GetCategoryByUseCase;
import org.jonasribeiro.admin.catalogo.application.category.retrieve.list.DefaultListCategoriesUseCase;
import org.jonasribeiro.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import org.jonasribeiro.admin.catalogo.application.category.update.DefaultUpdateCategoryUseCase;
import org.jonasribeiro.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import org.jonasribeiro.admin.catalogo.application.genre.create.CreateGenreUseCase;
import org.jonasribeiro.admin.catalogo.application.genre.create.DefaultCreateGenreUseCase;
import org.jonasribeiro.admin.catalogo.application.genre.delete.DefaultDeleteGenreUseCase;
import org.jonasribeiro.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import org.jonasribeiro.admin.catalogo.application.genre.retrieve.get.DefaultGetGenreByIdUseCase;
import org.jonasribeiro.admin.catalogo.application.genre.retrieve.get.GetGenreIdUseCase;
import org.jonasribeiro.admin.catalogo.application.genre.retrieve.list.DefaultListGenreUseCase;
import org.jonasribeiro.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import org.jonasribeiro.admin.catalogo.application.genre.update.DefaultUpdateGenreUseCase;
import org.jonasribeiro.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryGateway;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class GenreUseCaseConfig {

    private final GenreGateway genreGateway;

    private final CategoryGateway categoryGateway;

    public GenreUseCaseConfig(GenreGateway genreGateway, CategoryGateway categoryGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Bean
    public CreateGenreUseCase createGenreUseCase() {
        return new DefaultCreateGenreUseCase(genreGateway, categoryGateway);
    }

    @Bean
    public UpdateGenreUseCase updateGenreUseCase() {
        return new DefaultUpdateGenreUseCase(genreGateway, categoryGateway);
    }

    @Bean
    public GetGenreIdUseCase getGenreIdUseCase() {
        return new DefaultGetGenreByIdUseCase(genreGateway);
    }

    @Bean
    public ListGenreUseCase listGenreUseCase() {
        return new DefaultListGenreUseCase(genreGateway);
    }

    @Bean
    public DeleteGenreUseCase deleteGenreUseCase() {
        return new DefaultDeleteGenreUseCase(genreGateway);
    }
}
