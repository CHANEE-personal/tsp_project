package com.tsp.api.production.service;

import com.tsp.api.production.domain.AdminProductionDto;
import com.tsp.api.production.domain.AdminProductionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

public interface AdminProductionJpaService {

    /**
     * <pre>
     * 1. MethodName : findProductionList
     * 2. ClassName  : AdminProductionJpaService.java
     * 3. Comment    : 관리자 프로덕션 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 09.
     * </pre>
     */
    Page<AdminProductionDto> findProductionList(Map<String, Object> productionMap, PageRequest pageRequest);

    /**
     * <pre>
     * 1. MethodName : findOneProduction
     * 2. ClassName  : AdminProductionJpaService.java
     * 3. Comment    : 관리자 프로덕션 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 15.
     * </pre>
     */
    AdminProductionDto findOneProduction(Long idx);

    /**
     * <pre>
     * 1. MethodName : findPrevOneProduction
     * 2. ClassName  : AdminProductionJpaService.java
     * 3. Comment    : 관리자 이전 프로덕션 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 13.
     * </pre>
     */
    AdminProductionDto findPrevOneProduction(Long idx);

    /**
     * <pre>
     * 1. MethodName : findNextOneProduction
     * 2. ClassName  : AdminProductionJpaService.java
     * 3. Comment    : 관리자 다음 프로덕션 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 13.
     * </pre>
     */
    AdminProductionDto findNextOneProduction(Long idx);

    /**
     * <pre>
     * 1. MethodName : insertProduction
     * 2. ClassName  : AdminProductionJpaService.java
     * 3. Comment    : 관리자 프로덕션 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 16.
     * </pre>
     */
    AdminProductionDto insertProduction(AdminProductionEntity adminProductionEntity);

    /**
     * <pre>
     * 1. MethodName : updateProduction
     * 2. ClassName  : AdminProductionJpaService.java
     * 3. Comment    : 관리자 프로덕션 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 16.
     * </pre>
     */
    AdminProductionDto updateProduction(Long idx, AdminProductionEntity adminProductionEntity);

    /**
     * <pre>
     * 1. MethodName : deleteProduction
     * 2. ClassName  : AdminProductionJpaService.java
     * 3. Comment    : 관리자 프로덕션 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 17.
     * </pre>
     */
    Long deleteProduction(Long idx);

}
