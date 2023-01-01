package com.tsp.new_tsp_front.api.notice.service.impl;

import com.tsp.new_tsp_front.api.notice.domain.FrontNoticeDTO;
import com.tsp.new_tsp_front.api.notice.domain.FrontNoticeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


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
    public int findNoticeCount(Map<String, Object> noticeMap) {
        return frontNoticeJpaRepository.findNoticeCount(noticeMap);
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
    @Cacheable(value = "notice", key = "#noticeMap")
    @Transactional(readOnly = true)
    public List<FrontNoticeDTO> findNoticeList(Map<String, Object> noticeMap) {
        return frontNoticeJpaRepository.findNoticeList(noticeMap);
    }

    /**
     * <pre>
     * 1. MethodName : findFixedNoticeCount
     * 2. ClassName  : FrontNoticeJpaApiService.java
     * 3. Comment    : 프론트 > 상단 고정 공지사항 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 24.
     * </pre>
     */
    @Transactional(readOnly = true)
    public int findFixedNoticeCount(Map<String, Object> noticeMap) {
        return frontNoticeJpaRepository.findNoticeCount(noticeMap);
    }

    /**
     * <pre>
     * 1. MethodName : findFixedNoticeList
     * 2. ClassName  : FrontNoticeJpaService.java
     * 3. Comment    : 프론트 > 상단 고정 공지사항 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 24.
     * </pre>
     */
    @Cacheable(value = "notice", key = "#noticeMap.get('topFixed')")
    @Transactional(readOnly = true)
    public List<FrontNoticeDTO> findFixedNoticeList(Map<String, Object> noticeMap) {
        return frontNoticeJpaRepository.findNoticeList(noticeMap);
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
    @Cacheable(value = "notice", key = "#idx")
    @Transactional(readOnly = true)
    public FrontNoticeDTO findOneNotice(Long idx) {
        return this.frontNoticeJpaRepository.findOneNotice(idx);
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
    public FrontNoticeDTO findPrevOneNotice(Long idx) {
        return this.frontNoticeJpaRepository.findPrevOneNotice(idx);
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
    public FrontNoticeDTO findNextOneNotice(Long idx) {
        return this.frontNoticeJpaRepository.findNextOneNotice(idx);
    }
}
