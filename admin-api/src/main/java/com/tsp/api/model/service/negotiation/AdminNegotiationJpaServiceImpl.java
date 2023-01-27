package com.tsp.api.model.service.negotiation;

import com.tsp.api.model.domain.AdminModelEntity;
import com.tsp.api.model.domain.negotiation.AdminNegotiationDTO;
import com.tsp.api.model.domain.negotiation.AdminNegotiationEntity;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

import static com.tsp.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class AdminNegotiationJpaServiceImpl implements AdminNegotiationJpaService {

    private final AdminNegotiationJpaQueryRepository adminNegotiationJpaQueryRepository;
    private final AdminNegotiationJpaRepository adminNegotiationJpaRepository;

    private AdminNegotiationEntity oneNegotiation(Long idx) {
        return adminNegotiationJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_MODEL_NEGOTIATION));
    }

    /**
     * <pre>
     * 1. MethodName : findModelNegotiationList
     * 2. ClassName  : AdminNegotiationJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 섭외 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 09.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdminNegotiationDTO> findNegotiationList(Map<String, Object> negotiationMap, PageRequest pageRequest) {
        return adminNegotiationJpaQueryRepository.findNegotiationList(negotiationMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOneNegotiation
     * 2. ClassName  : AdminNegotiationJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 섭외 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 09.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminNegotiationDTO findOneNegotiation(Long idx) {
        return AdminNegotiationEntity.toDto(oneNegotiation(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneNegotiation
     * 2. ClassName  : AdminNegotiationJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 섭외 이전 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 21.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminNegotiationDTO findPrevOneNegotiation(Long idx) {
        return adminNegotiationJpaQueryRepository.findPrevOneNegotiation(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneNegotiation
     * 2. ClassName  : AdminNegotiationJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 섭외 다음 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 21.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminNegotiationDTO findNextOneNegotiation(Long idx) {
        return adminNegotiationJpaQueryRepository.findNextOneNegotiation(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertModelNegotiation
     * 2. ClassName  : AdminNegotiationJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 섭외 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 09.
     * </pre>
     */
    @Override
    @Transactional
    public AdminNegotiationDTO insertModelNegotiation(AdminModelEntity adminModelEntity, AdminNegotiationEntity adminNegotiationEntity) {
        adminModelEntity.addNegotiation(adminNegotiationEntity);
        return AdminNegotiationEntity.toDto(adminNegotiationJpaRepository.save(adminNegotiationEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateModelNegotiation
     * 2. ClassName  : AdminNegotiationJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 섭외 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 09.
     * </pre>
     */
    @Override
    @Transactional
    public AdminNegotiationDTO updateModelNegotiation(Long idx, AdminNegotiationEntity adminNegotiationEntity) {
        try {
            Optional.ofNullable(oneNegotiation(idx))
                    .ifPresent(adminNegotiation -> adminNegotiation.update(adminNegotiationEntity));
            return AdminNegotiationEntity.toDto(adminNegotiationEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_MODEL_NEGOTIATION);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteModelNegotiation
     * 2. ClassName  : AdminNegotiationJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 섭외 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 09.
     * </pre>
     */
    @Override
    @Transactional
    public Long deleteModelNegotiation(Long idx) {
        try {
            adminNegotiationJpaRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_MODEL_NEGOTIATION);
        }
    }
}
