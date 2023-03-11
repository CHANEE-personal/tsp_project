package com.tsp.api.faq.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.api.faq.domain.AdminFaqDto;
import com.tsp.api.faq.domain.AdminFaqEntity;
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

import static com.tsp.api.faq.domain.AdminFaqEntity.toDto;
import static com.tsp.api.faq.domain.AdminFaqEntity.toDtoList;
import static com.tsp.api.faq.domain.QAdminFaqEntity.adminFaqEntity;
import static com.tsp.common.StringUtil.getString;
import static com.tsp.exception.ApiExceptionType.NOT_FOUND_FAQ;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminFaqJpaQueryRepository {
    private final JPAQueryFactory queryFactory;

    private BooleanExpression searchFaq(Map<String, Object> faqMap) {
        String searchType = getString(faqMap.get("searchType"), "");
        String searchKeyword = getString(faqMap.get("searchKeyword"), "");

        if (!Objects.equals(searchKeyword, "")) {
            return "0".equals(searchType) ?
                    adminFaqEntity.title.contains(searchKeyword)
                            .or(adminFaqEntity.description.contains(searchKeyword)) :
                    "1".equals(searchType) ?
                            adminFaqEntity.title.contains(searchKeyword) :
                            adminFaqEntity.description.contains(searchKeyword);
        } else {
            return null;
        }
    }

    /**
     * <pre>
     * 1. MethodName : findFaqList
     * 2. ClassName  : AdminFaqJpaRepository.java
     * 3. Comment    : 관리자 FAQ 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 18.
     * </pre>
     */
    public Page<AdminFaqDto> findFaqList(Map<String, Object> faqMap, PageRequest pageRequest) {
        List<AdminFaqEntity> faqList = queryFactory
                .selectFrom(adminFaqEntity)
                .orderBy(adminFaqEntity.idx.desc())
                .where(searchFaq(faqMap))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(toDtoList(faqList), pageRequest, faqList.size());
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneFaq
     * 2. ClassName  : AdminFaqJpaRepository.java
     * 3. Comment    : 관리자 이전 FAQ 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 18.
     * </pre>
     */
    public AdminFaqDto findPrevOneFaq(Long idx) {
        AdminFaqEntity findPrevOneFaq = Optional.ofNullable(queryFactory
                .selectFrom(adminFaqEntity)
                .orderBy(adminFaqEntity.idx.desc())
                .where(adminFaqEntity.idx.lt(idx)
                        .and(adminFaqEntity.visible.eq("Y")))
                .fetchOne()).orElseThrow(() -> new TspException(NOT_FOUND_FAQ));

        return toDto(findPrevOneFaq);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneFaq
     * 2. ClassName  : AdminFaqJpaRepository.java
     * 3. Comment    : 관리자 다음 FAQ 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 18.
     * </pre>
     */
    public AdminFaqDto findNextOneFaq(Long idx) {
        AdminFaqEntity findNextOneFaq = Optional.ofNullable(queryFactory
                .selectFrom(adminFaqEntity)
                .orderBy(adminFaqEntity.idx.desc())
                .where(adminFaqEntity.idx.gt(idx)
                        .and(adminFaqEntity.visible.eq("Y")))
                .fetchOne()).orElseThrow(() -> new TspException(NOT_FOUND_FAQ));

        return toDto(findNextOneFaq);
    }
}
