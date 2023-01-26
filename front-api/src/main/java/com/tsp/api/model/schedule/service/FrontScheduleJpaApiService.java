package com.tsp.api.model.schedule.service;

import com.tsp.api.model.domain.FrontModelDTO;
import com.tsp.api.model.domain.schedule.FrontScheduleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FrontScheduleJpaApiService {
    private final FrontScheduleJpaQueryRepository frontScheduleJpaQueryRepository;

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
        return frontScheduleJpaQueryRepository.findScheduleList(scheduleMap, pageRequest);
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
        return frontScheduleJpaQueryRepository.findModelScheduleList(scheduleMap);
    }
}
