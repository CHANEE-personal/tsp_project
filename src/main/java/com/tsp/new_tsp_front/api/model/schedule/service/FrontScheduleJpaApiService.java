package com.tsp.new_tsp_front.api.model.schedule.service;

import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.schedule.FrontScheduleDTO;
import com.tsp.new_tsp_front.api.model.schedule.service.impl.FrontScheduleJpaRepository;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.exception.ApiExceptionType.NOT_FOUND_MODEL_SCHEDULE_LIST;

@Service
@RequiredArgsConstructor
public class FrontScheduleJpaApiService {
    private final FrontScheduleJpaRepository frontScheduleJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findScheduleCount
     * 2. ClassName  : FrontScheduleJpaApiService.java
     * 3. Comment    : 모델 스케줄 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 01.
     * </pre>
     */
    public Integer findScheduleCount() throws TspException {
        try {
            return frontScheduleJpaRepository.findScheduleCount();
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_MODEL_SCHEDULE_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findScheduleList
     * 2. ClassName  : FrontScheduleJpaApiService.java
     * 3. Comment    : 모델 스케줄 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 01.
     * </pre>
     */
    @Cacheable("schedule")
    @Transactional(readOnly = true)
    public List<FrontScheduleDTO> findScheduleList(Map<String, Object> scheduleMap) throws TspException {
        try {
            return frontScheduleJpaRepository.findScheduleList(scheduleMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_MODEL_SCHEDULE_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findModelScheduleList
     * 2. ClassName  : FrontScheduleJpaApiService.java
     * 3. Comment    : 모델 스케줄 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 03.
     * </pre>
     */
    @Cacheable("schedule")
    @Transactional(readOnly = true)
    public List<FrontModelDTO> findModelScheduleList(Map<String, Object> scheduleMap) throws TspException {
        try {
            return frontScheduleJpaRepository.findModelScheduleList(scheduleMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_MODEL_SCHEDULE_LIST, e);
        }
    }
}
