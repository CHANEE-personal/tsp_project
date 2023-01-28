package com.tsp.api.portfolio.service;

import com.tsp.api.portfolio.domain.FrontPortFolioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface FrontPortFolioJpaRepository extends JpaRepository<FrontPortFolioEntity, Long> {

    @Query("select f from FrontPortFolioEntity f " +
            "left join fetch f.commonImageEntityList i " +
            "where f.idx = :idx " +
            "and f.visible = 'Y'")
    Optional<FrontPortFolioEntity> findByIdx(@Param("idx") Long idx);
}
