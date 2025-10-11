package org.jonasribeiro.admin.catalogo.infraestructure.services.local;

import org.jonasribeiro.admin.catalogo.domain.video.Resource;
import org.jonasribeiro.admin.catalogo.infraestructure.services.StorageService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStorageService  implements StorageService {

    private final Map<String, Resource> storage;

    public InMemoryStorageService() {
        this.storage = new ConcurrentHashMap<>();
    }

    public Map<String, Resource> storage() {
        return this.storage;
    }

    public void reset(){
        this.storage.clear();
    }

    @Override
    public void deleteAll(Collection<String> names) {
        names.forEach(this.storage::remove);
    }

    @Override
    public Optional<Resource> get(String name) {
        return Optional.ofNullable(this.storage.get(name));
    }

    @Override
    public List<String> list(String prefix) {
        if (prefix == null) {
            return Collections.emptyList();
        }
        return this.storage.keySet().stream()
                .filter(key -> key.startsWith(prefix))
                .toList();
    }

    @Override
    public void store(String name, Resource resource) {
        this.storage.put(name, resource);
    }

    @Override
    public void clear() {
        this.storage.clear();
    }
}
