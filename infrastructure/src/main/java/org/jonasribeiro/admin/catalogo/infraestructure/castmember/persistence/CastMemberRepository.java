package org.jonasribeiro.admin.catalogo.infraestructure.castmember.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CastMemberRepository extends JpaRepository<CastMemberJpaEntity, String> {

    Page<CastMemberJpaEntity> findAll(Specification<CastMemberJpaEntity> specification, Pageable pageable);

    @Query(value = "SELECT c.id FROM CastMember c WHERE c.id IN :ids")
    List<String> existsByIds(@Param("ids") List<String> ids);
}
