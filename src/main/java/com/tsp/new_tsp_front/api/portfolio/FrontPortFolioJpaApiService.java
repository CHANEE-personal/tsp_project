package com.tsp.new_tsp_front.api.portfolio;

import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioDTO;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioEntity;
import com.tsp.new_tsp_front.api.portfolio.service.impl.FrontPortFolioJpaRepository;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
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
     * 1. MethodName : getPortfolioCount
     * 2. ClassName  : FrontPortFolioJpaApiService.java
     * 3. Comment    : 프론트 > 포트폴리오 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 11.
     * </pre>
     */
    public int getPortfolioCount(Map<String, Object> portfolioMap) throws TspException {
        try {
            return frontPortFolioJpaRepository.getPortfolioCount(portfolioMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_PORTFOLIO_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : getPortFolio
     * 2. ClassName  : FrontPortFolioJpaApiService.java
     * 3. Comment    : 프론트 > 포트폴리오 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 11.
     * </pre>
     */
    @Transactional(readOnly = true)
    public List<FrontPortFolioDTO> getPortFolioList(Map<String, Object> portFolioMap) throws TspException {
        try {
            return frontPortFolioJpaRepository.getPortFolioList(portFolioMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_PORTFOLIO_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : getPortFolioInfo
     * 2. ClassName  : FrontPortFolioJpaApiService.java
     * 3. Comment    : 프론트 > 포트폴리오 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 12.
     * </pre>
     */
    @Transactional
    public FrontPortFolioDTO getPortFolioInfo(FrontPortFolioEntity frontPortFolioEntity) throws TspException {
        try {
            return frontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_PORTFOLIO, e);
        }
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
    @Transactional(readOnly = true)
    public FrontPortFolioDTO findPrevOnePortfolio(FrontPortFolioEntity frontPortFolioEntity) throws TspException {
        try {
            return frontPortFolioJpaRepository.findPrevOnePortfolio(frontPortFolioEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_PORTFOLIO, e);
        }
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
    @Transactional(readOnly = true)
    public FrontPortFolioDTO findNextOnePortfolio(FrontPortFolioEntity frontPortFolioEntity) throws TspException {
        try {
            return frontPortFolioJpaRepository.findNextOnePortfolio(frontPortFolioEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_PORTFOLIO, e);
        }
    }
}
