package com.tsp.api.model.service.recommend;

import com.tsp.api.model.domain.recommend.AdminRecommendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRecommendJpaRepository extends JpaRepository<AdminRecommendEntity, Long> {
}
