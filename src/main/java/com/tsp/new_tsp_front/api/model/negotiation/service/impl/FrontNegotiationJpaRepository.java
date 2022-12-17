package com.tsp.new_tsp_front.api.model.negotiation.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.api.model.domain.negotiation.FrontNegotiationDTO;
import com.tsp.new_tsp_front.api.model.domain.negotiation.FrontNegotiationEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.api.model.domain.QFrontModelEntity.frontModelEntity;
import static com.tsp.new_tsp_front.api.model.domain.negotiation.QFrontNegotiationEntity.frontNegotiationEntity;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getInt;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getString;
import static java.time.LocalDate.now;
import static java.time.LocalDateTime.of;

@Slf4j
@RequiredArgsConstructor
@Repository
public class FrontNegotiationJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchNegotiation(Map<String, Object> negotiationMap) {
        String searchKeyword = getString(negotiationMap.get("searchKeyword"), "");
        LocalDateTime searchStartTime = (LocalDateTime) negotiationMap.get("searchStartTime");
        LocalDateTime searchEndTime = (LocalDateTime) negotiationMap.get("searchEndTime");

        if (searchStartTime != null && searchEndTime != null) {
            searchStartTime = (LocalDateTime) negotiationMap.get("searchStartTime");
            searchEndTime = (LocalDateTime) negotiationMap.get("searchEndTime");
        } else {
            searchStartTime = now().minusDays(now().getDayOfMonth()-1).atStartOfDay();
            searchEndTime = of(now().minusDays(now().getDayOfMonth()).plusMonths(1), LocalTime.of(23,59,59));
        }

        if (!"".equals(searchKeyword)) {
            return frontModelEntity.modelKorName.contains(searchKeyword)
                    .or(frontModelEntity.modelEngName.contains(searchKeyword)
                            .or(frontModelEntity.modelDescription.contains(searchKeyword)))
                    .or(frontNegotiationEntity.modelNegotiationDesc.contains(searchKeyword));
        } else {
            return frontNegotiationEntity.modelNegotiationDate.between(searchStartTime, searchEndTime);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findModelNegotiationCount
     * 2. ClassName  : FrontNegotiationJpaRepository.java
     * 3. Comment    : 모델 섭외 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 11.
     * </pre>
     */
    public Integer findNegotiationCount(Map<String, Object> negotiationMap) {
        return queryFactory.selectFrom(frontNegotiationEntity)
                .where(searchNegotiation(negotiationMap))
                .fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findModelNegotiationList
     * 2. ClassName  : FrontNegotiationJpaRepository.java
     * 3. Comment    : 모델 섭외 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 11.
     * </pre>
     */
    public List<FrontModelDTO> findModelNegotiationList(Map<String, Object> negotiationMap) {
        List<FrontModelEntity> modelNegotiationList = queryFactory
                .selectFrom(frontModelEntity)
                .orderBy(frontModelEntity.idx.desc())
                .leftJoin(frontModelEntity.modelNegotiationList, frontNegotiationEntity)
                .fetchJoin()
                .where(searchNegotiation(negotiationMap)
                        .and(frontModelEntity.visible.eq("Y"))
                        .and(frontNegotiationEntity.visible.eq("Y")))
                .offset(getInt(negotiationMap.get("jpaStartPage"), 0))
                .limit(getInt(negotiationMap.get("size"), 0))
                .fetch();

        modelNegotiationList.forEach(list -> modelNegotiationList.get(modelNegotiationList.indexOf(list))
                .setRowNum(getInt(negotiationMap.get("startPage"), 1) * (getInt(negotiationMap.get("size"), 1)) - (2 - modelNegotiationList.indexOf(list))));

        return FrontModelEntity.toDtoList(modelNegotiationList);
    }

    /**
     * <pre>
     * 1. MethodName : findOneNegotiation
     * 2. ClassName  : FrontNegotiationJpaRepository.java
     * 3. Comment    : 모델 섭외 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 11.
     * </pre>
     */
    public FrontNegotiationDTO findOneNegotiation(FrontNegotiationEntity existFrontNegotiationEntity) {
        FrontNegotiationEntity findOneNegotiation = queryFactory
                .selectFrom(frontNegotiationEntity)
                .orderBy(frontNegotiationEntity.idx.desc())
                .where(frontNegotiationEntity.visible.eq("Y")
                        .and(frontNegotiationEntity.idx.eq(existFrontNegotiationEntity.getIdx())))
                .fetchOne();

        return FrontNegotiationEntity.toDto(findOneNegotiation);
    }

    /**
     * <pre>
     * 1. MethodName : findOneModelNegotiation
     * 2. ClassName  : FrontNegotiationJpaRepository.java
     * 3. Comment    : 모델 섭외 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 11.
     * </pre>
     */
    public FrontModelDTO findOneModelNegotiation(FrontNegotiationEntity existFrontNegotiationEntity) {
        FrontModelEntity findOneModelNegotiation = queryFactory
                .selectFrom(frontModelEntity)
                .leftJoin(frontModelEntity.modelNegotiationList, frontNegotiationEntity)
                .fetchJoin()
                .where(frontModelEntity.visible.eq("Y")
                        .and(frontNegotiationEntity.visible.eq("Y"))
                        .and(frontModelEntity.idx.eq(existFrontNegotiationEntity.getModelIdx()))
                        .and(frontNegotiationEntity.idx.eq(existFrontNegotiationEntity.getIdx())))
                .fetchOne();

        assert findOneModelNegotiation != null;
        return FrontModelEntity.toDto(findOneModelNegotiation);
    }

    /**
     * <pre>
     * 1. MethodName : insertModelNegotiation
     * 2. ClassName  : FrontNegotiationJpaRepository.java
     * 3. Comment    : 모델 섭외 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 11.
     * </pre>
     */
    public FrontNegotiationDTO insertModelNegotiation(FrontNegotiationEntity frontNegotiationEntity) {
        em.persist(frontNegotiationEntity);
        return FrontNegotiationEntity.toDto(frontNegotiationEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateModelNegotiation
     * 2. ClassName  : FrontNegotiationJpaRepository.java
     * 3. Comment    : 모델 섭외 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 11.
     * </pre>
     */
    public FrontNegotiationDTO updateModelNegotiation(FrontNegotiationEntity existFrontNegotiationEntity) {
        em.merge(existFrontNegotiationEntity);
        em.flush();
        em.clear();
        return FrontNegotiationEntity.toDto(existFrontNegotiationEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteModelNegotiation
     * 2. ClassName  : FrontNegotiationJpaRepository.java
     * 3. Comment    : 모델 섭외 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 11.
     * </pre>
     */
    public Long deleteModelNegotiation(Long idx) {
        em.remove(em.find(FrontNegotiationEntity.class, idx));
        em.flush();
        em.clear();
        return idx;
    }
}
