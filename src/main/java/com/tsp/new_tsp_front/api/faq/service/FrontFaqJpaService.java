package com.tsp.new_tsp_front.api.faq.service;

import com.tsp.new_tsp_front.api.faq.domain.FrontFaqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class FrontFaqJpaService {
    private final FrontFaqJpaRepository frontFaqJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findFaqCount
     * 2. ClassName  : FrontFaqJpaService.java
     * 3. Comment    : 프론트 > FAQ 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 23.
     * </pre>
     */
    @Transactional(readOnly = true)
    public int findFaqCount(Map<String, Object> faqMap) {
        return frontFaqJpaRepository.findFaqCount(faqMap);
    }

    /**
     * <pre>
     * 1. MethodName : findFaqList
     * 2. ClassName  : FrontFaqJpaService.java
     * 3. Comment    : 프론트 > FAQ 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 23.
     * </pre>
     */
    @Cacheable(value = "faq", key = "#faqMap")
    @Transactional(readOnly = true)
    public List<FrontFaqDTO> findFaqList(Map<String, Object> faqMap) {
        return frontFaqJpaRepository.findFaqList(faqMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneFaq
     * 2. ClassName  : FrontFaqJpaService.java
     * 3. Comment    : 프론트 > FAQ 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 23.
     * </pre>
     */
    @Cacheable(value = "faq", key = "#idx")
    @Transactional(readOnly = true)
    public FrontFaqDTO findOneFaq(Long idx) {
        return this.frontFaqJpaRepository.findOneFaq(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneFaq
     * 2. ClassName  : FrontFaqJpaService.java
     * 3. Comment    : 프론트 > 이전 FAQ 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 17.
     * </pre>
     */
    @Cacheable(value = "faq", key = "#idx")
    @Transactional(readOnly = true)
    public FrontFaqDTO findPrevOneFaq(Long idx) {
        return this.frontFaqJpaRepository.findPrevOneFaq(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneFaq
     * 2. ClassName  : FrontFaqJpaService.java
     * 3. Comment    : 프론트 > FAQ 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 23.
     * </pre>
     */
    @Cacheable(value = "faq", key = "#idx")
    @Transactional(readOnly = true)
    public FrontFaqDTO findNextOneFaq(Long idx) {
        return this.frontFaqJpaRepository.findNextOneFaq(idx);
    }
}
