package com.tsp.new_tsp_front.api.faq.service;

import com.tsp.new_tsp_front.api.faq.domain.FrontFaqDTO;
import com.tsp.new_tsp_front.api.faq.domain.FrontFaqEntity;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.exception.ApiExceptionType.NOT_FOUND_FAQ;


@Service
@RequiredArgsConstructor
public class FrontFaqJpaService {
    private final FrontFaqJpaQueryRepository frontFaqJpaQueryRepository;
    private final FrontFaqJpaRepository frontFaqJpaRepository;

    private FrontFaqEntity oneFaq(Long idx) {
        return frontFaqJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_FAQ));
    }

    /**
     * <pre>
     * 1. MethodName : findFaqList
     * 2. ClassName  : FrontFaqJpaService.java
     * 3. Comment    : 프론트 > FAQ 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 23.
     * </pre>
     */
    @Transactional(readOnly = true)
    public Page<FrontFaqDTO> findFaqList(Map<String, Object> faqMap, PageRequest pageRequest) {
        return frontFaqJpaQueryRepository.findFaqList(faqMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOneFaq
     * 2. ClassName  : FrontFaqJpaService.java
     * 3. Comment    : 프론트 > FAQ 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 23.
     * </pre>
     */
    @Transactional(readOnly = true)
    public FrontFaqDTO findOneFaq(Long idx) {
        return FrontFaqEntity.toDto(oneFaq(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneFaq
     * 2. ClassName  : FrontFaqJpaService.java
     * 3. Comment    : 프론트 > 이전 FAQ 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    @Transactional(readOnly = true)
    public FrontFaqDTO findPrevOneFaq(Long idx) {
        return this.frontFaqJpaQueryRepository.findPrevOneFaq(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneFaq
     * 2. ClassName  : FrontFaqJpaService.java
     * 3. Comment    : 프론트 > FAQ 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 23.
     * </pre>
     */
    @Transactional(readOnly = true)
    public FrontFaqDTO findNextOneFaq(Long idx) {
        return this.frontFaqJpaQueryRepository.findNextOneFaq(idx);
    }
}
