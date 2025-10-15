package org.jonasribeiro.admin.catalogo.infraestructure.configuration.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jonasribeiro.admin.catalogo.infraestructure.configuration.json.Json;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper(){
        return Json.mapper();
    }
}
