package com.tsp.api.notice.service;

import com.tsp.api.notice.domain.FrontNoticeDTO;
import com.tsp.api.notice.domain.FrontNoticeEntity;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.tsp.exception.ApiExceptionType.NOT_FOUND_NOTICE;

@Service
@RequiredArgsConstructor
public class FrontNoticeJpaService {
    private final FrontNoticeJpaQueryRepository frontNoticeJpaQueryRepository;
    private final FrontNoticeJpaRepository frontNoticeJpaRepository;

    private FrontNoticeEntity oneNotice(Long idx) {
        return frontNoticeJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_NOTICE));
    }

    /**
     * <pre>
     * 1. MethodName : findNoticeList
     * 2. ClassName  : FrontNoticeJpaService.java
     * 3. Comment    : 프론트 > 공지사항 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    @Transactional(readOnly = true)
    public Page<FrontNoticeDTO> findNoticeList(Map<String, Object> noticeMap, PageRequest pageRequest) {
        return frontNoticeJpaQueryRepository.findNoticeList(noticeMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOneNotice
     * 2. ClassName  : FrontNoticeJpaService.java
     * 3. Comment    : 프론트 > 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    @Transactional(readOnly = true)
    public FrontNoticeDTO findOneNotice(Long idx) {
        return FrontNoticeEntity.toDto(oneNotice(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneNotice
     * 2. ClassName  : FrontNoticeJpaService.java
     * 3. Comment    : 프론트 > 이전 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    @Transactional(readOnly = true)
    public FrontNoticeDTO findPrevOneNotice(Long idx) {
        return this.frontNoticeJpaQueryRepository.findPrevOneNotice(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneNotice
     * 2. ClassName  : FrontNoticeJpaService.java
     * 3. Comment    : 프론트 > 다음 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    @Transactional(readOnly = true)
    public FrontNoticeDTO findNextOneNotice(Long idx) {
        return this.frontNoticeJpaQueryRepository.findNextOneNotice(idx);
    }
}
