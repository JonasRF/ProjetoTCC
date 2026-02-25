package org.jonasribeiro.admin.catalogo.infraestructure.genre.models;

import org.jonasribeiro.admin.catalogo.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.util.List;

@JacksonTest
public class CreateGenreRequestTest {

    @Autowired
    private JacksonTester<CreateGenreRequest> json;

    @Test
    public void testMarshall() throws Exception {

        final var expectedName = "Ação";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;

        final var request = new CreateGenreRequest(
                expectedName,
                expectedCategories,
                expectedIsActive
        );

        final var actualJson = this.json.write(request);

            System.out.println(actualJson);

            actualJson.assertThat().hasJsonPathValue("$.name", expectedName);
            actualJson.assertThat().hasJsonPathValue("$.categories_id", expectedCategories);
            actualJson.assertThat().hasJsonPathValue("$.is_active", expectedIsActive);
    }

    @Test
    public void testUnmarshall() throws Exception {
        final var expectedName = "Ação";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;

        final var json = """
                {
                    "name": "%s",
                    "categories_id": %s,
                    "is_active": %s
                }
                """.formatted(
                expectedName,
                expectedCategories,
                expectedIsActive
        );
        final var actualJson = this.json.parse(json);

        System.out.println(actualJson);

        actualJson.assertThat().hasFieldOrPropertyWithValue("name", expectedName);
        actualJson.assertThat().hasFieldOrPropertyWithValue("categories", expectedCategories);
        actualJson.assertThat().hasFieldOrPropertyWithValue("active", expectedIsActive);
    }
}
