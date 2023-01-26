package com.tsp.api.model.service.agency;

import com.tsp.api.model.domain.agency.AdminAgencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminAgencyJpaRepository extends JpaRepository<AdminAgencyEntity, Long> {
}
