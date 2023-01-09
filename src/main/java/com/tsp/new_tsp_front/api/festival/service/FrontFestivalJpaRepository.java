package com.tsp.new_tsp_front.api.festival.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_front.api.festival.domain.FrontFestivalDTO;
import com.tsp.new_tsp_front.api.festival.domain.FrontFestivalEntity;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.querydsl.core.types.Projections.fields;
import static com.tsp.new_tsp_front.api.festival.domain.QFrontFestivalEntity.frontFestivalEntity;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getString;
import static com.tsp.new_tsp_front.exception.ApiExceptionType.NOT_FOUND_FESTIVAL;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FrontFestivalJpaRepository {

    private final JPAQueryFactory queryFactory;

    private BooleanExpression searchFestival(Map<String, Object> festivalMap) {
        String searchType = getString(festivalMap.get("searchType"), "");
        String searchKeyword = getString(festivalMap.get("searchKeyword"), "");

        if (!Objects.equals(searchKeyword, "")) {
            return "0".equals(searchType) ?
                    frontFestivalEntity.festivalTitle.contains(searchKeyword)
                            .or(frontFestivalEntity.festivalDescription.contains(searchKeyword)) :
                    "1".equals(searchType) ?
                            frontFestivalEntity.festivalTitle.contains(searchKeyword) :
                            frontFestivalEntity.festivalDescription.contains(searchKeyword);
        } else {
            return null;
        }
    }

    /**
     * <pre>
     * 1. MethodName : findFestivalCount
     * 2. ClassName  : FrontFestivalJpaRepository.java
     * 3. Comment    : 관리자 행사 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    public int findFestivalCount(Map<String, Object> festivalMap) {
        return queryFactory.selectFrom(frontFestivalEntity)
                .where(searchFestival(festivalMap)).fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findFestivalList
     * 2. ClassName  : FrontFestivalJpaRepository.java
     * 3. Comment    : 행사 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    public List<FrontFestivalDTO> findFestivalList(FrontFestivalEntity existFrontFestivalEntity) {
        List<FrontFestivalEntity> festivalList = queryFactory
                .selectFrom(frontFestivalEntity)
                .orderBy(frontFestivalEntity.idx.desc())
                .where(frontFestivalEntity.festivalMonth.eq(existFrontFestivalEntity.getFestivalMonth())
                        .and(frontFestivalEntity.festivalDay.eq(existFrontFestivalEntity.getFestivalDay())))
                .fetch();

        return festivalList != null ? FrontFestivalEntity.toDtoList(festivalList) : Collections.emptyList();
    }

    /**
     * <pre>
     * 1. MethodName : findFestivalGroup
     * 2. ClassName  : FrontFestivalJpaRepository.java
     * 3. Comment    : 행사 리스트 그룹 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    public List<FrontFestivalDTO> findFestivalGroup(Integer month) {
        List<FrontFestivalEntity> festivalGroup = queryFactory
                .select(fields(FrontFestivalEntity.class,
                        frontFestivalEntity.festivalMonth,
                        frontFestivalEntity.festivalDay,
                        frontFestivalEntity.count().as("count")))
                .from(frontFestivalEntity)
                .where(frontFestivalEntity.festivalMonth.eq(month))
                .groupBy(frontFestivalEntity.festivalMonth,
                        frontFestivalEntity.festivalDay)
                .fetch();

        return festivalGroup != null ? FrontFestivalEntity.toDtoList(festivalGroup) : Collections.emptyList();
    }

    /**
     * <pre>
     * 1. MethodName : findOneFestival
     * 2. ClassName  : FrontFestivalJpaRepository.java
     * 3. Comment    : 행사 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    public FrontFestivalDTO findOneFestival(Long idx) {
        FrontFestivalEntity oneFestival = Optional.ofNullable(queryFactory
                .selectFrom(frontFestivalEntity)
                .where(frontFestivalEntity.idx.eq(idx))
                .fetchOne()).orElseThrow(() -> new TspException(NOT_FOUND_FESTIVAL, new Throwable()));

        return FrontFestivalEntity.toDto(oneFestival);
    }
}
