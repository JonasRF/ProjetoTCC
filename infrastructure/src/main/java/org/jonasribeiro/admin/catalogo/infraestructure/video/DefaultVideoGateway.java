package org.jonasribeiro.admin.catalogo.infraestructure.video;

import org.jonasribeiro.admin.catalogo.domain.Identifier;
import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.domain.video.*;
import org.jonasribeiro.admin.catalogo.infraestructure.configuration.annotations.VideoCreatedQueue;
import org.jonasribeiro.admin.catalogo.infraestructure.services.EventService;
import org.jonasribeiro.admin.catalogo.infraestructure.utils.SqlUtils;
import org.jonasribeiro.admin.catalogo.infraestructure.video.persistence.VideoJpaEntity;
import org.jonasribeiro.admin.catalogo.infraestructure.video.persistence.VideoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static org.jonasribeiro.admin.catalogo.domain.utils.CollectionsUtils.mapTo;
import static org.jonasribeiro.admin.catalogo.domain.utils.CollectionsUtils.nullIfEmpty;

@Component
public class DefaultVideoGateway implements VideoGateway {

    private final VideoRepository videoRepository;

    private final EventService eventService;

    public DefaultVideoGateway(
            @VideoCreatedQueue final EventService eventService,
                               final VideoRepository videoRepository
    ) {
        this.videoRepository = Objects.requireNonNull(videoRepository);
        this.eventService =  Objects.requireNonNull(eventService);
    }

    @Override
    @Transactional
    public Video create(final Video aVideo) {
        return save(aVideo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Video> findById(VideoID anId) {
        return this.videoRepository.findById(anId.getValue())
                .map(VideoJpaEntity::toAggregate);
    }

    @Override
    public void deleteById(final VideoID anId) {
        final var aVideoId = anId.getValue();
        if (this.videoRepository.existsById(aVideoId)) {
            this.videoRepository.deleteById(aVideoId);
        }
    }

    @Override
    @Transactional
    public Video update(final Video aVideo) {
        return save(aVideo);
    }

    @Override
    public Pagination<VideoPreview> findAll(VideoSearchQuery aQuery) {

      final var page =  PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

       final var actualPage = this.videoRepository.findAll(
                SqlUtils.like(aQuery.terms()),
                nullIfEmpty(mapTo(aQuery.castMembers(), Identifier::getValue)),
                nullIfEmpty(mapTo(aQuery.categories(), Identifier::getValue)),
                nullIfEmpty(mapTo(aQuery.genres(), Identifier::getValue)),
                page
        );

      return new Pagination<>(
              actualPage.getNumber(),
              actualPage.getSize(),
              actualPage.getTotalElements(),
              actualPage.toList()
      );
    }

    private Video save(final Video aVideo) {
       final var result = this.videoRepository.save(VideoJpaEntity.from(aVideo)).toAggregate();

         aVideo.publishDomainEvents(this.eventService::send);

         return result;
    }
}
