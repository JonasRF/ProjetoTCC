package org.jonasribeiro.admin.catalogo.infraestructure.category.models;


import org.jonasribeiro.admin.catalogo.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

@JacksonTest
class CategoryListResponseTest {

    @Autowired
    private JacksonTester<CategoryListResponse> json;

    @Test
    public void testMarshall() {
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var response = new CategoryListResponse(
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
}
