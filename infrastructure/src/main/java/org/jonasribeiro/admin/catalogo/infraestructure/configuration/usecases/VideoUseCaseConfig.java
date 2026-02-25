package org.jonasribeiro.admin.catalogo.infraestructure.configuration.usecases;

import org.jonasribeiro.admin.catalogo.application.video.create.CreateVideoUseCase;
import org.jonasribeiro.admin.catalogo.application.video.create.DefaultCreateVideoUseCase;
import org.jonasribeiro.admin.catalogo.application.video.delete.DefaultDeleteVideoUseCase;
import org.jonasribeiro.admin.catalogo.application.video.delete.DeleteVideoUseCase;
import org.jonasribeiro.admin.catalogo.application.video.media.get.DefaultGetMediaUseCase;
import org.jonasribeiro.admin.catalogo.application.video.media.get.GetMediaUseCase;
import org.jonasribeiro.admin.catalogo.application.video.media.update.DefaultUpdateMediaStatusUseCase;
import org.jonasribeiro.admin.catalogo.application.video.media.update.UpdateMediaStatusUseCase;
import org.jonasribeiro.admin.catalogo.application.video.media.upload.DefaultUploadMediaUseCase;
import org.jonasribeiro.admin.catalogo.application.video.media.upload.UploadMediaUseCase;
import org.jonasribeiro.admin.catalogo.application.video.retrieve.get.DefaultGetVideoByUseCase;
import org.jonasribeiro.admin.catalogo.application.video.retrieve.get.GetVideoByIdUseCase;
import org.jonasribeiro.admin.catalogo.application.video.retrieve.list.DefaultListVideosUseCase;
import org.jonasribeiro.admin.catalogo.application.video.retrieve.list.ListVideosUseCase;
import org.jonasribeiro.admin.catalogo.application.video.update.DefaultUpdateVideoUseCase;
import org.jonasribeiro.admin.catalogo.application.video.update.UpdateVideoUseCase;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberGateway;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryGateway;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreGateway;
import org.jonasribeiro.admin.catalogo.domain.video.MediaResourceGateway;
import org.jonasribeiro.admin.catalogo.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class VideoUseCaseConfig {

    private final CategoryGateway categoryGateway;
    private final CastMemberGateway castMemberGateway;
    private final GenreGateway genreGateway;
    private final MediaResourceGateway mediaResourceGateway;
    private final VideoGateway videoGateway;

    public VideoUseCaseConfig(
            final CategoryGateway categoryGateway,
            final CastMemberGateway castMemberGateway,
            final GenreGateway genreGateway,
            final MediaResourceGateway mediaResourceGateway,
            final VideoGateway videoGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Bean
    public CreateVideoUseCase createVideoUseCase() {
        return new DefaultCreateVideoUseCase(categoryGateway, genreGateway, castMemberGateway, mediaResourceGateway, videoGateway);
    }

    @Bean
    public UpdateVideoUseCase updateVideoUseCase() {
        return new DefaultUpdateVideoUseCase(categoryGateway, genreGateway, castMemberGateway, mediaResourceGateway,videoGateway);
    }

    @Bean
    public GetVideoByIdUseCase getVideoByIdUseCase() {
        return new DefaultGetVideoByUseCase(videoGateway);
    }

    @Bean
    public DeleteVideoUseCase deleteVideoUseCase() {
        return new DefaultDeleteVideoUseCase(videoGateway, mediaResourceGateway);
    }

    @Bean
    public ListVideosUseCase listVideosUseCase() {
        return new DefaultListVideosUseCase(videoGateway);
    }

    @Bean
    public GetMediaUseCase getMediaUseCase() {
        return new DefaultGetMediaUseCase(mediaResourceGateway);
    }

    @Bean
    public UploadMediaUseCase uploadMediaUseCase() {
        return new DefaultUploadMediaUseCase(mediaResourceGateway, videoGateway);
    }

    @Bean
    public UpdateMediaStatusUseCase updateMediaStatusUseCase() {
        return new DefaultUpdateMediaStatusUseCase(videoGateway);
    }
}