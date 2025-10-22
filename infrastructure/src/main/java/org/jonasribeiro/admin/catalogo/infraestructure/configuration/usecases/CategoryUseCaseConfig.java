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
import org.jonasribeiro.admin.catalogo.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfig {

    private final CategoryGateway categoryGateway;

    public CategoryUseCaseConfig(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }

    @Bean
    public GetCategoryByUseCase getCategoryByUseCase() {
        return new DefaultGetCategoryByIdUseCase(categoryGateway);
    }

    @Bean
    public ListCategoriesUseCase listCategoriesUseCase() {
        return new DefaultListCategoriesUseCase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }
}
