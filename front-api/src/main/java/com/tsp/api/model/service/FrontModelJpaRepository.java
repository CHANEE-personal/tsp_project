package com.tsp.api.model.service;

import com.tsp.api.model.domain.FrontModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrontModelJpaRepository extends JpaRepository<FrontModelEntity, Long> {
}
