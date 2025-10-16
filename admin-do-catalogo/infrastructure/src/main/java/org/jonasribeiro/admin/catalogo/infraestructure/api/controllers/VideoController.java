package org.jonasribeiro.admin.catalogo.infraestructure.api.controllers;

import org.jonasribeiro.admin.catalogo.application.video.create.CreateVideoCommand;
import org.jonasribeiro.admin.catalogo.application.video.create.CreateVideoUseCase;
import org.jonasribeiro.admin.catalogo.application.video.delete.DeleteVideoUseCase;
import org.jonasribeiro.admin.catalogo.application.video.retrieve.get.GetVideoByIdUseCase;
import org.jonasribeiro.admin.catalogo.application.video.update.UpdateVideoCommand;
import org.jonasribeiro.admin.catalogo.application.video.update.UpdateVideoUseCase;
import org.jonasribeiro.admin.catalogo.domain.video.Resource;
import org.jonasribeiro.admin.catalogo.infraestructure.api.VideoAPI;
import org.jonasribeiro.admin.catalogo.infraestructure.utils.HashingUtils;
import org.jonasribeiro.admin.catalogo.infraestructure.video.models.CreateVideoRequest;
import org.jonasribeiro.admin.catalogo.infraestructure.video.models.VideoResponse;
import org.jonasribeiro.admin.catalogo.infraestructure.video.presenters.VideoApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

@RestController
public class VideoController implements VideoAPI {

    private final CreateVideoUseCase createVideoUseCase;
    private final GetVideoByIdUseCase getVideoByIdUseCase;
    private final UpdateVideoUseCase updateVideoUseCase;
    private final DeleteVideoUseCase deleteVideoUseCase;

    public VideoController(CreateVideoUseCase createVideoUseCase, GetVideoByIdUseCase getVideoByIdUseCase, UpdateVideoUseCase updateVideoUseCase, DeleteVideoUseCase deleteVideoUseCase) {
        this.createVideoUseCase = Objects.requireNonNull(createVideoUseCase);
        this.getVideoByIdUseCase = Objects.requireNonNull(getVideoByIdUseCase);
        this.updateVideoUseCase = Objects.requireNonNull(updateVideoUseCase);
        this.deleteVideoUseCase = Objects.requireNonNull(deleteVideoUseCase);
    }

    @Override
    public ResponseEntity<?> createFull(
            final String aTitle,
            final String aDescription,
            final Integer launchedAt,
            final Double aDuration,
            final Boolean WasOpened,
            final Boolean WasPublished,
            final String aRating,
            final Set<String> categories,
            final Set<String> castMembers,
            final Set<String> genres,
            final MultipartFile videoFile,
            final MultipartFile trailerFile,
            final MultipartFile bannerFile,
            final MultipartFile thumbFile,
            final MultipartFile thumbHalfFile
    ) {
        final var aCmd = new CreateVideoCommand(
                aTitle,
                aDescription,
                launchedAt,
                aDuration,
                WasOpened,
                WasPublished,
                aRating,
                categories,
                genres,
                castMembers,
                resourceOf(videoFile),
                resourceOf(trailerFile),
                resourceOf(bannerFile),
                resourceOf(thumbFile),
                resourceOf(thumbHalfFile)
        );

        final var output = this.createVideoUseCase.execute(aCmd);

        return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
    }

    @Override
    public ResponseEntity<?> createPartial(final CreateVideoRequest payload) {
        final var aCmd = CreateVideoCommand.with(
                payload.title(),
                payload.description(),
                payload.yearLaunched(),
                payload.duration(),
                payload.opened(),
                payload.published(),
                payload.rating(),
                payload.categories(),
                payload.genres(),
                payload.castMembers()
        );
        final var output = this.createVideoUseCase.execute(aCmd);

        return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
    }

    @Override
    public VideoResponse getById(final String anId) {
       return VideoApiPresenter.present(this.getVideoByIdUseCase.execute(anId));
    }

    @Override
    public ResponseEntity<?> update(final String id, final CreateVideoRequest payload) {
        final var aCmd = UpdateVideoCommand.with(
                id,
                payload.title(),
                payload.description(),
                payload.yearLaunched(),
                payload.duration(),
                payload.opened(),
                payload.published(),
                payload.rating(),
                payload.categories(),
                payload.genres(),
                payload.castMembers()
        );
        final var output = this.updateVideoUseCase.execute(aCmd);

        return ResponseEntity.ok()
                .location(URI.create("/videos/" + output.id()))
                .body(VideoApiPresenter.present(output));
    }

    @Override
    public void deleteById(String id) {
        this.deleteVideoUseCase.execute(id);
    }

    private Resource resourceOf(final MultipartFile part) {
        if (part == null) {
            return null;
        }
        try {
            return Resource.with(
                    part.getBytes(),
                    HashingUtils.checksum(part.getBytes()),
                    part.getContentType(),
                    part.getOriginalFilename()
            );
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
