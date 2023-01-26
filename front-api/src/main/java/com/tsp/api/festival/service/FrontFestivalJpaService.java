package com.tsp.api.festival.service;

import com.tsp.api.festival.domain.FrontFestivalDTO;
import com.tsp.api.festival.domain.FrontFestivalEntity;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.tsp.exception.ApiExceptionType.NOT_FOUND_FESTIVAL;

@Service
@RequiredArgsConstructor
public class FrontFestivalJpaService {

    private final FrontFestivalJpaQueryRepository frontFestivalJpaQueryRepository;
    private final FrontFestivalJpaRepository frontFestivalJpaRepository;

    private FrontFestivalEntity oneFestival(Long idx) {
        return frontFestivalJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_FESTIVAL));
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
    @Transactional(readOnly = true)
    public Page<FrontFestivalDTO> findFestivalList(FrontFestivalEntity frontFestivalEntity, PageRequest pageRequest) {
        return frontFestivalJpaQueryRepository.findFestivalList(frontFestivalEntity, pageRequest);
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
    @Transactional(readOnly = true)
    public List<FrontFestivalDTO> findFestivalGroup(Integer month) {
        return frontFestivalJpaQueryRepository.findFestivalGroup(month);
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
    @Transactional(readOnly = true)
    public FrontFestivalDTO findOneFestival(Long idx) {
        return FrontFestivalEntity.toDto(oneFestival(idx));
    }
}
