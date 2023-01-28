package com.tsp.api.model.recommend.service;

import com.tsp.api.model.domain.recommend.FrontRecommendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Repository
public interface FrontRecommendJpaRepository extends JpaRepository<FrontRecommendEntity, Long> {
}
