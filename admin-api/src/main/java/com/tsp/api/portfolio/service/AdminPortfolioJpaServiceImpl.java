package com.tsp.api.portfolio.service;

import com.tsp.api.common.service.AdminCommonJpaRepository;
import com.tsp.api.common.domain.NewCodeEntity;
import com.tsp.api.portfolio.domain.AdminPortFolioDTO;
import com.tsp.api.portfolio.domain.AdminPortFolioEntity;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

import static com.tsp.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class AdminPortfolioJpaServiceImpl implements AdminPortfolioJpaService {
    private final AdminPortfolioJpaQueryRepository adminPortfolioJpaQueryRepository;
    private final AdminPortfolioJpaRepository adminPortfolioJpaRepository;
    private final AdminCommonJpaRepository adminCommonJpaRepository;

    private AdminPortFolioEntity onePortfolio(Long idx) {
        return adminPortfolioJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_PORTFOLIO));
    }

    private Optional<NewCodeEntity> oneCommon(Integer categoryCd) {
        return Optional.ofNullable(adminCommonJpaRepository.findByCategoryCd(categoryCd)
                .orElseThrow(() -> new TspException(NOT_FOUND_COMMON)));
    }

    /**
     * <pre>
     * 1. MethodName : findPortfolioList
     * 2. ClassName  : AdminPortfolioJpaServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 14.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdminPortFolioDTO> findPortfolioList(Map<String, Object> portfolioMap, PageRequest pageRequest) {
        return adminPortfolioJpaQueryRepository.findPortfolioList(portfolioMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 18.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminPortFolioDTO findOnePortfolio(Long idx) {
        return adminPortfolioJpaQueryRepository.findOnePortfolio(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaServiceImpl.java
     * 3. Comment    : 관리자 이전 포트폴리오 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 14.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminPortFolioDTO findPrevOnePortfolio(Long idx) {
        return adminPortfolioJpaQueryRepository.findPrevOnePortfolio(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaServiceImpl.java
     * 3. Comment    : 관리자 다음 포트폴리오 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 14.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminPortFolioDTO findNextOnePortfolio(Long idx) {
        return adminPortfolioJpaQueryRepository.findNextOnePortfolio(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertPortfolio
     * 2. ClassName  : AdminPortfolioJpaServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 18.
     * </pre>
     */
    @Override
    @Transactional
    public AdminPortFolioDTO insertPortfolio(AdminPortFolioEntity adminPortFolioEntity) {
        try {
            oneCommon(adminPortFolioEntity.getCategoryCd())
                    .ifPresent(adminCommon -> adminCommon.addPortfolio(adminPortFolioEntity));
            return AdminPortFolioEntity.toDto(adminPortfolioJpaRepository.save(adminPortFolioEntity));
        } catch (Exception e) {
            throw new TspException(ERROR_PORTFOLIO);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updatePortfolio
     * 2. ClassName  : AdminPortfolioJpaServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 18.
     * </pre>
     */
    @Override
    @Transactional
    public AdminPortFolioDTO updatePortfolio(Long idx, AdminPortFolioEntity adminPortFolioEntity) {
        try {
            Optional.ofNullable(onePortfolio(idx))
                    .ifPresent(adminPortFolio -> adminPortFolio.update(adminPortFolioEntity));
            return AdminPortFolioEntity.toDto(adminPortFolioEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_PORTFOLIO);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deletePortfolio
     * 2. ClassName  : AdminPortfolioJpaServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 18.
     * </pre>
     */
    @Override
    @Transactional
    public Long deletePortfolio(Long idx) {
        try {
            adminPortfolioJpaRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_PORTFOLIO);
        }
    }
}
