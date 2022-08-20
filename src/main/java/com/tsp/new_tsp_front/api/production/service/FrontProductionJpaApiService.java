package com.tsp.new_tsp_front.api.production.service;

import com.tsp.new_tsp_front.api.production.domain.FrontProductionDTO;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity;
import com.tsp.new_tsp_front.api.production.service.impl.FrontProductionJpaRepository;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class FrontProductionJpaApiService {
    private final FrontProductionJpaRepository frontProductionJpaRepository;

    /**
     * <pre>
     * 1. MethodName : getProductionCount
     * 2. ClassName  : FrontProductionJpaApiService.java
     * 3. Comment    : 프론트 > 프로덕션 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 06.
     * </pre>
     */
    public int getProductionCount(Map<String, Object> productionMap) throws TspException {
        try {
            return frontProductionJpaRepository.getProductionCount(productionMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_PRODUCTION_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : getProductionList
     * 2. ClassName  : FrontProductionJpaService.java
     * 3. Comment    : 프론트 > 프로덕션 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 06.
     * </pre>
     */
    @Transactional(readOnly = true)
    public List<FrontProductionDTO> getProductionList(Map<String, Object> productionMap) throws TspException {
        try {
            return frontProductionJpaRepository.getProductionList(productionMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_PRODUCTION_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : getProductionInfo
     * 2. ClassName  : FrontProductionJpaService.java
     * 3. Comment    : 프론트 > 프로덕션 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 11.
     * </pre>
     */
    @Transactional
    public FrontProductionDTO getProductionInfo(FrontProductionEntity frontProductionEntity) throws TspException {
        try {
            return frontProductionJpaRepository.getProductionInfo(frontProductionEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_PRODUCTION, e);
        }
    }
}
