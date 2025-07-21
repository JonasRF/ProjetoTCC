package org.jonasribeiro.admin.catalogo.domain;

import org.jonasribeiro.admin.catalogo.domain.category.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {

    @Test
    public void testeNewCategory() {
        Assertions.assertNotNull(new Category());
    }
}
