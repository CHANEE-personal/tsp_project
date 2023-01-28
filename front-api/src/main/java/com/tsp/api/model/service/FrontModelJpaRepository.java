package com.tsp.api.model.service;

import com.tsp.api.model.domain.FrontModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public interface FrontModelJpaRepository extends JpaRepository<FrontModelEntity, Long> {

    @Query("select m from FrontModelEntity m " +
            "join fetch m.frontAgencyEntity " +
            "left join fetch m.commonImageEntityList " +
            "where m.idx = :idx " +
            "and m.visible = 'Y'")
    Optional<FrontModelEntity> findByIdx(@Param("idx") Long idx);

    @Query("select m from FrontModelEntity m " +
            "left join fetch m.commonImageEntityList i " +
            "where m.modelMainYn = 'Y'" +
            "and m.visible = 'Y'" +
            "and i.imageType = 'main'")
    List<FrontModelEntity> findMainModelList();
}
