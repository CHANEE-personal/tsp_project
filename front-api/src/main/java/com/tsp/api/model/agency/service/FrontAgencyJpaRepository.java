package com.tsp.api.model.agency.service;

import com.tsp.api.model.domain.agency.FrontAgencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrontAgencyJpaRepository extends JpaRepository<FrontAgencyEntity, Long> {
}
