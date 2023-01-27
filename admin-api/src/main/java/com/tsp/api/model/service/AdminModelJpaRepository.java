package com.tsp.api.model.service;

import com.tsp.api.model.domain.AdminModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminModelJpaRepository extends JpaRepository<AdminModelEntity, Long> {

    @Query("select m from AdminModelEntity m " +
            "join fetch m.adminAgencyEntity " +
            "left join fetch m.commonImageEntityList " +
            "where m.idx = :idx " +
            "and m.visible = 'Y'")
    Optional<AdminModelEntity> findByIdx(@Param("idx") Long idx);

    @Query(value = "select * from tsp_model m where m.idx < :idx and m.category_cd = :categoryCd and m.visible = 'Y' order by m.idx desc limit 1", nativeQuery = true)
    Optional<AdminModelEntity> findPrevByIdx(@Param("idx") Long idx, @Param("categoryCd") Integer categoryCd);
}
