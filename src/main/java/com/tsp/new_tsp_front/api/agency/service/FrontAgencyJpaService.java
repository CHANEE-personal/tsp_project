package com.tsp.new_tsp_front.api.agency.service;

import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyDTO;
import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyEntity;
import com.tsp.new_tsp_front.api.agency.service.impl.FrontAgencyJpaRepository;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
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
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 24.
     * </pre>
     */
    @Transactional(readOnly = true)
    public int findAgencyCount(Map<String, Object> agencyMap) throws TspException {
        try {
            return frontAgencyJpaRepository.findAgencyCount(agencyMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_AGENCY_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findAgencyList
     * 2. ClassName  : FrontAgencyJpaService.java
     * 3. Comment    : 프론트 > Agency 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 24.
     * </pre>
     */
    @Transactional(readOnly = true)
    public List<FrontAgencyDTO> findAgencyList(Map<String, Object> agencyMap) throws TspException {
        try {
            return frontAgencyJpaRepository.findAgencyList(agencyMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_AGENCY_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOneAgency
     * 2. ClassName  : FrontAgencyJpaService.java
     * 3. Comment    : 프론트 > Agency 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 24.
     * </pre>
     */
    @Transactional(readOnly = true)
    public FrontAgencyDTO findOneAgency(FrontAgencyEntity frontAgencyEntity) throws TspException {
        try {
            return this.frontAgencyJpaRepository.findOneAgency(frontAgencyEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_AGENCY, e);
        }
    }
}
