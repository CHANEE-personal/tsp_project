package com.tsp.api.model.negotiation.service;

import com.tsp.api.model.domain.FrontModelEntity;
import com.tsp.api.model.domain.negotiation.FrontNegotiationDTO;
import com.tsp.api.model.domain.negotiation.FrontNegotiationEntity;
import com.tsp.api.model.service.FrontModelJpaRepository;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.tsp.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class FrontNegotiationJpaApiService {
    private final FrontNegotiationJpaQueryRepository frontNegotiationJpaQueryRepository;
    private final FrontNegotiationJpaRepository frontNegotiationJpaRepository;
    private final FrontModelJpaRepository frontModelJpaRepository;

    private FrontModelEntity oneModel(Long idx) {
        return frontModelJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_MODEL));
    }

    private FrontNegotiationEntity oneNegotiation(Long idx) {
        return frontNegotiationJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_MODEL_NEGOTIATION));
    }

    /**
     * <pre>
     * 1. MethodName : findModelNegotiationList
     * 2. ClassName  : FrontNegotiationJpaServiceImpl.java
     * 3. Comment    : 모델 섭외 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 11.
     * </pre>
     */
    @Transactional(readOnly = true)
    public Page<FrontNegotiationDTO> findModelNegotiationList(Map<String, Object> negotiationMap, PageRequest pageRequest) {
        return frontNegotiationJpaQueryRepository.findModelNegotiationList(negotiationMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOneNegotiation
     * 2. ClassName  : FrontNegotiationJpaServiceImpl.java
     * 3. Comment    : 모델 섭외 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 11.
     * </pre>
     */
    @Transactional(readOnly = true)
    public FrontNegotiationDTO findOneNegotiation(Long idx) {
        return FrontNegotiationEntity.toDto(oneNegotiation(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertModelNegotiation
     * 2. ClassName  : FrontNegotiationJpaServiceImpl.java
     * 3. Comment    : 모델 섭외 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 11.
     * </pre>
     */
    @Transactional
    public FrontNegotiationDTO insertModelNegotiation(Long modelIdx, FrontNegotiationEntity frontNegotiationEntity) {
        try {
            oneModel(modelIdx).addNegotiation(frontNegotiationEntity);
            return FrontNegotiationEntity.toDto(frontNegotiationJpaRepository.save(frontNegotiationEntity));
        } catch (Exception e) {
            throw new TspException(ERROR_MODEL_NEGOTIATION);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateModelNegotiation
     * 2. ClassName  : FrontNegotiationJpaServiceImpl.java
     * 3. Comment    : 모델 섭외 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 11.
     * </pre>
     */
    @Transactional
    public FrontNegotiationDTO updateModelNegotiation(Long idx, FrontNegotiationEntity frontNegotiationEntity) {
        try {
            oneNegotiation(idx).update(frontNegotiationEntity);
            return FrontNegotiationEntity.toDto(frontNegotiationJpaRepository.save(frontNegotiationEntity));
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_MODEL_NEGOTIATION);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteModelNegotiation
     * 2. ClassName  : FrontNegotiationJpaServiceImpl.java
     * 3. Comment    : 모델 섭외 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 11.
     * </pre>
     */
    @Transactional
    public Long deleteModelNegotiation(Long idx) {
        try {
            frontNegotiationJpaRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_MODEL_NEGOTIATION);
        }
    }
}
