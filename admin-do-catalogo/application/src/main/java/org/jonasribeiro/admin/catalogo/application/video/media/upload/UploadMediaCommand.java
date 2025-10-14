package org.jonasribeiro.admin.catalogo.application.video.media.upload;

import org.jonasribeiro.admin.catalogo.domain.resource.VideoResource;

public record UploadMediaCommand(
        String videoId,
        VideoResource videoResource
) {

    public static UploadMediaCommand with(final String anId, final VideoResource aResource) {
        return new UploadMediaCommand(anId, aResource);
    }
}
