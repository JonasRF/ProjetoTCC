package org.jonasribeiro.admin.catalogo.infraestructure.category.models;

import org.jonasribeiro.admin.catalogo.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class CreateCategoryRequestTest {

    @Autowired
    private JacksonTester<CreateCategoryRequest> json;

    @Test
    public void testMarshall() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var request = new CreateCategoryRequest(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        try {
            final var actualJson = this.json.write(request);

            System.out.println(actualJson);

            actualJson.assertThat().hasJsonPathValue("$.name", expectedName);
            actualJson.assertThat().hasJsonPathValue("$.description", expectedDescription);
            actualJson.assertThat().hasJsonPathValue("$.is_active", expectedIsActive);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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

            System.out.println(actualJson);

            assert actualJson.getObject().name().equals(expectedName);
            assert actualJson.getObject().description().equals(expectedDescription);
            assert actualJson.getObject().active().equals(expectedIsActive);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
