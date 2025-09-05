package org.jonasribeiro.admin.catalogo;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;

public class MySQLCleanUpExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(final ExtensionContext context) {
        final var appContext =  SpringExtension.getApplicationContext(context);
                cleanUp(List.of(
                        appContext.getBean("categoryRepository", CrudRepository.class),
                        appContext.getBean("genreRepository", CrudRepository.class)
                ));
                final var em = appContext.getBean("testEntityManager", TestEntityManager.class);
                em.flush();
                em.clear();
    }

    private void cleanUp(final Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}

