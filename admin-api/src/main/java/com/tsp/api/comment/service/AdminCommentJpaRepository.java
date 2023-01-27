package com.tsp.api.comment.service;

import com.tsp.api.comment.domain.AdminCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface AdminCommentJpaRepository extends JpaRepository<AdminCommentEntity, Long> {
    List<AdminCommentEntity> findByAdminModelEntityIdxAndCommentType(Long modelIdx, String commentType);
}