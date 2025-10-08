package org.jonasribeiro.admin.catalogo.domain.video;

import java.time.Instant;

public record VideoPreview(
        String id,
        String title,
        String description,
        Instant createdAt,
        Instant updatedAt
) {

    public VideoPreview(final Video aVideo) {
        this(
                aVideo.getId().getValue(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt()
        );
     }

    public static VideoPreview from( VideoPreview video) {
        return new VideoPreview(
                video.id(),
                video.title(),
                video.description(),
                video.createdAt(),
                video.updatedAt()
        );
    }
}
