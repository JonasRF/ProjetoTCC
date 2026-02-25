package org.jonasribeiro.admin.catalogo.infraestructure.category.models;

import org.jonasribeiro.admin.catalogo.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class UpdateCategoryRequestTest {

    @Autowired
    private JacksonTester<UpdateCategoryRequest> json;

    @Test
    public void testUnmarshall() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var json = """
                {
                    "name": "%s",
                    "description": "%s",
                    "is_active": %s
                }
                """.formatted(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        try {
            final var actualJson = this.json.parse(json);

            assert actualJson.getObject().name().equals(expectedName);
            assert actualJson.getObject().description().equals(expectedDescription);
            assert actualJson.getObject().active().equals(expectedIsActive);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
