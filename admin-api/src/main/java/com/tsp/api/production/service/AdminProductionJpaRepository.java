package com.tsp.api.production.service;

import com.tsp.api.domain.production.AdminProductionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminProductionJpaRepository extends JpaRepository<AdminProductionEntity, Long> {
}
