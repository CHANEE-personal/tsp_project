package com.tsp.new_tsp_front.api.portfolio.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioDTO;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.api.common.domain.QCommonImageEntity.commonImageEntity;
import static com.tsp.new_tsp_front.api.portfolio.domain.QFrontPortFolioEntity.frontPortFolioEntity;
import static com.tsp.new_tsp_front.api.portfolio.service.impl.PortFolioMapper.INSTANCE;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getInt;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getString;
import static java.util.Objects.requireNonNull;

@Repository
@RequiredArgsConstructor
public class FrontPortFolioJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchPortFolio(Map<String, Object> portfolioMap) {
        String searchType = getString(portfolioMap.get("searchType"), "");
        String searchKeyword = getString(portfolioMap.get("searchKeyword"), "");

        if ("0".equals(searchType)) {
            return frontPortFolioEntity.title.contains(searchKeyword)
                    .or(frontPortFolioEntity.description.contains(searchKeyword));
        } else if ("1".equals(searchType)) {
            return frontPortFolioEntity.title.contains(searchKeyword);
        } else {
            return frontPortFolioEntity.description.contains(searchKeyword);
        }
    }

    /**
     * <pre>
     * 1. MethodName : getPortFolioList
     * 2. ClassName  : FrontPortFolioList.java
     * 3. Comment    : 포트폴리오 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 11.
     * </pre>
     */
    public List<FrontPortFolioDTO> getPortFolioList(Map<String, Object> portFolioMap) {
        List<FrontPortFolioEntity> portFolioList = queryFactory
                .selectFrom(frontPortFolioEntity)
                .where(searchPortFolio(portFolioMap))
                .orderBy(frontPortFolioEntity.idx.desc())
                .offset(getInt(portFolioMap.get("jpaStartPage"), 0))
                .limit(getInt(portFolioMap.get("size"), 0))
                .fetch();

        portFolioList.forEach(list -> portFolioList.get(portFolioList.indexOf(list))
                .setRnum(getInt(portFolioMap.get("startPage"), 1) * (getInt(portFolioMap.get("size"), 1)) - (2 - portFolioList.indexOf(list))));

        return INSTANCE.toDtoList(portFolioList);
    }

    /**
     * <pre>
     * 1. MethodName : getPortFolioInfo
     * 2. ClassName  : FrontPortFolioList.java
     * 3. Comment    : 포트폴리오 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 12.
     * </pre>
     */
    public FrontPortFolioDTO getPortFolioInfo(FrontPortFolioEntity existFrontPortFolioEntity) {
        FrontPortFolioEntity getPortFolioInfo = queryFactory
                .selectFrom(frontPortFolioEntity)
                .leftJoin(frontPortFolioEntity.commonImageEntityList, commonImageEntity)
                .fetchJoin()
                .where(frontPortFolioEntity.idx.eq(existFrontPortFolioEntity.getIdx())
                        .and(frontPortFolioEntity.visible.eq("Y"))
                        .and(commonImageEntity.typeName.eq("portfolio")))
                .fetchOne();

        return INSTANCE.toDto(getPortFolioInfo);
    }

    /**
     * <pre>
     * 1. MethodName : viewPortfolioCount
     * 2. ClassName  : FrontPortFolioJpaRepository.java
     * 3. Comment    : 프론트 포트폴리오 조회 수
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 12.
     * </pre>
     */
    public Integer viewPortfolioCount(Integer idx) {
        return requireNonNull(queryFactory
                .selectFrom(frontPortFolioEntity)
                .where(frontPortFolioEntity.idx.eq(idx)).fetchOne()).getViewCount();
    }

    /**
     * <pre>
     * 1. MethodName : updatePortfolioViewCount
     * 2. ClassName  : FrontPortFolioJpaRepository.java
     * 3. Comment    : 프론트 포트폴리오 조회 수 증가
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 12.
     * </pre>
     */
    public Integer updatePortfolioViewCount(FrontPortFolioEntity existFrontPortFolioEntity) {
        // 모델 조회 수 증가
        queryFactory
                .update(frontPortFolioEntity)
                //add , minus , multiple 다 가능하다.
                .set(frontPortFolioEntity.viewCount, frontPortFolioEntity.viewCount.add(1))
                .where(frontPortFolioEntity.idx.eq(existFrontPortFolioEntity.getIdx()))
                .execute();

        em.flush();
        em.clear();

        return viewPortfolioCount(existFrontPortFolioEntity.getIdx());
    }
}
