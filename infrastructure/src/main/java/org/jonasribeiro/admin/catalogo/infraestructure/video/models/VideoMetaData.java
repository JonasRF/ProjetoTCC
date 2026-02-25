package org.jonasribeiro.admin.catalogo.infraestructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VideoMetaData(
        @JsonProperty("encoded_video_folder") String encodedVideoFolder,
        @JsonProperty("resource_id") String resourceId,
        @JsonProperty("file_path") String filePath
) {
}