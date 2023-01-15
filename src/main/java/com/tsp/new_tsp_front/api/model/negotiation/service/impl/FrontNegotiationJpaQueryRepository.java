package com.tsp.new_tsp_front.api.model.negotiation.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.api.model.domain.negotiation.FrontNegotiationDTO;
import com.tsp.new_tsp_front.api.model.domain.negotiation.FrontNegotiationEntity;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static com.tsp.new_tsp_front.api.model.domain.QFrontModelEntity.frontModelEntity;
import static com.tsp.new_tsp_front.api.model.domain.negotiation.FrontNegotiationEntity.toDto;
import static com.tsp.new_tsp_front.api.model.domain.negotiation.FrontNegotiationEntity.toDtoList;
import static com.tsp.new_tsp_front.api.model.domain.negotiation.QFrontNegotiationEntity.frontNegotiationEntity;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getInt;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getString;
import static com.tsp.new_tsp_front.exception.ApiExceptionType.NOT_FOUND_MODEL_NEGOTIATION;
import static java.time.LocalDate.now;
import static java.time.LocalDateTime.of;
import static java.util.Collections.emptyList;

@Slf4j
@RequiredArgsConstructor
@Repository
public class FrontNegotiationJpaQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchNegotiation(Map<String, Object> negotiationMap) {
        String searchKeyword = getString(negotiationMap.get("searchKeyword"), "");
        LocalDateTime searchStartTime = negotiationMap.get("searchStartTime") != null ? (LocalDateTime) negotiationMap.get("searchStartTime") : now().minusDays(now().getDayOfMonth() - 1).atStartOfDay();
        LocalDateTime searchEndTime = negotiationMap.get("searchEndTime") != null ? (LocalDateTime) negotiationMap.get("searchStartTime") : of(now().minusDays(now().getDayOfMonth()).plusMonths(1), LocalTime.of(23, 59, 59));

        return !Objects.equals(searchKeyword, "") ?
                frontNegotiationEntity.modelNegotiationDesc.contains(searchKeyword) :
                frontNegotiationEntity.modelNegotiationDate.between(searchStartTime, searchEndTime);
    }

    /**
     * <pre>
     * 1. MethodName : findModelNegotiationList
     * 2. ClassName  : FrontNegotiationJpaRepository.java
     * 3. Comment    : 모델 섭외 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 11.
     * </pre>
     */
    public Page<FrontNegotiationDTO> findModelNegotiationList(Map<String, Object> negotiationMap, PageRequest pageRequest) {
        List<FrontNegotiationEntity> modelNegotiationList = queryFactory
                .selectFrom(frontNegotiationEntity)
                .orderBy(frontNegotiationEntity.idx.desc())
                .where(searchNegotiation(negotiationMap)
                        .and(frontNegotiationEntity.visible.eq("Y")))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(toDtoList(modelNegotiationList), pageRequest, modelNegotiationList.size());
    }

    /**
     * <pre>
     * 1. MethodName : findOneModelNegotiation
     * 2. ClassName  : FrontNegotiationJpaRepository.java
     * 3. Comment    : 모델 섭외 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 11.
     * </pre>
     */
    public FrontModelDTO findOneModelNegotiation(FrontNegotiationEntity existFrontNegotiationEntity) {
        FrontModelEntity findOneModelNegotiation = Optional.ofNullable(queryFactory
                .selectFrom(frontModelEntity)
                .leftJoin(frontModelEntity.modelNegotiationList, frontNegotiationEntity)
                .fetchJoin()
                .where(frontModelEntity.visible.eq("Y")
                        .and(frontNegotiationEntity.visible.eq("Y"))
                        .and(frontModelEntity.idx.eq(existFrontNegotiationEntity.getFrontModelEntity().getIdx()))
                        .and(frontNegotiationEntity.idx.eq(existFrontNegotiationEntity.getIdx())))
                .fetchOne()).orElseThrow(() -> new TspException(NOT_FOUND_MODEL_NEGOTIATION));

        return FrontModelEntity.toDto(findOneModelNegotiation);
    }
}
