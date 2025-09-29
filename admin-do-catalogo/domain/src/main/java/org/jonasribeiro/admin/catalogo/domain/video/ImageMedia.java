package org.jonasribeiro.admin.catalogo.domain.video;

import org.jonasribeiro.admin.catalogo.domain.ValueObject;

import java.util.Objects;

public class ImageMedia extends ValueObject {

    private final String checksum;
    private final String name;
    private final String location;

    private ImageMedia(
            final String checksum,
            final String name,
            final String location
    ) {
        this.checksum = checksum;
        this.name = name;
        this.location = location;
    }

    public static ImageMedia with(
            final String checksum,
            final String name,
            final String location
    ) {
        return new ImageMedia(checksum, name, location);
    }

    public String checksum() {
        return checksum;
    }

    public String name() {
        return name;
    }

    public String location() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ImageMedia that = (ImageMedia) o;
        return Objects.equals(checksum, that.checksum) && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checksum, location);
    }
}
