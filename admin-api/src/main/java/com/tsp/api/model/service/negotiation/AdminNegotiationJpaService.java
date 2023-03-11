package com.tsp.api.model.service.negotiation;

import com.tsp.api.model.domain.negotiation.AdminNegotiationDto;
import com.tsp.api.model.domain.negotiation.AdminNegotiationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

public interface AdminNegotiationJpaService {

    /**
     * <pre>
     * 1. MethodName : findNegotiationList
     * 2. ClassName  : AdminNegotiationJpaService.java
     * 3. Comment    : 관리자 모델 섭외 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 09.
     * </pre>
     */
    Page<AdminNegotiationDto> findNegotiationList(Map<String, Object> negotiationMap, PageRequest pageRequest);

    /**
     * <pre>
     * 1. MethodName : findOneNegotiation
     * 2. ClassName  : AdminNegotiationJpaService.java
     * 3. Comment    : 관리자 모델 섭외 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 09.
     * </pre>
     */
    AdminNegotiationDto findOneNegotiation(Long idx);

    /**
     * <pre>
     * 1. MethodName : findPrevOneNegotiation
     * 2. ClassName  : AdminNegotiationJpaService.java
     * 3. Comment    : 관리자 모델 섭외 이전 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 21.
     * </pre>
     */
    AdminNegotiationDto findPrevOneNegotiation(Long idx);

    /**
     * <pre>
     * 1. MethodName : findNextOneNegotiation
     * 2. ClassName  : AdminNegotiationJpaService.java
     * 3. Comment    : 관리자 모델 섭외 다음 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 21.
     * </pre>
     */
    AdminNegotiationDto findNextOneNegotiation(Long idx);

    /**
     * <pre>
     * 1. MethodName : insertModelNegotiation
     * 2. ClassName  : AdminNegotiationJpaService.java
     * 3. Comment    : 관리자 모델 섭외 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 09.
     * </pre>
     */
    AdminNegotiationDto insertModelNegotiation(Long modelIdx, AdminNegotiationEntity adminNegotiationEntity);

    /**
     * <pre>
     * 1. MethodName : updateModelNegotiation
     * 2. ClassName  : AdminNegotiationJpaService.java
     * 3. Comment    : 관리자 모델 섭외 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 09.
     * </pre>
     */
    AdminNegotiationDto updateModelNegotiation(Long idx, AdminNegotiationEntity adminNegotiationEntity);

    /**
     * <pre>
     * 1. MethodName : deleteModelNegotiation
     * 2. ClassName  : AdminNegotiationJpaService.java
     * 3. Comment    : 관리자 모델 섭외 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 09.
     * </pre>
     */
    Long deleteModelNegotiation(Long idx);
}
