package org.jonasribeiro.admin.catalogo.application.video.media.upload;

import org.jonasribeiro.admin.catalogo.domain.video.Video;
import org.jonasribeiro.admin.catalogo.domain.video.VideoMediaType;

public record UploadMediaOutput(
        String videoId,
        VideoMediaType mediaType
) {

    public static UploadMediaOutput with(final Video aVideo, final VideoMediaType aType) {
        return new UploadMediaOutput(aVideo.getId().getValue(), aType);
    }
}