package com.tsp.new_tsp_front.api.production.service;

import com.tsp.new_tsp_front.api.production.domain.FrontProductionDTO;
import com.tsp.new_tsp_front.api.production.service.impl.FrontProductionJpaRepository;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FrontProductionJpaApiService {
    private final FrontProductionJpaRepository frontProductionJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findProductionCount
     * 2. ClassName  : FrontProductionJpaApiService.java
     * 3. Comment    : 프론트 > 프로덕션 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 06.
     * </pre>
     */
    public int findProductionCount(Map<String, Object> productionMap) {
        return frontProductionJpaRepository.findProductionCount(productionMap);
    }

    /**
     * <pre>
     * 1. MethodName : findProductionList
     * 2. ClassName  : FrontProductionJpaService.java
     * 3. Comment    : 프론트 > 프로덕션 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 06.
     * </pre>
     */
    @Cacheable(value = "production", key = "#productionMap")
    @Transactional(readOnly = true)
    public List<FrontProductionDTO> findProductionList(Map<String, Object> productionMap) {
        return frontProductionJpaRepository.findProductionList(productionMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneProduction
     * 2. ClassName  : FrontProductionJpaService.java
     * 3. Comment    : 프론트 > 프로덕션 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 11.
     * </pre>
     */
    @CachePut(value = "production", key = "#idx")
    @Transactional
    public FrontProductionDTO findOneProduction(Long idx) {
        return frontProductionJpaRepository.findOneProduction(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneProduction
     * 2. ClassName  : FrontProductionJpaServiceImpl.java
     * 3. Comment    : 이전 프로덕션 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    @CachePut(value = "production", key = "#idx")
    @Transactional
    public FrontProductionDTO findPrevOneProduction(Long idx) {
        return frontProductionJpaRepository.findPrevOneProduction(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneProduction
     * 2. ClassName  : FrontProductionJpaServiceImpl.java
     * 3. Comment    : 다음 프로덕션 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 17.
     * </pre>
     */
    @CachePut(value = "production", key = "#idx")
    @Transactional
    public FrontProductionDTO findNextOneProduction(Long idx) {
        return frontProductionJpaRepository.findNextOneProduction(idx);
    }
}
