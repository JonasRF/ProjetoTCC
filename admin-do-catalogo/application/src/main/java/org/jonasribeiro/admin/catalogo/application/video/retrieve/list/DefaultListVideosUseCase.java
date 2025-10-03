package org.jonasribeiro.admin.catalogo.application.video.retrieve.list;

import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.domain.video.VideoGateway;
import org.jonasribeiro.admin.catalogo.domain.video.VideoSearchQuery;

public class DefaultListVideosUseCase extends  ListVideosUseCase{

   private final VideoGateway videoGateway;

    public DefaultListVideosUseCase(VideoGateway videoGateway) {
        this.videoGateway = videoGateway;
    }

    @Override
    public Pagination<VideoListOutput> execute(VideoSearchQuery aQuery) {
        return this.videoGateway.findAll(aQuery)
                .map(VideoListOutput::from);
    }
}
