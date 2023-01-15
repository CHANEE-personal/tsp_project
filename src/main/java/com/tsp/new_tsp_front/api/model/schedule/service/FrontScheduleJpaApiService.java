package com.tsp.new_tsp_front.api.model.schedule.service;

import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.schedule.FrontScheduleDTO;
import com.tsp.new_tsp_front.api.model.schedule.service.impl.FrontScheduleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FrontScheduleJpaApiService {
    private final FrontScheduleJpaRepository frontScheduleJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findScheduleList
     * 2. ClassName  : FrontScheduleJpaApiService.java
     * 3. Comment    : 모델 스케줄 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 01.
     * </pre>
     */
    @Transactional(readOnly = true)
    public Page<FrontScheduleDTO> findScheduleList(Map<String, Object> scheduleMap, PageRequest pageRequest) {
        return frontScheduleJpaRepository.findScheduleList(scheduleMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findModelScheduleList
     * 2. ClassName  : FrontScheduleJpaApiService.java
     * 3. Comment    : 모델 스케줄 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 03.
     * </pre>
     */
    @Transactional(readOnly = true)
    public List<FrontModelDTO> findModelScheduleList(Map<String, Object> scheduleMap) {
        return frontScheduleJpaRepository.findModelScheduleList(scheduleMap);
    }
}
