package com.tsp.api.production.service;

import com.tsp.api.production.domain.AdminProductionDTO;
import com.tsp.api.production.domain.AdminProductionEntity;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.tsp.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class AdminProductionJpaServiceImpl implements AdminProductionJpaService {
    private final AdminProductionJpaQueryRepository adminProductionJpaQueryRepository;
    private final AdminProductionJpaRepository adminProductionJpaRepository;

    private AdminProductionEntity oneProduction(Long idx) {
        return adminProductionJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_PRODUCTION));
    }

    /**
     * <pre>
     * 1. MethodName : findProductionList
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 프로덕션 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 09.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdminProductionDTO> findProductionList(Map<String, Object> productionMap, PageRequest pageRequest) {
        return adminProductionJpaQueryRepository.findProductionList(productionMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOneProduction
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 프로덕션 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 15.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminProductionDTO findOneProduction(Long idx) {
        return AdminProductionEntity.toDto(adminProductionJpaRepository.findByIdx(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_PRODUCTION)));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneProduction
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 이전 프로덕션 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 13.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminProductionDTO findPrevOneProduction(Long idx) {
        return adminProductionJpaQueryRepository.findPrevOneProduction(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneProduction
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 다음 프로덕션 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 13.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminProductionDTO findNextOneProduction(Long idx) {
        return adminProductionJpaQueryRepository.findNextOneProduction(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertProduction
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 프로덕션 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 16.
     * </pre>
     */
    @Override
    @Transactional
    public AdminProductionDTO insertProduction(AdminProductionEntity adminProductionEntity) {
        try {
            return AdminProductionEntity.toDto(adminProductionJpaRepository.save(adminProductionEntity));
        } catch (Exception e) {
            throw new TspException(ERROR_PRODUCTION);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateProduction
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 프로덕션 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 16.
     * </pre>
     */
    @Override
    @Transactional
    public AdminProductionDTO updateProduction(Long idx, AdminProductionEntity adminProductionEntity) {
        try {
            oneProduction(idx).update(adminProductionEntity);
            return AdminProductionEntity.toDto(adminProductionEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_MODEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteProduction
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 프로덕션 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 17.
     * </pre>
     */
    @Override
    @Transactional
    public Long deleteProduction(Long idx) {
        try {
            adminProductionJpaRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_PRODUCTION);
        }
    }
}
