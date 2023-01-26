package com.tsp.api.production.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.api.common.EntityType;
import com.tsp.api.common.domain.CommonImageEntity;
import com.tsp.api.production.domain.AdminProductionDTO;
import com.tsp.api.production.domain.AdminProductionEntity;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.tsp.api.domain.common.QCommonImageEntity.commonImageEntity;
import static com.tsp.api.production.domain.AdminProductionEntity.toDto;
import static com.tsp.api.domain.production.QAdminProductionEntity.adminProductionEntity;
import static com.tsp.common.StringUtil.getString;
import static com.tsp.exception.ApiExceptionType.NOT_FOUND_PRODUCTION;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminProductionJpaQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchProduction(Map<String, Object> productionMap) {
        String searchType = getString(productionMap.get("searchType"), "");
        String searchKeyword = getString(productionMap.get("searchKeyword"), "");

        if (!Objects.equals(searchKeyword, "")) {
            return "0".equals(searchType) ?
                    adminProductionEntity.title.contains(searchKeyword)
                            .or(adminProductionEntity.description.contains(searchKeyword)) :
                    "1".equals(searchType) ?
                            adminProductionEntity.title.contains(searchKeyword) :
                            adminProductionEntity.description.contains(searchKeyword);
        } else {
            return null;
        }
    }

    /**
     * <pre>
     * 1. MethodName : findProductionList
     * 2. ClassName  : AdminProductionJpaRepository.java
     * 3. Comment    : 관리자 프로덕션 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 09.
     * </pre>
     */
    public Page<AdminProductionDTO> findProductionList(Map<String, Object> productionMap, PageRequest pageRequest) {
        List<AdminProductionEntity> productionList = queryFactory
                .selectFrom(adminProductionEntity)
                .orderBy(adminProductionEntity.idx.desc())
                .where(searchProduction(productionMap))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(AdminProductionEntity.toDtoList(productionList), pageRequest, productionList.size());
    }

    /**
     * <pre>
     * 1. MethodName : findOneProduction
     * 2. ClassName  : AdminProductionJpaRepository.java
     * 3. Comment    : 관리자 프로덕션 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 09.
     * </pre>
     */
    public AdminProductionDTO findOneProduction(Long idx) {
        AdminProductionEntity findOneProduction = Optional.ofNullable(queryFactory
                .selectFrom(adminProductionEntity)
                .orderBy(adminProductionEntity.idx.desc())
                .leftJoin(adminProductionEntity.commonImageEntityList, commonImageEntity)
                .fetchJoin()
                .where(adminProductionEntity.idx.eq(idx)
                        .and(adminProductionEntity.visible.eq("Y"))
                        .and(commonImageEntity.typeName.eq(EntityType.PRODUCTION)))
                .fetchOne()).orElseThrow(() -> new TspException(NOT_FOUND_PRODUCTION));

        return toDto(findOneProduction);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneProduction
     * 2. ClassName  : AdminProductionJpaRepository.java
     * 3. Comment    : 관리자 이전 프로덕션 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 13.
     * </pre>
     */
    public AdminProductionDTO findPrevOneProduction(Long idx) {
        // 이전 프로덕션 조회
        AdminProductionEntity findPrevOneProduction = Optional.ofNullable(queryFactory
                .selectFrom(adminProductionEntity)
                .orderBy(adminProductionEntity.idx.desc())
                .where(adminProductionEntity.idx.lt(idx)
                        .and(adminProductionEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_PRODUCTION));

        return toDto(findPrevOneProduction);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneProduction
     * 2. ClassName  : AdminProductionJpaRepository.java
     * 3. Comment    : 관리자 다음 프로덕션 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 13.
     * </pre>
     */
    public AdminProductionDTO findNextOneProduction(Long idx) {
        // 다음 프로덕션 조회
        AdminProductionEntity findNextOneProduction = Optional.ofNullable(queryFactory
                .selectFrom(adminProductionEntity)
                .orderBy(adminProductionEntity.idx.asc())
                .where(adminProductionEntity.idx.gt(idx)
                        .and(adminProductionEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_PRODUCTION));

        return toDto(findNextOneProduction);
    }

    /**
     * <pre>
     * 1. MethodName : insertProductionImage
     * 2. ClassName  : AdminProductionJpaRepository.java
     * 3. Comment    : 관리자 프로덕션 이미지 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 14.
     * </pre>
     */
    public Long insertProductionImage(CommonImageEntity commonImageEntity) {
        em.persist(commonImageEntity);
        return commonImageEntity.getIdx();
    }
}
