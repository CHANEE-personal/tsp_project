package com.tsp.api.model.service;

import com.tsp.api.domain.model.AdminModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminModelJpaRepository extends JpaRepository<AdminModelEntity, Long> {
}
