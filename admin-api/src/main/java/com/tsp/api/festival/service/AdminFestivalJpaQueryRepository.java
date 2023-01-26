package com.tsp.api.festival.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.api.festival.domain.AdminFestivalDTO;
import com.tsp.api.festival.domain.AdminFestivalEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.tsp.api.domain.festival.QAdminFestivalEntity.adminFestivalEntity;
import static com.tsp.common.StringUtil.getString;

@Repository
@RequiredArgsConstructor
public class AdminFestivalJpaQueryRepository {

    private final JPAQueryFactory queryFactory;

    private BooleanExpression searchFestival(Map<String, Object> festivalMap) {
        String searchType = getString(festivalMap.get("searchType"), "");
        String searchKeyword = getString(festivalMap.get("searchKeyword"), "");

        if (!Objects.equals(searchKeyword, "")) {
            return "0".equals(searchType) ?
                    adminFestivalEntity.festivalTitle.contains(searchKeyword)
                            .or(adminFestivalEntity.festivalDescription.contains(searchKeyword)) :
                    "1".equals(searchType) ?
                            adminFestivalEntity.festivalTitle.contains(searchKeyword) :
                                    adminFestivalEntity.festivalDescription.contains(searchKeyword);
        } else {
            return null;
        }
    }

    /**
     * <pre>
     * 1. MethodName : findFestivalList
     * 2. ClassName  : AdminFestivalJpaRepository.java
     * 3. Comment    : 관리자 행사 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    public Page<AdminFestivalDTO> findFestivalList(Map<String, Object> festivalMap, PageRequest pageRequest) {
        List<AdminFestivalEntity> festivalList = queryFactory
                .selectFrom(adminFestivalEntity)
                .where(searchFestival(festivalMap))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(AdminFestivalEntity.toDtoList(festivalList), pageRequest, festivalList.size());
    }
}
