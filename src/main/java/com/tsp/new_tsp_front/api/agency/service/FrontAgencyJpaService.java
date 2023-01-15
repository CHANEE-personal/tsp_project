package com.tsp.new_tsp_front.api.agency.service;

import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyDTO;
import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyEntity;
import com.tsp.new_tsp_front.api.agency.service.impl.FrontAgencyJpaQueryRepository;
import com.tsp.new_tsp_front.api.agency.service.impl.FrontAgencyJpaRepository;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.tsp.new_tsp_front.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class FrontAgencyJpaService {
    private final FrontAgencyJpaQueryRepository frontAgencyJpaQueryRepository;
    private final FrontAgencyJpaRepository frontAgencyJpaRepository;

    private FrontAgencyEntity oneAgency(Long idx) {
        return frontAgencyJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_AGENCY));
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
    @Transactional(readOnly = true)
    public Page<FrontAgencyDTO> findAgencyList(Map<String, Object> agencyMap, PageRequest pageRequest) {
        return frontAgencyJpaQueryRepository.findAgencyList(agencyMap, pageRequest);
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
    @Transactional(readOnly = true)
    public FrontAgencyDTO findOneAgency(Long idx) {
        return FrontAgencyEntity.toDto(oneAgency(idx));
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
    @Transactional(readOnly = true)
    public FrontAgencyDTO findPrevOneAgency(Long idx) {
        return this.frontAgencyJpaQueryRepository.findPrevOneAgency(idx);
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
    @Transactional(readOnly = true)
    public FrontAgencyDTO findNextOneAgency(Long idx) {
        try {
            return this.frontAgencyJpaQueryRepository.findNextOneAgency(idx);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_AGENCY);
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
    @Transactional
    public int favoriteAgency(Long idx) {
        try {
            return frontAgencyJpaQueryRepository.favoriteAgency(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_AGENCY_LIKE);
        }
    }
}
