package org.jonasribeiro.admin.catalogo.infraestructure.configuration.usecases;

import org.jonasribeiro.admin.catalogo.application.video.media.update.DefaultUpdateMediaStatusUseCase;
import org.jonasribeiro.admin.catalogo.application.video.media.update.UpdateMediaStatusUseCase;
import org.jonasribeiro.admin.catalogo.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class VideoUseCaseConfig {

    private final VideoGateway videoGateway;

    public VideoUseCaseConfig(VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Bean
    public UpdateMediaStatusUseCase updateMediaStatusUseCase() {
        return new DefaultUpdateMediaStatusUseCase(videoGateway);
    }
}
