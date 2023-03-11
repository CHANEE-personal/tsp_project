package com.tsp.api.model.service.schedule;

import com.tsp.api.model.domain.schedule.AdminScheduleDto;
import com.tsp.api.model.domain.schedule.AdminScheduleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

public interface AdminScheduleJpaService {

    /**
     * <pre>
     * 1. MethodName : findScheduleList
     * 2. ClassName  : AdminScheduleJpaService.java
     * 3. Comment    : 관리자 모델 스케줄 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    Page<AdminScheduleDto> findScheduleList(Map<String, Object> scheduleMap, PageRequest pageRequest);

    /**
     * <pre>
     * 1. MethodName : findOneSchedule
     * 2. ClassName  : AdminScheduleJpaService.java
     * 3. Comment    : 관리자 모델 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    AdminScheduleDto findOneSchedule(Long idx);

    /**
     * <pre>
     * 1. MethodName : findPrevOneSchedule
     * 2. ClassName  : AdminScheduleJpaService.java
     * 3. Comment    : 관리자 모델 스케줄 이전 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 22.
     * </pre>
     */
    AdminScheduleDto findPrevOneSchedule(Long idx);

    /**
     * <pre>
     * 1. MethodName : findNextOneSchedule
     * 2. ClassName  : AdminScheduleJpaService.java
     * 3. Comment    : 관리자 모델 스케줄 다음 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 22.
     * </pre>
     */
    AdminScheduleDto findNextOneSchedule(Long idx);

    /**
     * <pre>
     * 1. MethodName : insertSchedule
     * 2. ClassName  : AdminScheduleJpaService.java
     * 3. Comment    : 관리자 모델 스케줄 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    AdminScheduleDto insertSchedule(Long idx, AdminScheduleEntity adminScheduleEntity);

    /**
     * <pre>
     * 1. MethodName : updateSchedule
     * 2. ClassName  : AdminScheduleJpaService.java
     * 3. Comment    : 관리자 모델 스케줄 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    AdminScheduleDto updateSchedule(Long idx, AdminScheduleEntity adminScheduleEntity);

    /**
     * <pre>
     * 1. MethodName : deleteSchedule
     * 2. ClassName  : AdminScheduleJpaService.java
     * 3. Comment    : 관리자 모델 스케줄 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    Long deleteSchedule(Long idx);
}
