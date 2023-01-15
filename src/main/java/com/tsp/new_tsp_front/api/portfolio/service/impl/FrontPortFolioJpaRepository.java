package com.tsp.new_tsp_front.api.portfolio.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioDTO;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioEntity;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.*;

import static com.tsp.new_tsp_front.api.common.domain.QCommonImageEntity.commonImageEntity;
import static com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioEntity.toDto;
import static com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioEntity.toDtoList;
import static com.tsp.new_tsp_front.api.portfolio.domain.QFrontPortFolioEntity.frontPortFolioEntity;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getInt;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getString;
import static com.tsp.new_tsp_front.exception.ApiExceptionType.NOT_FOUND_PORTFOLIO;
import static java.util.Collections.emptyList;

@Repository
@RequiredArgsConstructor
public class FrontPortFolioJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchPortFolio(Map<String, Object> portfolioMap) {
        String searchType = getString(portfolioMap.get("searchType"), "");
        String searchKeyword = getString(portfolioMap.get("searchKeyword"), "");

        if (!Objects.equals(searchKeyword, "")) {
            return "0".equals(searchType) ?
                    frontPortFolioEntity.title.contains(searchKeyword)
                            .or(frontPortFolioEntity.description.contains(searchKeyword)) :
                    "1".equals(searchType) ?
                            frontPortFolioEntity.title.contains(searchKeyword) :
                            frontPortFolioEntity.description.contains(searchKeyword);
        } else {
            return null;
        }
    }

    /**
     * <pre>
     * 1. MethodName : findPortfolioCount
     * 2. ClassName  : FrontPortFolioJpaRepository.java
     * 3. Comment    : 포트폴리오 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 11.
     * </pre>
     */
    public int findPortfolioCount(Map<String, Object> portfolioMap) {
        return queryFactory
                .selectFrom(frontPortFolioEntity)
                .where(searchPortFolio(portfolioMap))
                .where(frontPortFolioEntity.visible.eq("Y"))
                .fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findPortfolioList
     * 2. ClassName  : FrontPortFolioList.java
     * 3. Comment    : 포트폴리오 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 11.
     * </pre>
     */
    public List<FrontPortFolioDTO> findPortfolioList(Map<String, Object> portFolioMap) {
        List<FrontPortFolioEntity> portFolioList = queryFactory
                .selectFrom(frontPortFolioEntity)
                .where(searchPortFolio(portFolioMap))
                .where(frontPortFolioEntity.visible.eq("Y"))
                .orderBy(frontPortFolioEntity.idx.desc())
                .offset(getInt(portFolioMap.get("jpaStartPage"), 0))
                .limit(getInt(portFolioMap.get("size"), 0))
                .fetch();

        return portFolioList != null ? toDtoList(portFolioList) : emptyList();
    }

    /**
     * <pre>
     * 1. MethodName : findOnePortfolio
     * 2. ClassName  : FrontPortFolioList.java
     * 3. Comment    : 포트폴리오 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 12.
     * </pre>
     */
    public FrontPortFolioDTO findOnePortfolio(Long idx) {
        // 포트폴리오 조회 수 증가
        updatePortfolioViewCount(idx);

        FrontPortFolioEntity getPortFolioInfo = Optional.ofNullable(queryFactory
                .selectFrom(frontPortFolioEntity)
                .leftJoin(frontPortFolioEntity.commonImageEntityList, commonImageEntity)
                .fetchJoin()
                .where(frontPortFolioEntity.idx.eq(idx)
                        .and(frontPortFolioEntity.visible.eq("Y"))
                        .and(commonImageEntity.typeName.eq("portfolio")))
                .fetchOne()).orElseThrow(() -> new TspException(NOT_FOUND_PORTFOLIO));

        return toDto(getPortFolioInfo);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOnePortfolio
     * 2. ClassName  : FrontPortfolioJpaRepository.java
     * 3. Comment    : 이전 포트폴리오 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 17.
     * </pre>
     */
    public FrontPortFolioDTO findPrevOnePortfolio(Long idx) {
        // 포트폴리오 조회 수 증가
        updatePortfolioViewCount(idx);

        // 이전 포트폴리오 조회
        FrontPortFolioEntity findPrevOnePortfolio = Optional.ofNullable(queryFactory
                .selectFrom(frontPortFolioEntity)
                .orderBy(frontPortFolioEntity.idx.desc())
                .where(frontPortFolioEntity.idx.lt(idx)
                        .and(frontPortFolioEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_PORTFOLIO));

        return toDto(findPrevOnePortfolio);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOnePortfolio
     * 2. ClassName  : FrontPortfolioJpaRepository.java
     * 3. Comment    : 다음 포트폴리오 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 17.
     * </pre>
     */
    public FrontPortFolioDTO findNextOnePortfolio(Long idx) {
        // 포트폴리오 조회 수 증가
        updatePortfolioViewCount(idx);

        // 다음 포트폴리오 조회
        FrontPortFolioEntity findNextOnePortfolio = Optional.ofNullable(queryFactory
                .selectFrom(frontPortFolioEntity)
                .orderBy(frontPortFolioEntity.idx.desc())
                .where(frontPortFolioEntity.idx.gt(idx)
                        .and(frontPortFolioEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_PORTFOLIO));

        return toDto(findNextOnePortfolio);
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
    public int updatePortfolioViewCount(Long idx) {
        // 모델 조회 수 증가
        queryFactory
                .update(frontPortFolioEntity)
                //add , minus , multiple 다 가능하다.
                .set(frontPortFolioEntity.viewCount, frontPortFolioEntity.viewCount.add(1))
                .where(frontPortFolioEntity.idx.eq(idx))
                .execute();

        em.flush();
        em.clear();

        return em.find(FrontPortFolioEntity.class, idx).getViewCount();
    }
}
