package com.tsp.api.faq.service;

import com.tsp.api.faq.domain.AdminFaqEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminFaqJpaRepository extends JpaRepository<AdminFaqEntity, Long> {
}
