package org.jonasribeiro.admin.catalogo.infraestructure.category.models;

import org.jonasribeiro.admin.catalogo.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

@JacksonTest
public class CategoryResponseTest {

    @Autowired
    private JacksonTester<CategoryResponse> json;

    @Test
    public void testMarshall() {
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var response = new CategoryResponse(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedDeletedAt
        );

        try {
            final var actualJson = this.json.write(response);

            System.out.println(actualJson);

            actualJson.assertThat().hasJsonPathValue("$.id", expectedId);
            actualJson.assertThat().hasJsonPathValue("$.name", expectedName);
            actualJson.assertThat().hasJsonPathValue("$.description", expectedDescription);
            actualJson.assertThat().hasJsonPathValue("$.is_active", expectedIsActive);
            actualJson.assertThat().hasJsonPathValue("$.created_at", expectedCreatedAt.toString());
            actualJson.assertThat().hasJsonPathValue("$.updated_at", expectedUpdatedAt.toString());
            actualJson.assertThat().hasJsonPathValue("$.deleted_at", expectedDeletedAt.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testUnmarshall() {
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var json = """
                {
                    "id": "%s",
                    "name": "%s",
                    "description": "%s",
                    "is_active": %s,
                    "created_at": "%s",
                    "updated_at": "%s",
                    "deleted_at": "%s"
                }
                """.formatted(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedCreatedAt.toString(),
                expectedUpdatedAt.toString(),
                expectedDeletedAt.toString()
        );

        try {
            final var actualJson = this.json.parse(json);

            System.out.println(actualJson);

            actualJson.assertThat().hasFieldOrPropertyWithValue("id", expectedId);
            actualJson.assertThat().hasFieldOrPropertyWithValue("name", expectedName);
            actualJson.assertThat().hasFieldOrPropertyWithValue("description", expectedDescription);
            actualJson.assertThat().hasFieldOrPropertyWithValue("active", expectedIsActive);
            actualJson.assertThat().hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt);
            actualJson.assertThat().hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt);
            actualJson.assertThat().hasFieldOrPropertyWithValue("deletedAt", expectedDeletedAt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
