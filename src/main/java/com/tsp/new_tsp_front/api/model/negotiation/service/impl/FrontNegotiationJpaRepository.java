package com.tsp.new_tsp_front.api.model.negotiation.service.impl;

import com.tsp.new_tsp_front.api.model.domain.negotiation.FrontNegotiationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrontNegotiationJpaRepository extends JpaRepository<FrontNegotiationEntity, Long> {
}
