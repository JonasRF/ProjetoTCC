package org.jonasribeiro.admin.catalogo.domain.video;

import org.jonasribeiro.admin.catalogo.domain.AggregateRoot;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberID;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreID;
import org.jonasribeiro.admin.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;
import java.time.Year;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Video extends AggregateRoot<VideoID> {

    private String title;
    private String description;
    private Year launchedAt;
    private double duration;

    private boolean opened;
    private boolean published;
    private Rating rating;

    private Instant createdAt;
    private Instant updatedAt;

    private ImageMedia banner;
    private ImageMedia thumbnail;
    private ImageMedia thumbnailHalf;

    private AudioVideoMedia video;
    private AudioVideoMedia trailer;

    private Set<CategoryID> categories;
    private Set<GenreID> genres;
    private Set<CastMemberID> castMembers;

    protected Video(
           final VideoID anId,
              final String aTitle,
                final String aDescription,
                final Year aLaunchedAt,
                final double aDuration,
                final boolean wasOpened,
                final boolean wasPublished,
                final Rating aRating,
                final Instant aCreationDate,
                final Instant aUpdatedDate,
                final ImageMedia aBanner,
                final ImageMedia aThumb,
                final ImageMedia aThumbHalf,
                final AudioVideoMedia aVideo,
                final AudioVideoMedia aTrailer,
                final Set<CategoryID> aCategories,
                final Set<GenreID> aGenres,
                final Set<CastMemberID> aCastMembers
    ) {
        super(anId);
        this.title = aTitle;
        this.description = aDescription;
        this.launchedAt = aLaunchedAt;
        this.duration = aDuration;
        this.opened = wasOpened;
        this.published = wasPublished;
        this.rating = aRating;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdatedDate;
        this.banner = aBanner;
        this.thumbnail = aThumb;
        this.thumbnailHalf = aThumbHalf;
        this.video = aVideo;
        this.trailer = aTrailer;
        this.categories = aCategories;
        this.genres = aGenres;
        this.castMembers = aCastMembers;
    }

    public static Video newVideo(
            final String aTitle,
            final String aDescription,
            final Year aLaunchedAt,
            final double aDuration,
            final boolean wasOpened,
            final boolean wasPublished,
            final Rating aRating,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> members
    ) {
        final var id = VideoID.unique();
        final var now = Instant.now();
        return new Video(
                id,
                aTitle,
                aDescription,
                aLaunchedAt,
                aDuration,
                wasOpened,
                wasPublished,
                aRating,
                now,
                now,
                null,
                null,
                null,
                null,
                null,
                categories,
                genres,
                members
        );
    }

    public static Video with(final Video aVideo) {
        return new Video(
                aVideo.getId(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getLaunchedAt(),
                aVideo.getDuration(),
                aVideo.getOpened(),
                aVideo.getPublished(),
                aVideo.getRating(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt(),
                aVideo.getBanner().orElse(null),
                aVideo.getThumbnail().orElse(null),
                aVideo.getThumbnailHalf().orElse(null),
                aVideo.getVideo().orElse(null),
                aVideo.getTrailer().orElse(null),
                new HashSet<>(aVideo.getCategories()),
                new HashSet<>(aVideo.getGenres()),
                new HashSet<>(aVideo.getCastMembers())
        );
    }

    @Override
    public void validate(final ValidationHandler handler) {
    }

    public Video update(
            final String aTitle,
            final String aDescription,
            final Year aLaunchedAt,
            final double aDuration,
            final boolean wasOpened,
            final boolean wasPublished,
            final Rating aRating,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> members
    ) {
        this.title = aTitle;
        this.description = aDescription;
        this.launchedAt = aLaunchedAt;
        this.duration = aDuration;
        this.opened = wasOpened;
        this.published = wasPublished;
        this.rating = aRating;
        this.setCategories(categories);
        this.setGenres(genres);
        this.setCastMembers(members);
        this.updatedAt = Instant.now();
        return this;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Year getLaunchedAt() {
        return launchedAt;
    }

    public Double getDuration() {
        return duration;
    }

    public boolean getOpened() {
        return opened;
    }

    public boolean getPublished() {
        return published;
    }

    public Rating getRating() {
        return rating;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Video setBanner(final ImageMedia banner) {
        this.banner = banner;
        this.updatedAt = Instant.now();
        return this;
    }

    public Video setThumbnail(final ImageMedia thumbnail) {
        this.thumbnail = thumbnail;
        this.updatedAt = Instant.now();
        return this;
    }

    public Video setThumbnailHalf(final ImageMedia thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
        this.updatedAt = Instant.now();
        return this;
    }

    public Video setVideo(final AudioVideoMedia video) {
        this.video = video;
        this.updatedAt = Instant.now();
        return  this;
    }

    public Video setTrailer(final AudioVideoMedia trailer) {
        this.trailer = trailer;
        this.updatedAt = Instant.now();
        return this;
    }

    public Set<CastMemberID> getCastMembers() {
        return castMembers != null ? Collections.unmodifiableSet(castMembers) : Collections.emptySet();
    }

    public Set<CategoryID> getCategories() {
        return categories != null ? Collections.unmodifiableSet(categories) : Collections.emptySet();
    }

    public Set<GenreID> getGenres() {
        return genres != null ? Collections.unmodifiableSet(genres) : Collections.emptySet();
    }

    public Optional<ImageMedia> getBanner() {
        return Optional.ofNullable(banner);
    }

    public Optional<AudioVideoMedia> getVideo() {
        return Optional.ofNullable(video);
    }

    public Optional<ImageMedia> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    public Optional<ImageMedia> getThumbnailHalf() {
        return Optional.ofNullable(thumbnailHalf);
    }

    public Optional<AudioVideoMedia> getTrailer() {
        return Optional.ofNullable(trailer);
    }

    private void setCategories(final Set<CategoryID> categories) {
        this.categories = categories != null ? new HashSet<>(categories) : Collections.emptySet();
    }

    private void setGenres(final Set<GenreID> genres) {
        this.genres = genres != null ? new HashSet<>(genres) : Collections.emptySet();
    }

    private void setCastMembers(final Set<CastMemberID> castMembers) {
        this.castMembers = castMembers != null ? new HashSet<>(castMembers) : Collections.emptySet();
    }
}
