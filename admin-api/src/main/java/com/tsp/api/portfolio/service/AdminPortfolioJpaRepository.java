package com.tsp.api.portfolio.service;

import com.tsp.api.domain.portfolio.AdminPortFolioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminPortfolioJpaRepository extends JpaRepository<AdminPortFolioEntity, Long> {
}
