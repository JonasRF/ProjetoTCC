package org.jonasribeiro.admin.catalogo.domain.resource;

import org.jonasribeiro.admin.catalogo.domain.ValueObject;
import org.jonasribeiro.admin.catalogo.domain.video.Resource;
import org.jonasribeiro.admin.catalogo.domain.video.VideoMediaType;

import java.util.Objects;

public class VideoResource extends ValueObject {

    private final Resource resource;
    private final VideoMediaType type;


    public VideoResource(Resource resource, VideoMediaType type) {
        this.resource = Objects.requireNonNull(resource);
        this.type = Objects.requireNonNull(type);
    }

    public static VideoResource with(final Resource resource, final VideoMediaType type) {
        return new VideoResource(resource, type);
    }

    public Resource resource() {
        return resource;
    }

    public VideoMediaType type() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VideoResource that = (VideoResource) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type);
    }
}