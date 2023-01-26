package com.tsp.api.model.negotiation.service;

import com.tsp.api.model.domain.negotiation.FrontNegotiationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrontNegotiationJpaRepository extends JpaRepository<FrontNegotiationEntity, Long> {
}
