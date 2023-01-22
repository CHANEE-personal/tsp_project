package com.tsp.api.notice.service;

import com.tsp.api.notice.domain.FrontNoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrontNoticeJpaRepository extends JpaRepository<FrontNoticeEntity, Long> {
}
