package com.tsp.new_tsp_front.api.model.service.impl;

import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FrontModelJpaRepository extends JpaRepository<FrontModelEntity, Long> {
}
