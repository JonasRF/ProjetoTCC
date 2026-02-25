package org.jonasribeiro.admin.catalogo.application.video.retrieve.list;

import org.jonasribeiro.admin.catalogo.application.UseCase;
import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.domain.video.VideoSearchQuery;

public abstract class ListVideosUseCase extends UseCase<VideoSearchQuery, Pagination<VideoListOutput>> {
}
