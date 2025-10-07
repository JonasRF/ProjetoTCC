package org.jonasribeiro.admin.catalogo.infraestructure.video.persistence;

import org.jonasribeiro.admin.catalogo.domain.category.CategoryID;
import org.jonasribeiro.admin.catalogo.domain.genre.GenreID;
import org.jonasribeiro.admin.catalogo.domain.pagination.Pagination;
import org.jonasribeiro.admin.catalogo.domain.video.VideoPreview;
import org.jonasribeiro.admin.catalogo.infraestructure.api.CastMemberAPI;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VideoRepository extends JpaRepository<VideoJpaEntity, String> {

    @Query("""
            select new org.jonasribeiro.admin.catalogo.domain.video.VideoPreview(
                v.id,
                v.title,
                v.description,
                v.createdAt,
                v.updatedAt
            )
            """)
    Pagination<VideoPreview> findAll(
          @Param("terms") String terms,
          @Param("castMembers") Iterable<String> castMembers,
          @Param("categories")Iterable<String> categories,
          @Param("genres") Iterable<String> genres,
          Pageable page
    );
}