package org.jonasribeiro.admin.catalogo.infraestructure.video.persistence;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

public class VideoGenreID implements Serializable {

    @Column(name = "video_id", nullable = false)
    private String videoId;

    @Column(name = "genre_id", nullable = false)
    private String genreId;

    public VideoGenreID() {
    }

    private VideoGenreID( final String genreId, final String videoId) {
        this.genreId = genreId;
        this.videoId = videoId;
    }

    public static VideoGenreID from( final String genreId, final String videoId) {
        return new VideoGenreID(genreId, videoId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VideoGenreID that = (VideoGenreID) o;
        return Objects.equals(videoId, that.videoId) && Objects.equals(genreId, that.genreId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoId, genreId);
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }
}
