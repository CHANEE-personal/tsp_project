package com.tsp.api.notice.service;

import com.tsp.api.notice.domain.AdminNoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminNoticeJpaRepository extends JpaRepository<AdminNoticeEntity, Long> {
}
