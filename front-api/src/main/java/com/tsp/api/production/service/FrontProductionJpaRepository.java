package com.tsp.api.production.service;

import com.tsp.api.production.domain.FrontProductionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface FrontProductionJpaRepository extends JpaRepository<FrontProductionEntity, Long> {

    @Query("select p from FrontProductionEntity p " +
            "left join fetch p.commonImageEntityList i " +
            "where p.idx = :idx " +
            "and p.visible = 'Y'")
    Optional<FrontProductionEntity> findByIdx(@Param("idx") Long idx);
}
