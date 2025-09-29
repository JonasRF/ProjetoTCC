package org.jonasribeiro.admin.catalogo.domain.video;

import org.jonasribeiro.admin.catalogo.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class VideoID extends Identifier {

        private final String value;

    private VideoID(final String value) {
        this.value = Objects.requireNonNull(value);
    }

        public static VideoID from(final String anId) {
            return new VideoID(anId.toLowerCase());
        }

        public static VideoID from(final UUID anId) {
            return new VideoID(anId.toString());
        }

    public static VideoID unique() {
        return VideoID.from(UUID.randomUUID());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
       final VideoID videoID = (VideoID) o;
        return getValue().equals(videoID.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}

