package com.tsp.api.common.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.api.common.domain.NewCodeDto;
import com.tsp.api.common.domain.NewCodeEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.tsp.api.common.domain.QNewCodeEntity.newCodeEntity;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminCommonJpaQueryRepository {

    private final JPAQueryFactory queryFactory;


    /**
     * <pre>
     * 1. MethodName : findCommonCodeList
     * 2. ClassName  : AdminCommonJpaRepository.java
     * 3. Comment    : 관리자 공통 코드 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public Page<NewCodeDto> findCommonCodeList(Map<String, Object> commonMap, PageRequest pageRequest) {
        List<NewCodeEntity> commonCodeList = queryFactory
                .selectFrom(newCodeEntity)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(NewCodeEntity.toDtoList(commonCodeList), pageRequest, commonCodeList.size());
    }
}
