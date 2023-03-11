package com.tsp.api.comment.service;

import com.tsp.api.comment.domain.AdminCommentDto;
import com.tsp.api.comment.domain.AdminCommentEntity;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.tsp.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class AdminCommentJpaServiceImpl implements AdminCommentJpaService {
    private final AdminCommentJpaQueryRepository adminCommentJpaQueryRepository;
    private final AdminCommentJpaRepository adminCommentJpaRepository;

    private AdminCommentEntity oneComment(Long idx) {
        return adminCommentJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_COMMENT));
    }

    /**
     * <pre>
     * 1. MethodName : findAdminCommentList
     * 2. ClassName  : AdminCommentJpaServiceImpl.java
     * 3. Comment    : 관리자 코멘트 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdminCommentDto> findAdminCommentList(Map<String, Object> commentMap, PageRequest pageRequest) {
        return adminCommentJpaQueryRepository.findAdminCommentList(commentMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOneAdminComment
     * 2. ClassName  : AdminCommentJpaServiceImpl.java
     * 3. Comment    : 관리자 코멘트 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminCommentDto findOneAdminComment(Long idx) {
        return AdminCommentEntity.toDto(oneComment(idx));
    }

    /**
     * <pre>
     * 1. MethodName : updateAdminComment
     * 2. ClassName  : AdminCommentJpaServiceImpl.java
     * 3. Comment    : 관리자 코멘트 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @Override
    @Transactional
    public AdminCommentDto updateAdminComment(Long idx, AdminCommentEntity adminCommentEntity) {
        try {
            oneComment(idx).update(adminCommentEntity);
            return AdminCommentEntity.toDto(adminCommentEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_COMMENT);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteAdminComment
     * 2. ClassName  : AdminCommentJpaServiceImpl.java
     * 3. Comment    : 관리자 코멘트 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @Override
    @Transactional
    public Long deleteAdminComment(Long idx) {
        try {
            adminCommentJpaRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_COMMENT);
        }
    }
}
