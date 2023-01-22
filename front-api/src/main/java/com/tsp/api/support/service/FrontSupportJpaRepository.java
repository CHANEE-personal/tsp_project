package com.tsp.api.support.service;

import com.tsp.api.support.domain.FrontSupportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrontSupportJpaRepository extends JpaRepository<FrontSupportEntity, Long> {
}
