package com.tsp.api.model.service.negotiation;

import com.tsp.api.domain.model.negotiation.AdminNegotiationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminNegotiationJpaRepository extends JpaRepository<AdminNegotiationEntity, Long> {
}
