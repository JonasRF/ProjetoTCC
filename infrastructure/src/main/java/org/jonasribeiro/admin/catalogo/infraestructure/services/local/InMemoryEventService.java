package org.jonasribeiro.admin.catalogo.infraestructure.services.local;

import org.jonasribeiro.admin.catalogo.infraestructure.configuration.json.Json;
import org.jonasribeiro.admin.catalogo.infraestructure.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryEventService implements EventService {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryEventService.class);

    @Override
    public void send(Object event) {
        LOG.info("Event was observed: {}", Json.writeValueAsString(event));
    }
}