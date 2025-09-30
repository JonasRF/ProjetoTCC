package org.jonasribeiro.admin.catalogo.application.video;

import org.jonasribeiro.admin.catalogo.domain.video.Video;

public record CreateVideoOutput(String id) {
    public static CreateVideoOutput from(final Video aVideo) {
        return new CreateVideoOutput(aVideo.getId().getValue());
    }
}
