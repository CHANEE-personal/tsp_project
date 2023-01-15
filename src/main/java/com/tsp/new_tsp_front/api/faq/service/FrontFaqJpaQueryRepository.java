package com.tsp.new_tsp_front.api.faq.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_front.api.faq.domain.FrontFaqDTO;
import com.tsp.new_tsp_front.api.faq.domain.FrontFaqEntity;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.tsp.new_tsp_front.api.faq.domain.FrontFaqEntity.toDto;
import static com.tsp.new_tsp_front.api.faq.domain.FrontFaqEntity.toDtoList;
import static com.tsp.new_tsp_front.api.faq.domain.QFrontFaqEntity.frontFaqEntity;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getInt;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getString;
import static com.tsp.new_tsp_front.exception.ApiExceptionType.NOT_FOUND_FAQ;
import static java.util.Collections.emptyList;

@Slf4j
@RequiredArgsConstructor
@Repository
public class FrontFaqJpaQueryRepository {
    private final JPAQueryFactory queryFactory;

    private BooleanExpression searchFaq(Map<String, Object> faqMap) {
        String searchType = getString(faqMap.get("searchType"), "");
        String searchKeyword = getString(faqMap.get("searchKeyword"), "");

        if (!Objects.equals(searchKeyword, "")) {
            return "0".equals(searchType) ?
                    frontFaqEntity.title.contains(searchKeyword)
                            .or(frontFaqEntity.description.contains(searchKeyword)) :
                    "1".equals(searchType) ?
                            frontFaqEntity.title.contains(searchKeyword) :
                            frontFaqEntity.description.contains(searchKeyword);
        } else {
            return null;
        }
    }

    /**
     * <pre>
     * 1. MethodName : findFaqList
     * 2. ClassName  : FrontFaqJpaRepository.java
     * 3. Comment    : FAQ 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 23.
     * </pre>
     */
    public Page<FrontFaqDTO> findFaqList(Map<String, Object> faqMap, PageRequest pageRequest) {
        List<FrontFaqEntity> faqList = queryFactory
                .selectFrom(frontFaqEntity)
                .orderBy(frontFaqEntity.idx.desc())
                .where(searchFaq(faqMap))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(toDtoList(faqList), pageRequest, faqList.size());
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneFaq
     * 2. ClassName  : FrontFaqJpaRepository.java
     * 3. Comment    : 이전 Faq 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    public FrontFaqDTO findPrevOneFaq(Long idx) {
        // 이전 FAQ 조회
        FrontFaqEntity findPrevOneFaq = Optional.ofNullable(queryFactory
                .selectFrom(frontFaqEntity)
                .orderBy(frontFaqEntity.idx.desc())
                .where(frontFaqEntity.idx.lt(idx)
                        .and(frontFaqEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_FAQ));

        return toDto(findPrevOneFaq);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneFaq
     * 2. ClassName  : FrontFaqJpaRepository.java
     * 3. Comment    : 다음 Faq 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 17.
     * </pre>
     */
    public FrontFaqDTO findNextOneFaq(Long idx) {
        // 다음 FAQ 조회
        FrontFaqEntity findNextOneFaq = Optional.ofNullable(queryFactory
                .selectFrom(frontFaqEntity)
                .orderBy(frontFaqEntity.idx.desc())
                .where(frontFaqEntity.idx.gt(idx)
                        .and(frontFaqEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_FAQ));

        return toDto(findNextOneFaq);
    }
}
