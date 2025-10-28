package org.jonasribeiro.admin.catalogo.infraestructure.configuration.config;

import org.jonasribeiro.admin.catalogo.infraestructure.configuration.annotations.VideoCreatedQueue;
import org.jonasribeiro.admin.catalogo.infraestructure.configuration.properties.amqp.QueueProperties;
import org.jonasribeiro.admin.catalogo.infraestructure.services.EventService;
import org.jonasribeiro.admin.catalogo.infraestructure.services.impl.RabbitEventService;
import org.jonasribeiro.admin.catalogo.infraestructure.services.local.InMemoryEventService;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class EventConfig {

    @Bean
    @VideoCreatedQueue
    @ConditionalOnMissingBean
    EventService videoCreatedEventService(
            @VideoCreatedQueue final QueueProperties props,
            final RabbitOperations ops
            ){
        return new RabbitEventService(props.getExchange(), props.getRoutingKey(), ops);
    }
}
