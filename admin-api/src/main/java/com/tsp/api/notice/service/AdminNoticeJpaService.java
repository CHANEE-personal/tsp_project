package com.tsp.api.notice.service;

import com.tsp.api.domain.notice.AdminNoticeDTO;
import com.tsp.api.domain.notice.AdminNoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

public interface AdminNoticeJpaService {

    /**
     * <pre>
     * 1. MethodName : findNoticeList
     * 2. ClassName  : AdminNoticeJpaService.java
     * 3. Comment    : 관리자 공지사항 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    Page<AdminNoticeDTO> findNoticeList(Map<String, Object> noticeMap, PageRequest pageRequest);

    /**
     * <pre>
     * 1. MethodName : findOneNotice
     * 2. ClassName  : AdminNoticeJpaService.java
     * 3. Comment    : 관리자 공지사항 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    AdminNoticeDTO findOneNotice(Long idx);

    /**
     * <pre>
     * 1. MethodName : findPrevOneNotice
     * 2. ClassName  : AdminNoticeJpaService.java
     * 3. Comment    : 관리자 이전 공지사항 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 18.
     * </pre>
     */
    AdminNoticeDTO findPrevOneNotice(Long idx);

    /**
     * <pre>
     * 1. MethodName : findNextOneNotice
     * 2. ClassName  : AdminNoticeJpaService.java
     * 3. Comment    : 관리자 다음 공지사항 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 18.
     * </pre>
     */
    AdminNoticeDTO findNextOneNotice(Long idx);

    /**
     * <pre>
     * 1. MethodName : insertNotice
     * 2. ClassName  : AdminNoticeJpaService.java
     * 3. Comment    : 관리자 공지사항 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    AdminNoticeDTO insertNotice(AdminNoticeEntity adminNoticeEntity);

    /**
     * <pre>
     * 1. MethodName : updateNotice
     * 2. ClassName  : AdminNoticeJpaService.java
     * 3. Comment    : 관리자 공지사항 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    AdminNoticeDTO updateNotice(Long idx, AdminNoticeEntity adminNoticeEntity);

    /**
     * <pre>
     * 1. MethodName : toggleFixed
     * 2. ClassName  : AdminNoticeJpaService.java
     * 3. Comment    : 관리자 공지사항 상단 고정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 23.
     * </pre>
     */
    Boolean toggleFixed(Long idx);

    /**
     * <pre>
     * 1. MethodName : deleteNotice
     * 2. ClassName  : AdminNoticeJpaService.java
     * 3. Comment    : 관리자 공지사항 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    Long deleteNotice(Long idx);
}
