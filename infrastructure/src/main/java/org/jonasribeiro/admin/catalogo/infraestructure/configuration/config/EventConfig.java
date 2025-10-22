package org.jonasribeiro.admin.catalogo.infraestructure.configuration.config;

import org.jonasribeiro.admin.catalogo.infraestructure.configuration.annotations.VideoCreatedQueue;
import org.jonasribeiro.admin.catalogo.infraestructure.configuration.properties.amqp.QueueProperties;
import org.jonasribeiro.admin.catalogo.infraestructure.services.EventService;
import org.jonasribeiro.admin.catalogo.infraestructure.services.impl.RabbitEventService;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {

    @Bean
    @VideoCreatedQueue
    EventService videoCreatedEventService(
            @VideoCreatedQueue final QueueProperties props,
            final RabbitOperations ops
            ){
        return new RabbitEventService(props.getExchange(), props.getRoutingKey(), ops);
    }
}
