package org.jonasribeiro.admin.catalogo.infraestructure.video;

import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.domain.video.*;
import org.jonasribeiro.admin.catalogo.infraestructure.video.persistence.VideoJpaEntity;
import org.jonasribeiro.admin.catalogo.infraestructure.video.persistence.VideoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

public class DefaultVideoGateway implements VideoGateway {

    private final VideoRepository videoRepository;

    public DefaultVideoGateway(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
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
    public void deleteById(VideoID anId) {
        final var aVideoId = anId.getValue();
        if (this.videoRepository.existsById(aVideoId)) {
            this.videoRepository.deleteById(aVideoId);
        }
    }

    @Override
    @Transactional
    public Video update(Video aVideo) {
        return save(aVideo);
    }

    @Override
    public Pagination<VideoPreview> findAll(VideoSearchQuery aQuery) {

        PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        this.videoRepository.findAll(
                aQuery.terms(),
                aQuery.castMembers(),
                aQuery.categories(),
                aQuery.genres(),
                page
        );

      return null;
    }

    private Video save(final Video aVideo) {
        return this.videoRepository.save(VideoJpaEntity.from(aVideo)).toAggregate();
    }
}
