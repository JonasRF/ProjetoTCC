package org.jonasribeiro.admin.catalogo.infraestructure.castmember.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jonasribeiro.admin.catalogo.application.castmember.retrieve.get.CastMemberOutput;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberType;

public record CastMemberResponse(
       @JsonProperty("id") String id,
       @JsonProperty("name")String name,
       @JsonProperty("type")String type,
       @JsonProperty("created_at") String createdAt,
       @JsonProperty("updated_at") String updatedAt
) {
}