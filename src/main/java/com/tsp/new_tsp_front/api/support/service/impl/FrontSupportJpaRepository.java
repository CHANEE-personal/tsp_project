package com.tsp.new_tsp_front.api.support.service.impl;

import com.tsp.new_tsp_front.api.support.domain.FrontSupportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FrontSupportJpaRepository extends JpaRepository<FrontSupportEntity, Long> {
}
