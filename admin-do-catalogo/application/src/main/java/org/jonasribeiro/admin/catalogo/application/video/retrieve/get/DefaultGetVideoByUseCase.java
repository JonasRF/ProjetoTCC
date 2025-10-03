package org.jonasribeiro.admin.catalogo.application.video.retrieve.get;

import org.jonasribeiro.admin.catalogo.domain.exceptions.NotFoundException;
import org.jonasribeiro.admin.catalogo.domain.video.Video;
import org.jonasribeiro.admin.catalogo.domain.video.VideoGateway;
import org.jonasribeiro.admin.catalogo.domain.video.VideoID;

import java.util.Objects;

public class DefaultGetVideoByUseCase extends GetVideoByIdUseCase {

    private final VideoGateway videoGateway;

    public DefaultGetVideoByUseCase(VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public VideoOutput execute(final String anIn) {
        final var aVideoId = VideoID.from(anIn);
        return this.videoGateway.findById(aVideoId)
                .map(VideoOutput::from)
                .orElseThrow(() -> NotFoundException.with(Video.class, aVideoId));
    }
}
