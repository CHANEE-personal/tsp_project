package com.tsp.api.notice.service;

import com.tsp.api.domain.notice.AdminNoticeDTO;
import com.tsp.api.domain.notice.AdminNoticeEntity;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.tsp.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class AdminNoticeJpaServiceImpl implements AdminNoticeJpaService {
    private final AdminNoticeJpaQueryRepository adminNoticeJpaQueryRepository;
    private final AdminNoticeJpaRepository adminNoticeJpaRepository;

    private AdminNoticeEntity oneNotice(Long idx) {
        return adminNoticeJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_NOTICE));
    }


    /**
     * <pre>
     * 1. MethodName : findNoticeList
     * 2. ClassName  : AdminNoticeServiceImpl.java
     * 3. Comment    : 관리자 공지사항 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdminNoticeDTO> findNoticeList(Map<String, Object> noticeMap, PageRequest pageRequest) {
        return adminNoticeJpaQueryRepository.findNoticeList(noticeMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOneNotice
     * 2. ClassName  : AdminNoticeServiceImpl.java
     * 3. Comment    : 관리자 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminNoticeDTO findOneNotice(Long idx) {
        return AdminNoticeEntity.toDto(oneNotice(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneNotice
     * 2. ClassName  : AdminNoticeServiceImpl.java
     * 3. Comment    : 관리자 이전 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 18.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminNoticeDTO findPrevOneNotice(Long idx) {
        return adminNoticeJpaQueryRepository.findPrevOneNotice(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneNotice
     * 2. ClassName  : AdminNoticeServiceImpl.java
     * 3. Comment    : 관리자 다음 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 18.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminNoticeDTO findNextOneNotice(Long idx) {
        return adminNoticeJpaQueryRepository.findNextOneNotice(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertNotice
     * 2. ClassName  : AdminNoticeServiceImpl.java
     * 3. Comment    : 관리자 공지사항 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    @Override
    @Transactional
    public AdminNoticeDTO insertNotice(AdminNoticeEntity adminNoticeEntity) {
        try {
            return AdminNoticeEntity.toDto(adminNoticeJpaRepository.save(adminNoticeEntity));
        } catch (Exception e) {
            throw new TspException(ERROR_NOTICE);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateNotice
     * 2. ClassName  : AdminNoticeServiceImpl.java
     * 3. Comment    : 관리자 공지사항 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    @Override
    @Transactional
    public AdminNoticeDTO updateNotice(Long idx, AdminNoticeEntity adminNoticeEntity) {
        try {
            oneNotice(idx).update(adminNoticeEntity);
            return AdminNoticeEntity.toDto(adminNoticeEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_NOTICE);
        }
    }

    /**
     * <pre>
     * 1. MethodName : toggleFixed
     * 2. ClassName  : AdminNoticeServiceImpl.java
     * 3. Comment    : 관리자 공지사항 상단 고정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 23.
     * </pre>
     */
    @Override
    @Transactional
    public Boolean toggleFixed(Long idx) {
        try {
            AdminNoticeEntity oneNotice = oneNotice(idx);
            Optional.ofNullable(oneNotice)
                    .ifPresent(adminNotice -> adminNotice.toggleTopFixed(oneNotice.getTopFixed()));
            return Objects.requireNonNull(oneNotice).getTopFixed();
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_NOTICE);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteNotice
     * 2. ClassName  : AdminNoticeServiceImpl.java
     * 3. Comment    : 관리자 공지사항 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    @Override
    @Transactional
    public Long deleteNotice(Long idx) {
        try {
            adminNoticeJpaRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_NOTICE);
        }
    }
}
