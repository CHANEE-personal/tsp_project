package com.tsp.new_tsp_front.api.production.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionDTO;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.*;

import static com.tsp.new_tsp_front.api.common.domain.QCommonImageEntity.commonImageEntity;
import static com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity.toDto;
import static com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity.toDtoList;
import static com.tsp.new_tsp_front.api.production.domain.QFrontProductionEntity.frontProductionEntity;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getInt;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getString;
import static com.tsp.new_tsp_front.exception.ApiExceptionType.NOT_FOUND_PRODUCTION;
import static java.util.Collections.emptyList;

@Repository
@RequiredArgsConstructor
public class FrontProductionJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchProduction(Map<String, Object> productionMap) {
        String searchType = getString(productionMap.get("searchType"), "");
        String searchKeyword = getString(productionMap.get("searchKeyword"), "");

        if (!Objects.equals(searchKeyword, "")) {
            return "0".equals(searchType) ?
                    frontProductionEntity.title.contains(searchKeyword)
                            .or(frontProductionEntity.description.contains(searchKeyword)) :
                    "1".equals(searchType) ?
                            frontProductionEntity.title.contains(searchKeyword) :
                            frontProductionEntity.description.contains(searchKeyword);
        } else {
            return null;
        }
    }

    /**
     * <pre>
     * 1. MethodName : findProductionCount
     * 2. ClassName  : FrontModelJpaRepository.java
     * 3. Comment    : 프로덕션 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 06.
     * </pre>
     */
    public int findProductionCount(Map<String, Object> productionMap) {
        return queryFactory
                .selectFrom(frontProductionEntity)
                .where(searchProduction(productionMap))
                .where(frontProductionEntity.visible.eq("Y"))
                .fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findProductionList
     * 2. ClassName  : FrontProductionJpaRepository.java
     * 3. Comment    : 프로덕션 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 06.
     * </pre>
     */
    public List<FrontProductionDTO> findProductionList(Map<String, Object> productionMap) {
        List<FrontProductionEntity> productionList = queryFactory
                .selectFrom(frontProductionEntity)
                .where(searchProduction(productionMap))
                .where(frontProductionEntity.visible.eq("Y"))
                .orderBy(frontProductionEntity.idx.desc())
                .offset(getInt(productionMap.get("jpaStartPage"), 0))
                .limit(getInt(productionMap.get("size"), 0))
                .fetch();

        return productionList != null ? toDtoList(productionList) : emptyList();
    }

    /**
     * <pre>
     * 1. MethodName : findOneProduction
     * 2. ClassName  : FrontProductionJpaRepository.java
     * 3. Comment    : 프로덕션 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 12.
     * </pre>
     */
    public FrontProductionDTO findOneProduction(Long idx) {
        // 프로덕션 조회 수 증가
        updateProductionViewCount(idx);

        //프로덕션 상세 조회
        FrontProductionEntity findOneProduction = Optional.ofNullable(queryFactory
                .selectFrom(frontProductionEntity)
                .leftJoin(frontProductionEntity.commonImageEntityList, commonImageEntity)
                .fetchJoin()
                .where(frontProductionEntity.idx.eq(idx)
                        .and(frontProductionEntity.visible.eq("Y"))
                        .and(commonImageEntity.typeName.eq("production")))
                .fetchOne()).orElseThrow(() -> new TspException(NOT_FOUND_PRODUCTION));

        return toDto(findOneProduction);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneProduction
     * 2. ClassName  : FrontProductionJpaRepository.java
     * 3. Comment    : 이전 프로덕션 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    public FrontProductionDTO findPrevOneProduction(Long idx) {
        // 프로덕션 조회 수 증가
        updateProductionViewCount(idx);

        // 이전 프로덕션 조회
        FrontProductionEntity findPrevOneProduction = Optional.ofNullable(queryFactory
                .selectFrom(frontProductionEntity)
                .orderBy(frontProductionEntity.idx.desc())
                .where(frontProductionEntity.idx.lt(idx)
                        .and(frontProductionEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_PRODUCTION));

        return toDto(findPrevOneProduction);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneProduction
     * 2. ClassName  : FrontProductionJpaRepository.java
     * 3. Comment    : 다음 프로덕션 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    public FrontProductionDTO findNextOneProduction(Long idx) {
        // 프로덕션 조회 수 증가
        updateProductionViewCount(idx);

        // 다음 프로덕션 조회
        FrontProductionEntity findNextOneProduction = Optional.ofNullable(queryFactory
                .selectFrom(frontProductionEntity)
                .orderBy(frontProductionEntity.idx.asc())
                .where(frontProductionEntity.idx.gt(idx)
                        .and(frontProductionEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_PRODUCTION));

        return toDto(findNextOneProduction);
    }

    /**
     * <pre>
     * 1. MethodName : updateProductionViewCount
     * 2. ClassName  : FrontModelJpaRepository.java
     * 3. Comment    : 프론트 모델 조회 수 증가
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 09.
     * </pre>
     */
    public int updateProductionViewCount(Long idx) {
        // 모델 조회 수 증가
        queryFactory
                .update(frontProductionEntity)
                //add , minus , multiple 다 가능하다.
                .set(frontProductionEntity.viewCount, frontProductionEntity.viewCount.add(1))
                .where(frontProductionEntity.idx.eq(idx))
                .execute();

        em.flush();
        em.clear();

        return em.find(FrontProductionEntity.class, idx).getViewCount();
    }
}
