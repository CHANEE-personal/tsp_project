package com.tsp.api.comment.service;

import com.tsp.api.domain.comment.AdminCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminCommentJpaRepository extends JpaRepository<AdminCommentEntity, Long> {
    List<AdminCommentEntity> findByAdminModelEntityIdxAndCommentType(Long modelIdx, String commentType);
}
