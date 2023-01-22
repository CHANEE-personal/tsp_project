package com.tsp.api.notice.service;

import com.tsp.api.domain.notice.AdminNoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminNoticeJpaRepository extends JpaRepository<AdminNoticeEntity, Long> {
}
