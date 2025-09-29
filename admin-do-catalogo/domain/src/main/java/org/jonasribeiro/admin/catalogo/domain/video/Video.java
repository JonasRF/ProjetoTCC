package org.jonasribeiro.admin.catalogo.domain.video;

import org.jonasribeiro.admin.catalogo.domain.AggregateRoot;
import org.jonasribeiro.admin.catalogo.domain.castmember.CastMemberID;
import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreID;
import org.jonasribeiro.admin.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;
import java.time.Year;
import java.util.Set;

public class Video extends AggregateRoot<VideoID> {

    private String title;
    private String description;
    private Year launchedAt;
    private Double duration;

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

    @Override
    public void validate(final ValidationHandler handler) {
    }

    public static Video newVideo(
            final String aTitle,
            final String aDescription,
            final Year aLaunchedAt,
            final double aDuration,
            final boolean wasOpened,
            final boolean wasPublished,
            final Rating aRating,
            final Set<CategoryID> aCategories,
            final Set<GenreID> aGenres,
            final Set<CastMemberID> aCastMembers
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
                aCategories,
                aGenres,
                aCastMembers
        );
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Year getLaunchedAt() {
        return launchedAt;
    }

    public void setLaunchedAt(Year launchedAt) {
        this.launchedAt = launchedAt;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ImageMedia getBanner() {
        return banner;
    }

    public void setBanner(ImageMedia banner) {
        this.banner = banner;
    }

    public ImageMedia getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ImageMedia thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ImageMedia getThumbnailHalf() {
        return thumbnailHalf;
    }

    public void setThumbnailHalf(ImageMedia thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
    }

    public AudioVideoMedia getVideo() {
        return video;
    }

    public void setVideo(AudioVideoMedia video) {
        this.video = video;
    }

    public AudioVideoMedia getTrailer() {
        return trailer;
    }

    public void setTrailer(AudioVideoMedia trailer) {
        this.trailer = trailer;
    }

    public Set<CategoryID> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryID> categories) {
        this.categories = categories;
    }

    public Set<GenreID> getGenres() {
        return genres;
    }

    public void setGenres(Set<GenreID> genres) {
        this.genres = genres;
    }

    public Set<CastMemberID> getCastMembers() {
        return castMembers;
    }

    public void setCastMembers(Set<CastMemberID> castMembers) {
        this.castMembers = castMembers;
    }

    public static Video 
}
