package com.tsp.api.model.service.negotiation;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.api.domain.model.negotiation.AdminNegotiationDTO;
import com.tsp.api.domain.model.negotiation.AdminNegotiationEntity;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.tsp.api.domain.model.negotiation.AdminNegotiationEntity.toDto;
import static com.tsp.api.domain.model.negotiation.AdminNegotiationEntity.toDtoList;
import static com.tsp.api.domain.model.negotiation.QAdminNegotiationEntity.adminNegotiationEntity;
import static com.tsp.common.StringUtil.getString;
import static com.tsp.exception.ApiExceptionType.NOT_FOUND_MODEL_NEGOTIATION;
import static java.time.LocalDate.now;
import static java.time.LocalDateTime.of;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminNegotiationJpaQueryRepository {
    private final JPAQueryFactory queryFactory;

    private BooleanExpression searchNegotiation(Map<String, Object> negotiationMap) {
        String searchKeyword = getString(negotiationMap.get("searchKeyword"), "");
        LocalDateTime searchStartTime = negotiationMap.get("searchStartTime") != null ? (LocalDateTime) negotiationMap.get("searchStartTime") : now().minusDays(now().getDayOfMonth() - 1).atStartOfDay();
        LocalDateTime searchEndTime = negotiationMap.get("searchEndTime") != null ? (LocalDateTime) negotiationMap.get("searchStartTime") : of(now().minusDays(now().getDayOfMonth()).plusMonths(1), LocalTime.of(23, 59, 59));

        return !Objects.equals(searchKeyword, "") ?
                    adminNegotiationEntity.modelNegotiationDesc.contains(searchKeyword) :
                adminNegotiationEntity.modelNegotiationDate.between(searchStartTime, searchEndTime);
    }

    /**
     * <pre>
     * 1. MethodName : findNegotiationList
     * 2. ClassName  : AdminNegotiationJpaRepository.java
     * 3. Comment    : 관리자 모델 섭외 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 09.
     * </pre>
     */
    public Page<AdminNegotiationDTO> findNegotiationList(Map<String, Object> negotiationMap, PageRequest pageRequest) {
        List<AdminNegotiationEntity> negotiationList = queryFactory
                .selectFrom(adminNegotiationEntity)
                .orderBy(adminNegotiationEntity.idx.desc())
                .where(searchNegotiation(negotiationMap)
                        .and(adminNegotiationEntity.visible.eq("Y")))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(toDtoList(negotiationList), pageRequest, negotiationList.size());
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneNegotiation
     * 2. ClassName  : AdminNegotiationJpaRepository.java
     * 3. Comment    : 관리자 이전 모델 섭외 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 21.
     * </pre>
     */
    public AdminNegotiationDTO findPrevOneNegotiation(Long idx) {
        // 이전 모델 섭외 조회
        AdminNegotiationEntity findPrevOneNegotiation = Optional.ofNullable(queryFactory
                .selectFrom(adminNegotiationEntity)
                .orderBy(adminNegotiationEntity.idx.desc())
                .where(adminNegotiationEntity.idx.lt(idx)
                        .and(adminNegotiationEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_MODEL_NEGOTIATION));

        return toDto(findPrevOneNegotiation);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneNegotiation
     * 2. ClassName  : AdminNegotiationJpaRepository.java
     * 3. Comment    : 관리자 다음 모델 섭외 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 21.
     * </pre>
     */
    public AdminNegotiationDTO findNextOneNegotiation(Long idx) {
        // 다음 모델 섭외 조회
        AdminNegotiationEntity findNextOneNegotiation = Optional.ofNullable(queryFactory
                .selectFrom(adminNegotiationEntity)
                .orderBy(adminNegotiationEntity.idx.asc())
                .where(adminNegotiationEntity.idx.gt(idx)
                        .and(adminNegotiationEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_MODEL_NEGOTIATION));

        return toDto(findNextOneNegotiation);
    }
}
