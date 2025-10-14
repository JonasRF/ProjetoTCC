package org.jonasribeiro.admin.catalogo.application.video.media.get;

public record GetMediaCommand(
        String videoId,
        String mediaType
) {

    public static GetMediaCommand with(final String aVideoId, final String aType) {
        return new GetMediaCommand(aVideoId, aType);
    }
}
