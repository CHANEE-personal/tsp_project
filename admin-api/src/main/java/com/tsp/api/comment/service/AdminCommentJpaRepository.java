package com.tsp.api.comment.service;

import com.tsp.api.comment.domain.AdminCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@EnableJpaRepositories
@Transactional(readOnly = true)
public interface AdminCommentJpaRepository extends JpaRepository<AdminCommentEntity, Long> {
    List<AdminCommentEntity> findByAdminModelEntityIdxAndCommentType(Long modelIdx, String commentType);
}
