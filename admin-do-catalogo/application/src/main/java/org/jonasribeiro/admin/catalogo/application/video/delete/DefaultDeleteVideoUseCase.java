package org.jonasribeiro.admin.catalogo.application.video.delete;

import org.jonasribeiro.admin.catalogo.domain.video.MediaResourceGateway;
import org.jonasribeiro.admin.catalogo.domain.video.VideoGateway;
import org.jonasribeiro.admin.catalogo.domain.video.VideoID;

import java.util.Objects;

public class DefaultDeleteVideoUseCase extends DeleteVideoUseCase {

    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;



    public DefaultDeleteVideoUseCase(final VideoGateway videoGateway, MediaResourceGateway mediaResourceGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public void execute(final String anIn) {
        final var aVideoId = VideoID.from(anIn);
        this.videoGateway.deleteById(VideoID.from(anIn));
        this.mediaResourceGateway.clearResources(aVideoId);
    }
}
