package com.tsp.new_tsp_front.api.festival.service;

import com.tsp.new_tsp_front.api.festival.domain.FrontFestivalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FrontFestivalJpaRepository extends JpaRepository<FrontFestivalEntity, Long> {
}
