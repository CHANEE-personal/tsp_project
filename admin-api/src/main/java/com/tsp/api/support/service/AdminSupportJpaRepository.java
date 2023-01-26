package com.tsp.api.support.service;

import com.tsp.api.domain.support.AdminSupportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminSupportJpaRepository extends JpaRepository<AdminSupportEntity, Long> {
}
