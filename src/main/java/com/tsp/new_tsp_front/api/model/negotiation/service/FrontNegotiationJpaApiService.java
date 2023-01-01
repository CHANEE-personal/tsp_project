package com.tsp.new_tsp_front.api.model.negotiation.service;

import com.tsp.new_tsp_front.api.model.domain.negotiation.FrontNegotiationDTO;
import com.tsp.new_tsp_front.api.model.domain.negotiation.FrontNegotiationEntity;
import com.tsp.new_tsp_front.api.model.negotiation.service.impl.FrontNegotiationJpaRepository;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class FrontNegotiationJpaApiService {
    private final FrontNegotiationJpaRepository frontNegotiationJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findNegotiationCount
     * 2. ClassName  : FrontNegotiationJpaServiceImpl.java
     * 3. Comment    : 모델 섭외 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 11.
     * </pre>
     */
    @Transactional(readOnly = true)
    public int findNegotiationCount(Map<String, Object> negotiationMap) {
        return frontNegotiationJpaRepository.findNegotiationCount(negotiationMap);
    }

    /**
     * <pre>
     * 1. MethodName : findModelNegotiationList
     * 2. ClassName  : FrontNegotiationJpaServiceImpl.java
     * 3. Comment    : 모델 섭외 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 11.
     * </pre>
     */
    @Cacheable(value = "negotiation", key = "#negotiationMap")
    @Transactional(readOnly = true)
    public List<FrontNegotiationDTO> findModelNegotiationList(Map<String, Object> negotiationMap) {
        return frontNegotiationJpaRepository.findModelNegotiationList(negotiationMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneNegotiation
     * 2. ClassName  : FrontNegotiationJpaServiceImpl.java
     * 3. Comment    : 모델 섭외 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 11.
     * </pre>
     */
    @Cacheable(value = "negotiation", key = "#idx")
    @Transactional(readOnly = true)
    public FrontNegotiationDTO findOneNegotiation(Long idx) {
        return frontNegotiationJpaRepository.findOneNegotiation(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertModelNegotiation
     * 2. ClassName  : FrontNegotiationJpaServiceImpl.java
     * 3. Comment    : 모델 섭외 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 11.
     * </pre>
     */
    @CachePut("negotiation")
    @Modifying(clearAutomatically = true)
    @Transactional
    public FrontNegotiationDTO insertModelNegotiation(FrontNegotiationEntity frontNegotiationEntity) {
        try {
            return frontNegotiationJpaRepository.insertModelNegotiation(frontNegotiationEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_MODEL_NEGOTIATION, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateModelNegotiation
     * 2. ClassName  : FrontNegotiationJpaServiceImpl.java
     * 3. Comment    : 모델 섭외 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 11.
     * </pre>
     */
    @CachePut(value = "negotiation", key = "#frontNegotiationEntity.idx")
    @Modifying(clearAutomatically = true)
    @Transactional
    public FrontNegotiationDTO updateModelNegotiation(FrontNegotiationEntity frontNegotiationEntity) {
        try {
            return frontNegotiationJpaRepository.updateModelNegotiation(frontNegotiationEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_MODEL_NEGOTIATION, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteModelNegotiation
     * 2. ClassName  : FrontNegotiationJpaServiceImpl.java
     * 3. Comment    : 모델 섭외 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 11.
     * </pre>
     */
    @CacheEvict(value = "negotiation", key = "#idx")
    @Modifying(clearAutomatically = true)
    @Transactional
    public Long deleteModelNegotiation(Long idx) {
        try {
            return frontNegotiationJpaRepository.deleteModelNegotiation(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_MODEL_NEGOTIATION, e);
        }
    }
}
