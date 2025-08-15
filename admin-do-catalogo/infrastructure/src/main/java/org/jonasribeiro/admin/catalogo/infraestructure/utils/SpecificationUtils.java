package org.jonasribeiro.admin.catalogo.infraestructure.utils;

import org.springframework.data.jpa.domain.Specification;

public class SpecificationUtils {

    public static <T> Specification<T> like(final String prop, final String term) {

        return (root, queyry, cb) -> cb.like(cb.upper(root.get(prop)), likeTerm(term.toUpperCase()));
    }

    private static String likeTerm(final String term) {
        return "%" + term + "%";
    }
}
