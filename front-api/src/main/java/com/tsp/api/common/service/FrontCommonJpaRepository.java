package com.tsp.api.common.service;

import com.tsp.api.common.domain.NewCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrontCommonJpaRepository extends JpaRepository<NewCodeEntity, Long> {
}
