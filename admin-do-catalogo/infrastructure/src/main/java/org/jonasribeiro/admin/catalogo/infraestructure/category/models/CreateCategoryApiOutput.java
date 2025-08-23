package org.jonasribeiro.admin.catalogo.infraestructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateCategoryApiOutput(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt,
        @JsonProperty("deleted_at") String deletedAt
) {

}
