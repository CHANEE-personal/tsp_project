package com.tsp.api.portfolio.service;

import com.tsp.api.portfolio.domain.FrontPortFolioDTO;
import com.tsp.api.portfolio.domain.FrontPortFolioEntity;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.tsp.exception.ApiExceptionType.NOT_FOUND_PORTFOLIO;

@Service
@RequiredArgsConstructor
public class FrontPortFolioJpaApiService {
    private final FrontPortFolioJpaQueryRepository frontPortFolioJpaQueryRepository;
    private final FrontPortFolioJpaRepository frontPortFolioJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findPortfolioList
     * 2. ClassName  : FrontPortFolioJpaApiService.java
     * 3. Comment    : 프론트 > 포트폴리오 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 11.
     * </pre>
     */
    @Transactional(readOnly = true)
    public Page<FrontPortFolioDTO> findPortfolioList(Map<String, Object> portFolioMap, PageRequest pageRequest) {
        return frontPortFolioJpaQueryRepository.findPortfolioList(portFolioMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOnePortfolio
     * 2. ClassName  : FrontPortFolioJpaApiService.java
     * 3. Comment    : 프론트 > 포트폴리오 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 12.
     * </pre>
     */
    @Transactional
    public FrontPortFolioDTO findOnePortfolio(Long idx) {
        FrontPortFolioEntity onePortfolio = frontPortFolioJpaRepository.findByIdx(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_PORTFOLIO));

        // 조회 수 증가
        onePortfolio.updateViewCount();
        return FrontPortFolioEntity.toDto(onePortfolio);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOnePortfolio
     * 2. ClassName  : FrontPortfolioJpaServiceImpl.java
     * 3. Comment    : 이전 포트폴리오 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    @Transactional
    public FrontPortFolioDTO findPrevOnePortfolio(Long idx) {
        return frontPortFolioJpaQueryRepository.findPrevOnePortfolio(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOnePortfolio
     * 2. ClassName  : FrontPortfolioJpaServiceImpl.java
     * 3. Comment    : 다음 포트폴리오 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    @Transactional
    public FrontPortFolioDTO findNextOnePortfolio(Long idx) {
        return frontPortFolioJpaQueryRepository.findNextOnePortfolio(idx);
    }
}
