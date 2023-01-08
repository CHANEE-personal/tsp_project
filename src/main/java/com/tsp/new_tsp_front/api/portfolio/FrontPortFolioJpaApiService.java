package com.tsp.new_tsp_front.api.portfolio;

import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioDTO;
import com.tsp.new_tsp_front.api.portfolio.service.impl.FrontPortFolioJpaRepository;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class FrontPortFolioJpaApiService {
    private final FrontPortFolioJpaRepository frontPortFolioJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findPortfolioCount
     * 2. ClassName  : FrontPortFolioJpaApiService.java
     * 3. Comment    : 프론트 > 포트폴리오 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 11.
     * </pre>
     */
    public int findPortfolioCount(Map<String, Object> portfolioMap) {
        return frontPortFolioJpaRepository.findPortfolioCount(portfolioMap);
    }

    /**
     * <pre>
     * 1. MethodName : findPortfolioList
     * 2. ClassName  : FrontPortFolioJpaApiService.java
     * 3. Comment    : 프론트 > 포트폴리오 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 11.
     * </pre>
     */
    @Cacheable(value = "portfolio", key = "#portFolioMap")
    @Transactional(readOnly = true)
    public List<FrontPortFolioDTO> findPortfolioList(Map<String, Object> portFolioMap) {
        return frontPortFolioJpaRepository.findPortfolioList(portFolioMap);
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
    @CachePut(value = "portfolio", key = "#idx")
    @Transactional
    public FrontPortFolioDTO findOnePortfolio(Long idx) {
        return frontPortFolioJpaRepository.findOnePortfolio(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOnePortfolio
     * 2. ClassName  : FrontPortfolioJpaServiceImpl.java
     * 3. Comment    : 이전 포트폴리오 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 17.
     * </pre>
     */
    @CachePut(value = "portfolio", key = "#idx")
    @Transactional
    public FrontPortFolioDTO findPrevOnePortfolio(Long idx) {
        return frontPortFolioJpaRepository.findPrevOnePortfolio(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOnePortfolio
     * 2. ClassName  : FrontPortfolioJpaServiceImpl.java
     * 3. Comment    : 다음 포트폴리오 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 17.
     * </pre>
     */
    @CachePut(value = "portfolio", key = "#idx")
    @Transactional
    public FrontPortFolioDTO findNextOnePortfolio(Long idx) {
        return frontPortFolioJpaRepository.findNextOnePortfolio(idx);
    }
}
