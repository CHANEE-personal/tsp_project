package com.tsp.new_tsp_front.api.model.schedule.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.api.model.domain.schedule.FrontScheduleDTO;
import com.tsp.new_tsp_front.api.model.domain.schedule.FrontScheduleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.tsp.new_tsp_front.api.model.domain.FrontModelEntity.toDtoList;
import static com.tsp.new_tsp_front.api.model.domain.QFrontModelEntity.*;
import static com.tsp.new_tsp_front.api.model.domain.schedule.FrontScheduleEntity.toDtoList;
import static com.tsp.new_tsp_front.api.model.domain.schedule.QFrontScheduleEntity.*;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getInt;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getString;
import static java.time.LocalDate.now;
import static java.time.LocalDateTime.of;
import static java.util.Collections.emptyList;

@Slf4j
@RequiredArgsConstructor
@Repository
public class FrontScheduleJpaRepository {
    private final JPAQueryFactory queryFactory;

    private BooleanExpression searchModelSchedule(Map<String, Object> scheduleMap) {
        String searchKeyword = getString(scheduleMap.get("searchKeyword"), "");
        LocalDateTime searchStartTime = scheduleMap.get("searchStartTime") != null ? (LocalDateTime) scheduleMap.get("searchStartTime") : now().minusDays(now().getDayOfMonth() - 1).atStartOfDay();
        LocalDateTime searchEndTime = scheduleMap.get("searchEndTime") != null ? (LocalDateTime) scheduleMap.get("searchStartTime") : of(now().minusDays(now().getDayOfMonth()).plusMonths(1), LocalTime.of(23, 59, 59));

        return !Objects.equals(searchKeyword, "") ?
                frontModelEntity.modelKorName.contains(searchKeyword)
                        .or(frontModelEntity.modelEngName.contains(searchKeyword)
                                .or(frontModelEntity.modelDescription.contains(searchKeyword)))
                        .or(frontScheduleEntity.modelSchedule.contains(searchKeyword)) :
                frontScheduleEntity.modelScheduleTime.between(searchStartTime, searchEndTime);
    }

    /**
     * <pre>
     * 1. MethodName : findScheduleCount
     * 2. ClassName  : FrontScheduleJpaRepository.java
     * 3. Comment    : 모델 스케줄 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 01.
     * </pre>
     */
    public int findScheduleCount(Map<String, Object> scheduleMap) {
        return queryFactory.selectFrom(frontScheduleEntity)
                .where(searchModelSchedule(scheduleMap))
                .fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findScheduleList
     * 2. ClassName  : FrontScheduleJpaRepository.java
     * 3. Comment    : 모델 스케줄 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 01.
     * </pre>
     */
    public List<FrontScheduleDTO> findScheduleList(Map<String, Object> scheduleMap) {
        List<FrontScheduleEntity> scheduleList = queryFactory
                .selectFrom(frontScheduleEntity)
                .orderBy(frontScheduleEntity.idx.desc())
                .where(frontScheduleEntity.visible.eq("Y"))
                .offset(getInt(scheduleMap.get("jpaStartPage"), 0))
                .limit(getInt(scheduleMap.get("size"), 0))
                .fetch();

        return scheduleList != null ? toDtoList(scheduleList) : emptyList();
    }

    /**
     * <pre>
     * 1. MethodName : findModelScheduleList
     * 2. ClassName  : FrontScheduleJpaRepository.java
     * 3. Comment    : 모델 스케줄 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 03.
     * </pre>
     */
    public List<FrontModelDTO> findModelScheduleList(Map<String, Object> scheduleMap) {
        List<FrontModelEntity> findModelScheduleList = queryFactory
                .selectFrom(frontModelEntity)
                .leftJoin(frontModelEntity.modelScheduleList, frontScheduleEntity)
                .fetchJoin()
                .where(searchModelSchedule(scheduleMap)
                        .and(frontScheduleEntity.visible.eq("Y"))
                        .and(frontModelEntity.visible.eq("Y")))
                .fetch();

        return findModelScheduleList != null ? toDtoList(findModelScheduleList) : emptyList();
    }
}
