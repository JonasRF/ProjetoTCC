package org.jonasribeiro.admin.catalogo.infraestructure;

import org.jonasribeiro.admin.catalogo.application.UseCase;
import org.jonasribeiro.admin.catalogo.infraestructure.configuration.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.jetty.JettyWebServer;
import org.springframework.boot.web.server.WebServer;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.printf("Hello, %s!%n", "World");
        SpringApplication.run(WebServerConfig.class, args);
    }
}