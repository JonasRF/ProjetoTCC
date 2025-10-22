package org.jonasribeiro.admin.catalogo.application.video.media.get;

import org.jonasribeiro.admin.catalogo.domain.exceptions.NotFoundException;
import org.jonasribeiro.admin.catalogo.domain.validation.Error;
import org.jonasribeiro.admin.catalogo.domain.video.MediaResourceGateway;
import org.jonasribeiro.admin.catalogo.domain.video.VideoID;
import org.jonasribeiro.admin.catalogo.domain.video.VideoMediaType;

import java.util.Objects;

public class DefaultGetMediaUseCase extends GetMediaUseCase {

    private final MediaResourceGateway mediaResourceGateway;

    public DefaultGetMediaUseCase(MediaResourceGateway mediaResourceGateway) {
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public MediaOutput execute(GetMediaCommand aCmd) {
       final var anId = VideoID.from(aCmd.videoId());
       final var aType = VideoMediaType.of(aCmd.mediaType())
                .orElseThrow(() -> typeNotFound(aCmd.mediaType()));

       final var resource = this.mediaResourceGateway.getResource(anId, aType)
               .orElseThrow(() -> notFound(aCmd.videoId(), aCmd.mediaType()));

       return  MediaOutput.with(resource);
    }

    private NotFoundException notFound(final String anId, final String aType) {
        return NotFoundException.with(new Error("Resource %s for video %s was not found".formatted(aType, anId)));
    }

    private NotFoundException typeNotFound( final String aType) {
        return NotFoundException.with(new Error("Media type %s doesn't exists".formatted(aType)));
    }
}
