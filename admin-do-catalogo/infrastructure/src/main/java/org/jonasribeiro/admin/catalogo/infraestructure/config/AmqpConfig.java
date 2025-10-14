package org.jonasribeiro.admin.catalogo.infraestructure.config;

import org.jonasribeiro.admin.catalogo.infraestructure.configuration.annotations.VideoCreatedQueue;
import org.jonasribeiro.admin.catalogo.infraestructure.configuration.annotations.VideoEncodedQueue;
import org.jonasribeiro.admin.catalogo.infraestructure.configuration.properties.amqp.QueueProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    @Bean
    @ConfigurationProperties("amqp.queues.video-created")
    @VideoCreatedQueue
    public QueueProperties videoCreatedQueueProperties() {
        return new QueueProperties();
    }

    @Bean
    @ConfigurationProperties("amqp.queues.video-encoded")
    @VideoEncodedQueue
    public QueueProperties videoEncodedQueueProperties() {
        return new QueueProperties();
    }

    @Bean
    public String queueName(@VideoCreatedQueue QueueProperties props) {
        return props.getQueue();
    }
}
