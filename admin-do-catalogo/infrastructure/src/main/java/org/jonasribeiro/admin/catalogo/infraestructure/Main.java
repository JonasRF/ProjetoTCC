package org.jonasribeiro.admin.catalogo.infraestructure;

import org.jonasribeiro.admin.catalogo.application.UseCase;
import org.jonasribeiro.admin.catalogo.domain.category.Category;
import org.jonasribeiro.admin.catalogo.infraestructure.category.persistence.CategoryJpaEntity;
import org.jonasribeiro.admin.catalogo.infraestructure.category.persistence.CategoryRepository;
import org.jonasribeiro.admin.catalogo.infraestructure.configuration.WebServerConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.jetty.JettyWebServer;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.printf("Hello, %s!%n", "World");
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "production");
        SpringApplication.run(WebServerConfig.class, args);
    }

    @Bean
    public ApplicationRunner runner(CategoryRepository repository){
        return args -> {
            List<CategoryJpaEntity> all = repository.findAll();

            Category filmes = Category.newCategory("Filmes", null, true);
            repository.saveAndFlush(CategoryJpaEntity.from(filmes));
            repository.deleteAll();
        };
    }
}