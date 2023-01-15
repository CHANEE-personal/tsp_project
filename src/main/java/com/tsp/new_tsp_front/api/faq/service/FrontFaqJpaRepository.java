package com.tsp.new_tsp_front.api.faq.service;

import com.tsp.new_tsp_front.api.faq.domain.FrontFaqEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FrontFaqJpaRepository extends JpaRepository<FrontFaqEntity, Long> {
}
