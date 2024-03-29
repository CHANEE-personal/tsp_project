package com.tsp.api.production.service;

import com.tsp.api.production.domain.AdminProductionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AdminProductionJpaRepository extends JpaRepository<AdminProductionEntity, Long> {

    @Query("select p from AdminProductionEntity p " +
            "left join fetch p.commonImageEntityList " +
            "where p.idx = :idx " +
            "and p.visible = 'Y'")
    Optional<AdminProductionEntity> findByIdx(@Param("idx") Long idx);
}
