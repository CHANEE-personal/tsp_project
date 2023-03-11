package com.tsp.api.model.service.agency;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.api.model.domain.agency.AdminAgencyDto;
import com.tsp.api.model.domain.agency.AdminAgencyEntity;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.tsp.api.common.domain.QCommonImageEntity.commonImageEntity;
import static com.tsp.api.model.domain.agency.AdminAgencyEntity.toDto;
import static com.tsp.api.model.domain.agency.AdminAgencyEntity.toDtoList;
import static com.tsp.api.model.domain.agency.QAdminAgencyEntity.adminAgencyEntity;
import static com.tsp.common.StringUtil.getString;
import static com.tsp.exception.ApiExceptionType.NOT_FOUND_AGENCY;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminAgencyJpaQueryRepository {
    private final JPAQueryFactory queryFactory;

    private BooleanExpression searchAgency(Map<String, Object> agencyMap) {
        String searchType = getString(agencyMap.get("searchType"), "");
        String searchKeyword = getString(agencyMap.get("searchKeyword"), "");

        if (!Objects.equals(searchKeyword, "")) {
            return "0".equals(searchType) ?
                    adminAgencyEntity.agencyName.contains(searchKeyword)
                            .or(adminAgencyEntity.agencyDescription.contains(searchKeyword)) :
                    "1".equals(searchType) ?
                            adminAgencyEntity.agencyName.contains(searchKeyword) :
                            adminAgencyEntity.agencyDescription.contains(searchKeyword);
        } else {
            return null;
        }
    }

    /**
     * <pre>
     * 1. MethodName : findAgencyList
     * 2. ClassName  : AdminAgencyJpaRepository.java
     * 3. Comment    : 관리자 모델 소속사 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 14.
     * </pre>
     */
    public Page<AdminAgencyDto> findAgencyList(Map<String, Object> agencyMap, PageRequest pageRequest) {
        List<AdminAgencyEntity> agencyList = queryFactory
                .selectFrom(adminAgencyEntity)
                .orderBy(adminAgencyEntity.idx.desc())
                .where(searchAgency(agencyMap))
                .where(adminAgencyEntity.visible.eq("Y"))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(toDtoList(agencyList), pageRequest, agencyList.size());
    }

    /**
     * <pre>
     * 1. MethodName : findOneAgency
     * 2. ClassName  : AdminAgencyJpaRepository.java
     * 3. Comment    : 관리자 모델 소속사 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 14.
     * </pre>
     */
    public AdminAgencyDto findOneAgency(Long idx) {
        AdminAgencyEntity findOneAgency = Optional.ofNullable(queryFactory
                .selectFrom(adminAgencyEntity)
                .orderBy(adminAgencyEntity.idx.desc())
                .leftJoin(adminAgencyEntity.commonImageEntityList, commonImageEntity)
                .fetchJoin()
                .where(adminAgencyEntity.visible.eq("Y")
                        .and(adminAgencyEntity.idx.eq(idx)))
                .fetchOne()).orElseThrow(() -> new TspException(NOT_FOUND_AGENCY));

        return toDto(findOneAgency);
    }
}
