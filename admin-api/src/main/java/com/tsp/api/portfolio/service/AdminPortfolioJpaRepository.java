package com.tsp.api.portfolio.service;

import com.tsp.api.portfolio.domain.AdminPortFolioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AdminPortfolioJpaRepository extends JpaRepository<AdminPortFolioEntity, Long> {

    @Query("select f from AdminPortFolioEntity f " +
            "left join fetch f.commonImageEntityList " +
            "where f.idx = :idx " +
            "and f.visible = 'Y'")
    Optional<AdminPortFolioEntity> findByIdx(@Param("idx") Long idx);
}
