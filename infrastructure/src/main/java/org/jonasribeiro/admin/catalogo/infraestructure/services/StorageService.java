package org.jonasribeiro.admin.catalogo.infraestructure.services;

import org.jonasribeiro.admin.catalogo.domain.video.Resource;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StorageService {

    void deleteAll(Collection<String> names);

    Optional<Resource> get(String name);

    List<String> list(String prefix);

    void store(String name , Resource resource);

    void clear();
}
