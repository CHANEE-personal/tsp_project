package com.tsp.new_tsp_front.api.faq.service;

import com.tsp.new_tsp_front.api.faq.domain.FrontFaqDTO;
import com.tsp.new_tsp_front.api.faq.domain.FrontFaqEntity;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.exception.ApiExceptionType.*;

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
    public int findFaqCount(Map<String, Object> faqMap) throws TspException {
        try {
            return frontFaqJpaRepository.findFaqCount(faqMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_FAQ_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findFaqsList
     * 2. ClassName  : FrontFaqJpaService.java
     * 3. Comment    : 프론트 > FAQ 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 23.
     * </pre>
     */
    @Transactional(readOnly = true)
    public List<FrontFaqDTO> findFaqsList(Map<String, Object> faqMap) throws TspException {
        try {
            return frontFaqJpaRepository.findFaqsList(faqMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_FAQ_LIST, e);
        }
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
    @Transactional(readOnly = true)
    public FrontFaqDTO findOneFaq(FrontFaqEntity frontFaqEntity) throws TspException {
        try {
            return this.frontFaqJpaRepository.findOneFaq(frontFaqEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_FAQ, e);
        }
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
    @Transactional(readOnly = true)
    public FrontFaqDTO findPrevOneFaq(FrontFaqEntity frontFaqEntity) throws TspException {
        try {
            return this.frontFaqJpaRepository.findPrevOneFaq(frontFaqEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_FAQ, e);
        }
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
    @Transactional(readOnly = true)
    public FrontFaqDTO findNextOneFaq(FrontFaqEntity frontFaqEntity) throws TspException {
        try {
            return this.frontFaqJpaRepository.findNextOneFaq(frontFaqEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_FAQ, e);
        }
    }
}
