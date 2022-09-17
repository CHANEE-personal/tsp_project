package com.tsp.new_tsp_front.api.notice.service.impl;

import com.tsp.new_tsp_front.api.notice.domain.FrontNoticeDTO;
import com.tsp.new_tsp_front.api.notice.domain.FrontNoticeEntity;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class FrontNoticeJpaService {
    private final FrontNoticeJpaRepository frontNoticeJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findNoticeCount
     * 2. ClassName  : FrontNoticeJpaApiService.java
     * 3. Comment    : 프론트 > 공지사항 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    @Transactional(readOnly = true)
    public int findNoticeCount(Map<String, Object> noticeMap) throws TspException {
        try {
            return frontNoticeJpaRepository.findNoticeCount(noticeMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_NOTICE_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findNoticesList
     * 2. ClassName  : FrontNoticeJpaService.java
     * 3. Comment    : 프론트 > 공지사항 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    @Transactional(readOnly = true)
    public List<FrontNoticeDTO> findNoticesList(Map<String, Object> noticeMap) throws TspException {
        try {
            return frontNoticeJpaRepository.findNoticesList(noticeMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_NOTICE_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOneNotice
     * 2. ClassName  : FrontNoticeJpaService.java
     * 3. Comment    : 프론트 > 공지사항 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    @Transactional(readOnly = true)
    public FrontNoticeDTO findOneNotice(FrontNoticeEntity frontNoticeEntity) throws TspException {
        try {
            return this.frontNoticeJpaRepository.findOneNotice(frontNoticeEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_NOTICE, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneNotice
     * 2. ClassName  : FrontNoticeJpaService.java
     * 3. Comment    : 프론트 > 이전 공지사항 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 17.
     * </pre>
     */
    @Transactional(readOnly = true)
    public FrontNoticeDTO findPrevOneNotice(FrontNoticeEntity frontNoticeEntity) throws TspException {
        try {
            return this.frontNoticeJpaRepository.findPrevOneNotice(frontNoticeEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_NOTICE, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneNotice
     * 2. ClassName  : FrontNoticeJpaService.java
     * 3. Comment    : 프론트 > 다음 공지사항 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 17.
     * </pre>
     */
    @Transactional(readOnly = true)
    public FrontNoticeDTO findNextOneNotice(FrontNoticeEntity frontNoticeEntity) throws TspException {
        try {
            return this.frontNoticeJpaRepository.findNextOneNotice(frontNoticeEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_NOTICE, e);
        }
    }
}
