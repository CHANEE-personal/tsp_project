package com.tsp.api.portfolio.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.api.common.EntityType;
import com.tsp.api.common.domain.CommonImageEntity;
import com.tsp.api.portfolio.domain.AdminPortFolioDto;
import com.tsp.api.portfolio.domain.AdminPortFolioEntity;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.tsp.api.common.domain.QCommonImageEntity.commonImageEntity;
import static com.tsp.api.portfolio.domain.AdminPortFolioEntity.toDto;
import static com.tsp.api.portfolio.domain.QAdminPortFolioEntity.adminPortFolioEntity;
import static com.tsp.api.production.domain.QAdminProductionEntity.adminProductionEntity;
import static com.tsp.common.StringUtil.getString;
import static com.tsp.exception.ApiExceptionType.NOT_FOUND_PORTFOLIO;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminPortfolioJpaQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchPortfolio(Map<String, Object> portfolioMap) {
        String searchType = getString(portfolioMap.get("searchType"), "");
        String searchKeyword = getString(portfolioMap.get("searchKeyword"), "");

        if (!Objects.equals(searchKeyword, "")) {
            return "0".equals(searchType) ?
                    adminPortFolioEntity.title.contains(searchKeyword)
                            .or(adminPortFolioEntity.description.contains(searchKeyword)) :
                    "1".equals(searchType) ?
                            adminPortFolioEntity.title.contains(searchKeyword) :
                            adminPortFolioEntity.description.contains(searchKeyword);
        } else {
            return null;
        }
    }

    /**
     * <pre>
     * 1. MethodName : findPortfolioList
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 포트폴리오 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 13.
     * </pre>
     */
    public Page<AdminPortFolioDto> findPortfolioList(Map<String, Object> portfolioMap, PageRequest pageRequest) {
        List<AdminPortFolioEntity> portfolioList = queryFactory
                .selectFrom(adminPortFolioEntity)
                .orderBy(adminPortFolioEntity.idx.desc())
                .where(searchPortfolio(portfolioMap))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(AdminPortFolioEntity.toDtoList(portfolioList), pageRequest, portfolioList.size());
    }

    /**
     * <pre>
     * 1. MethodName : findOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 포트폴리오 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 13.
     * </pre>
     */
    public AdminPortFolioDto findOnePortfolio(Long idx) {
        // 포트폴리오 상세 조회
        AdminPortFolioEntity findOnePortfolio = Optional.ofNullable(queryFactory
                .selectFrom(adminPortFolioEntity)
                .leftJoin(adminPortFolioEntity.commonImageEntityList, commonImageEntity)
                .fetchJoin()
                .where(adminPortFolioEntity.idx.eq(idx)
                        .and(adminPortFolioEntity.visible.eq("Y")
                                .and(commonImageEntity.typeName.eq(EntityType.PORTFOLIO))))
                .fetchOne()).orElseThrow(() -> new TspException(NOT_FOUND_PORTFOLIO));

        return toDto(findOnePortfolio);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 이전 포트폴리오 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 14.
     * </pre>
     */
    public AdminPortFolioDto findPrevOnePortfolio(Long idx) {
        // 이전 포트폴리오 조회
        AdminPortFolioEntity findPrevOnePortfolio = Optional.ofNullable(queryFactory
                .selectFrom(adminPortFolioEntity)
                .orderBy(adminPortFolioEntity.idx.desc())
                .where(adminPortFolioEntity.idx.lt(idx)
                        .and(adminProductionEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_PORTFOLIO));

        return toDto(findPrevOnePortfolio);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 다음 포트폴리오 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 14.
     * </pre>
     */
    public AdminPortFolioDto findNextOnePortfolio(Long idx) {
        // 다음 포트폴리오 조회
        AdminPortFolioEntity findPrevOnePortfolio = Optional.ofNullable(queryFactory
                .selectFrom(adminPortFolioEntity)
                .orderBy(adminPortFolioEntity.idx.desc())
                .where(adminPortFolioEntity.idx.gt(idx)
                        .and(adminProductionEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_PORTFOLIO));

        return toDto(findPrevOnePortfolio);
    }

    /**
     * <pre>
     * 1. MethodName : insertPortfolioImage
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 포트폴리오 이미지 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 14.
     * </pre>
     */
    public Long insertPortfolioImage(CommonImageEntity commonImageEntity) {
        em.persist(commonImageEntity);
        return commonImageEntity.getIdx();
    }
}
