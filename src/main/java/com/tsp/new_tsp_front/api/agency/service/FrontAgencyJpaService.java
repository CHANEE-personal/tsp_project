package com.tsp.new_tsp_front.api.agency.service;

import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyDTO;
import com.tsp.new_tsp_front.api.agency.service.impl.FrontAgencyJpaRepository;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class FrontAgencyJpaService {
    private final FrontAgencyJpaRepository frontAgencyJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findAgencyCount
     * 2. ClassName  : FrontAgencyJpaService.java
     * 3. Comment    : 프론트 > Agency 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @Transactional(readOnly = true)
    public int findAgencyCount(Map<String, Object> agencyMap) {
        return frontAgencyJpaRepository.findAgencyCount(agencyMap);
    }

    /**
     * <pre>
     * 1. MethodName : findAgencyList
     * 2. ClassName  : FrontAgencyJpaService.java
     * 3. Comment    : 프론트 > Agency 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @Cacheable(value = "agency", key = "#agencyMap")
    @Transactional(readOnly = true)
    public List<FrontAgencyDTO> findAgencyList(Map<String, Object> agencyMap) {
        return frontAgencyJpaRepository.findAgencyList(agencyMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneAgency
     * 2. ClassName  : FrontAgencyJpaService.java
     * 3. Comment    : 프론트 > Agency 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @Cacheable(value = "agency", key = "#idx")
    @Transactional(readOnly = true)
    public FrontAgencyDTO findOneAgency(Long idx) {
        return this.frontAgencyJpaRepository.findOneAgency(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneAgency
     * 2. ClassName  : FrontAgencyJpaService.java
     * 3. Comment    : 프론트 > 이전 Agency 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    @Cacheable(value = "agency", key = "#idx")
    @Transactional(readOnly = true)
    public FrontAgencyDTO findPrevOneAgency(Long idx) {
        return this.frontAgencyJpaRepository.findPrevOneAgency(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneAgency
     * 2. ClassName  : FrontAgencyJpaService.java
     * 3. Comment    : 프론트 > 다음 Agency 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    @Cacheable(value = "agency", key = "#idx")
    @Transactional(readOnly = true)
    public FrontAgencyDTO findNextOneAgency(Long idx) {
        try {
            return this.frontAgencyJpaRepository.findNextOneAgency(idx);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_AGENCY, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : favoriteAgency
     * 2. ClassName  : FrontAgencyJpaService.java
     * 3. Comment    : 프론트 > Agency 좋아요
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @CachePut(value = "agency", key = "#idx")
    @Transactional
    public int favoriteAgency(Long idx) {
        try {
            return frontAgencyJpaRepository.favoriteAgency(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_AGENCY_LIKE, e);
        }
    }
}
