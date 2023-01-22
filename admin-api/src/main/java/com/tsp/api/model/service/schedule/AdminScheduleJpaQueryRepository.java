package com.tsp.api.model.service.schedule;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.api.domain.model.AdminModelDTO;
import com.tsp.api.domain.model.AdminModelEntity;
import com.tsp.api.domain.model.schedule.AdminScheduleDTO;
import com.tsp.api.domain.model.schedule.AdminScheduleEntity;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.tsp.api.domain.model.AdminModelEntity.toDto;
import static com.tsp.api.domain.model.QAdminModelEntity.adminModelEntity;
import static com.tsp.api.domain.model.schedule.AdminScheduleEntity.toDtoList;
import static com.tsp.api.domain.model.schedule.QAdminScheduleEntity.adminScheduleEntity;
import static com.tsp.common.StringUtil.getString;
import static com.tsp.exception.ApiExceptionType.NOT_FOUND_MODEL_SCHEDULE;
import static java.time.LocalDate.now;
import static java.time.LocalDateTime.of;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminScheduleJpaQueryRepository {
    private final JPAQueryFactory queryFactory;

    private BooleanExpression searchModelSchedule(Map<String, Object> scheduleMap) {
        String searchKeyword = getString(scheduleMap.get("searchKeyword"), "");
        LocalDateTime searchStartTime = scheduleMap.get("searchStartTime") != null ? (LocalDateTime) scheduleMap.get("searchStartTime") : now().minusDays(now().getDayOfMonth() - 1).atStartOfDay();
        LocalDateTime searchEndTime = scheduleMap.get("searchEndTime") != null ? (LocalDateTime) scheduleMap.get("searchStartTime") : of(now().minusDays(now().getDayOfMonth()).plusMonths(1), LocalTime.of(23, 59, 59));

        return !Objects.equals(searchKeyword, "") ?
                        adminScheduleEntity.modelSchedule.contains(searchKeyword) :
                adminScheduleEntity.modelScheduleTime.between(searchStartTime, searchEndTime);
    }

    /**
     * <pre>
     * 1. MethodName : findModelScheduleList
     * 2. ClassName  : AdminScheduleJpaRepository.java
     * 3. Comment    : 관리자 모델 스케줄 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    public Page<AdminScheduleDTO> findScheduleList(Map<String, Object> scheduleMap, PageRequest pageRequest) {
        List<AdminScheduleEntity> scheduleList = queryFactory
                .selectFrom(adminScheduleEntity)
                .orderBy(adminScheduleEntity.idx.desc())
                .where(searchModelSchedule(scheduleMap)
                        .and(adminScheduleEntity.visible.eq("Y")))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(toDtoList(scheduleList), pageRequest, scheduleList.size());
    }

    /**
     * <pre>
     * 1. MethodName : findOneModelSchedule
     * 2. ClassName  : AdminScheduleJpaRepository.java
     * 3. Comment    : 관리자 모델 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    public AdminModelDTO findOneModelSchedule(AdminScheduleEntity existAdminScheduleEntity) {
        AdminModelEntity findOneModelSchedule = Optional.ofNullable(queryFactory
                .selectFrom(adminModelEntity)
                .leftJoin(adminModelEntity.scheduleList, adminScheduleEntity)
                .fetchJoin()
                .where(adminModelEntity.visible.eq("Y")
                        .and(adminScheduleEntity.visible.eq("Y"))
                        .and(adminModelEntity.idx.eq(existAdminScheduleEntity.getAdminModelEntity().getIdx()))
                        .and(adminScheduleEntity.idx.eq(existAdminScheduleEntity.getIdx())))
                .fetchOne()).orElseThrow(() -> new TspException(NOT_FOUND_MODEL_SCHEDULE));

        return toDto(findOneModelSchedule);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneSchedule
     * 2. ClassName  : AdminScheduleJpaRepository.java
     * 3. Comment    : 관리자 이전 모델 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 22.
     * </pre>
     */
    public AdminScheduleDTO findPrevOneSchedule(Long idx) {
        // 이전 모델 스케줄 조회
        AdminScheduleEntity findPrevOneSchedule = Optional.ofNullable(queryFactory
                .selectFrom(adminScheduleEntity)
                .orderBy(adminScheduleEntity.idx.desc())
                .where(adminScheduleEntity.idx.lt(idx)
                        .and(adminScheduleEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_MODEL_SCHEDULE));

        return AdminScheduleEntity.toDto(findPrevOneSchedule);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneSchedule
     * 2. ClassName  : AdminScheduleJpaRepository.java
     * 3. Comment    : 관리자 다음 모델 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 22.
     * </pre>
     */
    public AdminScheduleDTO findNextOneSchedule(Long idx) {
        // 다음 모델 스케줄 조회
        AdminScheduleEntity findNextOneSchedule = Optional.ofNullable(queryFactory
                .selectFrom(adminScheduleEntity)
                .orderBy(adminScheduleEntity.idx.asc())
                .where(adminScheduleEntity.idx.gt(idx)
                        .and(adminScheduleEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_MODEL_SCHEDULE));

        return AdminScheduleEntity.toDto(findNextOneSchedule);
    }
}
