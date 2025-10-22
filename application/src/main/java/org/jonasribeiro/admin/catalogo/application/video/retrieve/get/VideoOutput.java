package org.jonasribeiro.admin.catalogo.application.video.retrieve.get;

import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberID;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreID;
import org.jonasribeiro.admin.catalogo.domain.utils.CollectionsUtils;
import org.jonasribeiro.admin.catalogo.domain.video.AudioVideoMedia;
import org.jonasribeiro.admin.catalogo.domain.video.ImageMedia;
import org.jonasribeiro.admin.catalogo.domain.video.Rating;
import org.jonasribeiro.admin.catalogo.domain.video.Video;

import java.time.Instant;
import java.util.Set;

public record VideoOutput(
        String id,
        Instant createdAt,
        Instant updatedAt,
        String title,
        String description,
        int launchedAt,
        double duration,
        boolean opened,
        boolean published,
        Rating rating,
        Set<String> categories,
        Set<String> genres,
        Set<String> castMembers,
        AudioVideoMedia video,
        AudioVideoMedia trailer,
        ImageMedia banner,
        ImageMedia thumbnail,
        ImageMedia thumbnailHalf
) {

    public static VideoOutput from(final Video aVideo) {
        return new VideoOutput(
                aVideo.getId().getValue(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getLaunchedAt().getValue(),
                aVideo.getDuration(),
                aVideo.getOpened(),
                aVideo.getPublished(),
                aVideo.getRating(),
                CollectionsUtils.mapTo(aVideo.getCategories(), CategoryID::getValue),
                CollectionsUtils.mapTo(aVideo.getGenres(), GenreID::getValue),
                CollectionsUtils.mapTo(aVideo.getCastMembers(), CastMemberID::getValue),
                aVideo.getVideo().orElse(null),
                aVideo.getTrailer().orElse(null),
                aVideo.getBanner().orElse(null),
                aVideo.getThumbnail().orElse(null),
                aVideo.getThumbnailHalf().orElse(null)

        );
    }
}