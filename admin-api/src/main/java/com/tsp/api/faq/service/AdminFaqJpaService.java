package com.tsp.api.faq.service;

import com.tsp.api.faq.domain.AdminFaqDto;
import com.tsp.api.faq.domain.AdminFaqEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

public interface AdminFaqJpaService {

    /**
     * <pre>
     * 1. MethodName : findFaqList
     * 2. ClassName  : AdminFaqJpaService.java
     * 3. Comment    : 관리자 FAQ 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 22.
     * </pre>
     */
    Page<AdminFaqDto> findFaqList(Map<String, Object> faqMap, PageRequest pageRequest);

    /**
     * <pre>
     * 1. MethodName : findOneFaq
     * 2. ClassName  : AdminFaqJpaService.java
     * 3. Comment    : 관리자 FAQ 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 22.
     * </pre>
     */
    AdminFaqDto findOneFaq(Long idx);

    /**
     * <pre>
     * 1. MethodName : findPrevOneFaq
     * 2. ClassName  : AdminFaqJpaService.java
     * 3. Comment    : 관리자 이전 FAQ 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 18.
     * </pre>
     */
    AdminFaqDto findPrevOneFaq(Long idx);

    /**
     * <pre>
     * 1. MethodName : findNextOneFaq
     * 2. ClassName  : AdminFaqJpaService.java
     * 3. Comment    : 관리자 다음 FAQ 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 18.
     * </pre>
     */
    AdminFaqDto findNextOneFaq(Long idx);

    /**
     * <pre>
     * 1. MethodName : insertFaq
     * 2. ClassName  : AdminFaqJpaService.java
     * 3. Comment    : 관리자 FAQ 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 22.
     * </pre>
     */
    AdminFaqDto insertFaq(AdminFaqEntity adminFaqEntity);

    /**
     * <pre>
     * 1. MethodName : updateFaq
     * 2. ClassName  : AdminFaqJpaService.java
     * 3. Comment    : 관리자 FAQ 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 22.
     * </pre>
     */
    AdminFaqDto updateFaq(Long idx, AdminFaqEntity adminFaqEntity);

    /**
     * <pre>
     * 1. MethodName : deleteFaq
     * 2. ClassName  : AdminFaqJpaService.java
     * 3. Comment    : 관리자 FAQ 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 22.
     * </pre>
     */
    Long deleteFaq(Long idx);
}
