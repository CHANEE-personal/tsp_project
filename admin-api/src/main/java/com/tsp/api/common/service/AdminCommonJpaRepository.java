package com.tsp.api.common.service;

import com.tsp.api.domain.common.NewCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminCommonJpaRepository extends JpaRepository<NewCodeEntity, Long> {
    Optional<NewCodeEntity> findByCategoryCd(Integer categoryCd);
}
