package org.jonasribeiro.admin.catalogo.infraestructure.api.controllers;

import org.jonasribeiro.admin.catalogo.application.video.create.CreateVideoCommand;
import org.jonasribeiro.admin.catalogo.application.video.create.CreateVideoUseCase;
import org.jonasribeiro.admin.catalogo.domain.video.Resource;
import org.jonasribeiro.admin.catalogo.infraestructure.api.VideoAPI;
import org.jonasribeiro.admin.catalogo.infraestructure.utils.HashingUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

@RestController
public class VideoController implements VideoAPI {

    private final CreateVideoUseCase createVideoUseCase;

    public VideoController(CreateVideoUseCase createVideoUseCase) {
        this.createVideoUseCase = Objects.requireNonNull(createVideoUseCase);
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
