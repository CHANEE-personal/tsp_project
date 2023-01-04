package com.tsp.new_tsp_front.api.agency.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyDTO;
import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyEntity;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.*;

import static com.tsp.new_tsp_front.api.agency.domain.FrontAgencyEntity.toDto;
import static com.tsp.new_tsp_front.api.agency.domain.FrontAgencyEntity.toDtoList;
import static com.tsp.new_tsp_front.api.agency.domain.QFrontAgencyEntity.frontAgencyEntity;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getInt;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getString;
import static com.tsp.new_tsp_front.exception.ApiExceptionType.NOT_FOUND_AGENCY;
import static java.util.Collections.emptyList;

@Slf4j
@RequiredArgsConstructor
@Repository
public class FrontAgencyJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchAgency(Map<String, Object> faqMap) {
        String searchType = getString(faqMap.get("searchType"), "");
        String searchKeyword = getString(faqMap.get("searchKeyword"), "");

        if (!Objects.equals(searchKeyword, "")) {
            return "0".equals(searchType) ?
                    frontAgencyEntity.agencyName.contains(searchKeyword)
                            .or(frontAgencyEntity.agencyDescription.contains(searchKeyword)) :
                    "1".equals(searchType) ?
                            frontAgencyEntity.agencyName.contains(searchKeyword) :
                            frontAgencyEntity.agencyDescription.contains(searchKeyword);
        } else {
            return null;
        }
    }

    /**
     * <pre>
     * 1. MethodName : findAgencyCount
     * 2. ClassName  : FrontAgencyJpaRepository.java
     * 3. Comment    : Agency 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    public int findAgencyCount(Map<String, Object> faqMap) {
        return queryFactory.selectFrom(frontAgencyEntity).where(searchAgency(faqMap)).fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findAgencyList
     * 2. ClassName  : FrontAgencyJpaRepository.java
     * 3. Comment    : Agency 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    public List<FrontAgencyDTO> findAgencyList(Map<String, Object> agencyMap) {
        List<FrontAgencyEntity> agencyList = queryFactory
                .selectFrom(frontAgencyEntity)
                .orderBy(frontAgencyEntity.idx.desc())
                .where(searchAgency(agencyMap))
                .offset(getInt(agencyMap.get("jpaStartPage"), 0))
                .limit(getInt(agencyMap.get("size"), 0))
                .fetch();

        return agencyList != null ? toDtoList(agencyList) : emptyList();
    }

    /**
     * <pre>
     * 1. MethodName : findOneAgency
     * 2. ClassName  : FrontAgencyJpaRepository.java
     * 3. Comment    : Agency 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    public FrontAgencyDTO findOneAgency(Long idx) {
        FrontAgencyEntity oneAgency = Optional.ofNullable(queryFactory
                .selectFrom(frontAgencyEntity)
                .where(frontAgencyEntity.idx.eq(idx))
                .fetchOne()).orElseThrow(() -> new TspException(NOT_FOUND_AGENCY, new Throwable()));

        return toDto(oneAgency);
    }

    /**
     * <pre>
     * 1. MethodName : favoriteAgency
     * 2. ClassName  : FrontAgencyJpaRepository.java
     * 3. Comment    : Agency 좋아요
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    public int favoriteAgency(Long idx) {
        queryFactory
                .update(frontAgencyEntity)
                //add , minus , multiple 다 가능하다.
                .set(frontAgencyEntity.favoriteCount, frontAgencyEntity.favoriteCount.add(1))
                .where(frontAgencyEntity.idx.eq(idx))
                .execute();

        em.flush();
        em.clear();

        return em.find(FrontAgencyEntity.class, idx).getFavoriteCount();
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneAgency
     * 2. ClassName  : FrontAgencyJpaRepository.java
     * 3. Comment    : 이전 소속사 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 17.
     * </pre>
     */
    public FrontAgencyDTO findPrevOneAgency(Long idx) {
        // 이전 소속사 조회
        FrontAgencyEntity findPrevOneAgency = Optional.ofNullable(queryFactory
                .selectFrom(frontAgencyEntity)
                .orderBy(frontAgencyEntity.idx.desc())
                .where(frontAgencyEntity.idx.lt(idx)
                        .and(frontAgencyEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_AGENCY, new Throwable()));

        return toDto(findPrevOneAgency);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneAgency
     * 2. ClassName  : FrontAgencyJpaRepository.java
     * 3. Comment    : 다음 소속사 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 17.
     * </pre>
     */
    public FrontAgencyDTO findNextOneAgency(Long idx) {
        // 다음 소속사 조회
        FrontAgencyEntity findNextOneAgency = Optional.ofNullable(queryFactory
                .selectFrom(frontAgencyEntity)
                .orderBy(frontAgencyEntity.idx.desc())
                .where(frontAgencyEntity.idx.gt(idx)
                        .and(frontAgencyEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_AGENCY, new Throwable()));

        return toDto(findNextOneAgency);
    }
}
