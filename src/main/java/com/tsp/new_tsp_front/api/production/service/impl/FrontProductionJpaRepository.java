package com.tsp.new_tsp_front.api.production.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionDTO;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.api.common.domain.QCommonImageEntity.commonImageEntity;
import static com.tsp.new_tsp_front.api.production.domain.QFrontProductionEntity.frontProductionEntity;
import static com.tsp.new_tsp_front.api.production.service.impl.ProductionMapper.INSTANCE;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getInt;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getString;
import static java.util.Objects.requireNonNull;

@Repository
@RequiredArgsConstructor
public class FrontProductionJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchProduction(Map<String, Object> productionMap) {
        String searchType = getString(productionMap.get("searchType"), "");
        String searchKeyword = getString(productionMap.get("searchKeyword"), "");

        if ("0".equals(searchType)) {
            return frontProductionEntity.title.contains(searchKeyword).or(frontProductionEntity.description.contains(searchKeyword));
        } else if ("1".equals(searchType)) {
            return frontProductionEntity.title.contains(searchKeyword);
        } else {
            return frontProductionEntity.description.contains(searchKeyword);
        }
    }

    /**
     * <pre>
     * 1. MethodName : getProductionCount
     * 2. ClassName  : FrontModelJpaRepository.java
     * 3. Comment    : 프로덕션 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 06.
     * </pre>
     */
    public int getProductionCount(Map<String, Object> productionMap) {
        return queryFactory
                .selectFrom(frontProductionEntity)
                .where(searchProduction(productionMap)
                        .and(frontProductionEntity.visible.eq("Y")))
                .fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : getProductionList
     * 2. ClassName  : FrontProductionJpaRepository.java
     * 3. Comment    : 프로덕션 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 06.
     * </pre>
     */
    public List<FrontProductionDTO> getProductionList(Map<String, Object> productionMap) {
        List<FrontProductionEntity> productionList = queryFactory
                .selectFrom(frontProductionEntity)
                .where(searchProduction(productionMap).and(frontProductionEntity.visible.eq("Y")))
                .orderBy(frontProductionEntity.idx.desc())
                .offset(getInt(productionMap.get("jpaStartPage"), 0))
                .limit(getInt(productionMap.get("size"), 0))
                .fetch();

        productionList.forEach(list -> productionList.get(productionList.indexOf(list))
                .setRnum(getInt(productionMap.get("startPage"), 1) * (getInt(productionMap.get("size"), 1)) - (2 - productionList.indexOf(list))));

        return INSTANCE.toDtoList(productionList);
    }

    /**
     * <pre>
     * 1. MethodName : getProductionInfo
     * 2. ClassName  : FrontProductionJpaRepository.java
     * 3. Comment    : 프로덕션 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 12.
     * </pre>
     */
    public FrontProductionDTO getProductionInfo(FrontProductionEntity existFrontProductionEntity) {
        // 프로덕션 조회 수 증가
        updateProductionViewCount(existFrontProductionEntity);

        //프로덕션 상세 조회
        FrontProductionEntity getProductionInfo = queryFactory
                .selectFrom(frontProductionEntity)
                .leftJoin(frontProductionEntity.commonImageEntityList, commonImageEntity)
                .fetchJoin()
                .where(frontProductionEntity.idx.eq(existFrontProductionEntity.getIdx())
                        .and(frontProductionEntity.visible.eq("Y"))
                        .and(commonImageEntity.typeName.eq("production")))
                .fetchOne();

        return INSTANCE.toDto(getProductionInfo);
    }

    /**
     * <pre>
     * 1. MethodName : viewProductionCount
     * 2. ClassName  : FrontProductionJpaRepository.java
     * 3. Comment    : 프론트 프로덕션 조회 수
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 12.
     * </pre>
     */
    public Integer viewProductionCount(Integer idx) {
        return requireNonNull(queryFactory
                .selectFrom(frontProductionEntity)
                .where(frontProductionEntity.idx.eq(idx)).fetchOne()).getViewCount();
    }

    /**
     * <pre>
     * 1. MethodName : updateProductionViewCount
     * 2. ClassName  : FrontModelJpaRepository.java
     * 3. Comment    : 프론트 모델 조회 수 증가
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 09.
     * </pre>
     */
    public Integer updateProductionViewCount(FrontProductionEntity existFrontProductionEntity) {
        // 모델 조회 수 증가
        queryFactory
                .update(frontProductionEntity)
                //add , minus , multiple 다 가능하다.
                .set(frontProductionEntity.viewCount, frontProductionEntity.viewCount.add(1))
                .where(frontProductionEntity.idx.eq(existFrontProductionEntity.getIdx()))
                .execute();

        em.flush();
        em.clear();

        return viewProductionCount(existFrontProductionEntity.getIdx());
    }
}
