package com.tsp.api.common.service;

import com.tsp.api.common.domain.CommonImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrontCommonImageJpaRepository extends JpaRepository<CommonImageEntity, Long> {
}
