package com.tsp.api.comment.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.api.comment.domain.AdminCommentDTO;
import com.tsp.api.comment.domain.AdminCommentEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.tsp.api.comment.domain.AdminCommentEntity.toDtoList;
import static com.tsp.api.comment.domain.QAdminCommentEntity.adminCommentEntity;
import static com.tsp.common.StringUtil.getString;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminCommentJpaQueryRepository {
    private final JPAQueryFactory queryFactory;

    private BooleanExpression searchComment(Map<String, Object> commentMap) {
        String searchKeyword = getString(commentMap.get("searchKeyword"), "");
        return adminCommentEntity.comment.contains(searchKeyword);
    }

    /**
     * <pre>
     * 1. MethodName : findAdminCommentList
     * 2. ClassName  : AdminCommentJpaRepository.java
     * 3. Comment    : 관리자 어드민 코멘트 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    public Page<AdminCommentDTO> findAdminCommentList(Map<String, Object> commentMap, PageRequest pageRequest) {
        List<AdminCommentEntity> commentList = queryFactory
                .selectFrom(adminCommentEntity)
                .orderBy(adminCommentEntity.idx.desc())
                .where(searchComment(commentMap))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(toDtoList(commentList), pageRequest, commentList.size());
    }
}
