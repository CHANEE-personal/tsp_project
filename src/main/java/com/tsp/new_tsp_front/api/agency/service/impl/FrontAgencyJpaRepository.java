package com.tsp.new_tsp_front.api.agency.service.impl;

import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FrontAgencyJpaRepository extends JpaRepository<FrontAgencyEntity, Long> {
}
