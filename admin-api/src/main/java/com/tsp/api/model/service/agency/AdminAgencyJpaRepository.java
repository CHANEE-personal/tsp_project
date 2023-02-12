package com.tsp.api.model.service.agency;

import com.tsp.api.model.domain.agency.AdminAgencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminAgencyJpaRepository extends JpaRepository<AdminAgencyEntity, Long> {
    @Query("select a from AdminAgencyEntity a " +
            "left join fetch a.commonImageEntityList " +
            "where a.idx = :idx " +
            "and a.visible = 'Y'")
    Optional<AdminAgencyEntity> findByIdx(@Param("idx") Long idx);
}
