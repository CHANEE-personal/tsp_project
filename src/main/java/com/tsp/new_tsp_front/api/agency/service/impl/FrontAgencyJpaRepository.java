package com.tsp.new_tsp_front.api.agency.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyDTO;
import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.api.agency.domain.QFrontAgencyEntity.frontAgencyEntity;
import static com.tsp.new_tsp_front.api.agency.service.impl.AgencyMapper.INSTANCE;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getInt;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getString;
import static java.util.Objects.requireNonNull;

@Slf4j
@RequiredArgsConstructor
@Repository
public class FrontAgencyJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchAgency(Map<String, Object> faqMap) {
        String searchType = getString(faqMap.get("searchType"), "");
        String searchKeyword = getString(faqMap.get("searchKeyword"), "");

        if ("0".equals(searchType)) {
            return frontAgencyEntity.agencyName.contains(searchKeyword)
                    .or(frontAgencyEntity.agencyDescription.contains(searchKeyword));
        } else if ("1".equals(searchType)) {
            return frontAgencyEntity.agencyName.contains(searchKeyword);
        } else {
            return frontAgencyEntity.agencyDescription.contains(searchKeyword);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findAgencyCount
     * 2. ClassName  : FrontAgencyJpaRepository.java
     * 3. Comment    : 관리자 Agency 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 24.
     * </pre>
     */
    public Integer findAgencyCount(Map<String, Object> faqMap) {
        return queryFactory.selectFrom(frontAgencyEntity).where(searchAgency(faqMap)).fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findAgencyList
     * 2. ClassName  : FrontAgencyJpaRepository.java
     * 3. Comment    : 관리자 Agency 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 24.
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

        agencyList.forEach(list -> agencyList.get(agencyList.indexOf(list))
                .setRnum(getInt(agencyMap.get("startPage"), 1) * (getInt(agencyMap.get("size"), 1)) - (2 - agencyList.indexOf(list))));

        return INSTANCE.toDtoList(agencyList);
    }

    /**
     * <pre>
     * 1. MethodName : findOneAgency
     * 2. ClassName  : FrontAgencyJpaRepository.java
     * 3. Comment    : 관리자 Agency 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 24.
     * </pre>
     */
    public FrontAgencyDTO findOneAgency(FrontAgencyEntity existFrontAgencyEntity) {
        FrontAgencyEntity findOneAgency = queryFactory
                .selectFrom(frontAgencyEntity)
                .orderBy(frontAgencyEntity.idx.desc())
                .where(frontAgencyEntity.idx.eq(existFrontAgencyEntity.getIdx())
                        .and(frontAgencyEntity.visible.eq("Y")))
                .fetchOne();

        return INSTANCE.toDto(findOneAgency);
    }

    /**
     * <pre>
     * 1. MethodName : favoriteAgencyCount
     * 2. ClassName  : FrontAgencyJpaRepository.java
     * 3. Comment    : 프론트 Agency 좋아요 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 24.
     * </pre>
     */
    public Integer favoriteAgencyCount(Integer idx) {
        return requireNonNull(queryFactory
                .selectFrom(frontAgencyEntity)
                .where(frontAgencyEntity.idx.eq(idx)).fetchOne()).getFavoriteCount();
    }

    /**
     * <pre>
     * 1. MethodName : favoriteAgency
     * 2. ClassName  : FrontAgencyJpaRepository.java
     * 3. Comment    : 프론트 Agency 좋아요
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 24.
     * </pre>
     */
    public Integer favoriteAgency(FrontAgencyEntity existFrontAgencyEntity) {
        queryFactory
                .update(frontAgencyEntity)
                //add , minus , multiple 다 가능하다.
                .set(frontAgencyEntity.favoriteCount, frontAgencyEntity.favoriteCount.add(1))
                .where(frontAgencyEntity.idx.eq(existFrontAgencyEntity.getIdx()))
                .execute();

        em.flush();
        em.clear();

        return favoriteAgencyCount(existFrontAgencyEntity.getIdx());
    }
}
