package com.tsp.new_tsp_front.api.festival.service;

import com.tsp.new_tsp_front.api.festival.domain.FrontFestivalDTO;
import com.tsp.new_tsp_front.api.festival.domain.FrontFestivalEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FrontFestivalJpaService {

    private final FrontFestivalJpaRepository frontFestivalJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findFestivalCount
     * 2. ClassName  : FrontFestivalJpaService.java
     * 3. Comment    : 프론트 > 행사 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    @Transactional(readOnly = true)
    public int findFestivalCount(Map<String, Object> festivalMap) {
        return frontFestivalJpaRepository.findFestivalCount(festivalMap);
    }

    /**
     * <pre>
     * 1. MethodName : findFestivalList
     * 2. ClassName  : FrontFestivalJpaService.java
     * 3. Comment    : 프론트 > 행사 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    @Cacheable(value = "festival", key = "#frontFestivalEntity")
    @Transactional(readOnly = true)
    public List<FrontFestivalDTO> findFestivalList(FrontFestivalEntity frontFestivalEntity) {
        return frontFestivalJpaRepository.findFestivalList(frontFestivalEntity);
    }

    /**
     * <pre>
     * 1. MethodName : findFestivalGroup
     * 2. ClassName  : FrontFestivalJpaService.java
     * 3. Comment    : 프론트 > 행사 리스트 그룹 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    @Cacheable(value = "festival", key = "#month")
    @Transactional(readOnly = true)
    public List<FrontFestivalDTO> findFestivalGroup(Integer month) {
        return frontFestivalJpaRepository.findFestivalGroup(month);
    }

    /**
     * <pre>
     * 1. MethodName : findOneFestival
     * 2. ClassName  : FrontFestivalJpaService.java
     * 3. Comment    : 프론트 > 행사 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    @Cacheable(value = "festival", key = "#idx")
    @Transactional(readOnly = true)
    public FrontFestivalDTO findOneFestival(Long idx) {
        return frontFestivalJpaRepository.findOneFestival(idx);
    }
}
