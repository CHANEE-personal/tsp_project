package com.tsp.new_tsp_front.api.faq.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_front.api.faq.domain.FrontFaqDTO;
import com.tsp.new_tsp_front.api.faq.domain.FrontFaqEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.api.faq.domain.QFrontFaqEntity.frontFaqEntity;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getInt;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getString;

@Slf4j
@RequiredArgsConstructor
@Repository
public class FrontFaqJpaRepository {
    private final JPAQueryFactory queryFactory;

    private BooleanExpression searchFaq(Map<String, Object> faqMap) {
        String searchType = getString(faqMap.get("searchType"), "");
        String searchKeyword = getString(faqMap.get("searchKeyword"), "");

        if ("0".equals(searchType)) {
            return frontFaqEntity.title.contains(searchKeyword)
                    .or(frontFaqEntity.description.contains(searchKeyword));
        } else if ("1".equals(searchType)) {
            return frontFaqEntity.title.contains(searchKeyword);
        } else {
            return frontFaqEntity.description.contains(searchKeyword);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findFaqCount
     * 2. ClassName  : FrontFaqJpaRepository.java
     * 3. Comment    : 관리자 FAQ 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 23.
     * </pre>
     */
    public int findFaqCount(Map<String, Object> faqMap) {
        return queryFactory.selectFrom(frontFaqEntity).where(searchFaq(faqMap)).fetch().size();
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
    public List<FrontFaqDTO> findFaqList(Map<String, Object> faqMap) {
        List<FrontFaqEntity> faqList = queryFactory
                .selectFrom(frontFaqEntity)
                .orderBy(frontFaqEntity.idx.desc())
                .where(searchFaq(faqMap))
                .offset(getInt(faqMap.get("jpaStartPage"), 0))
                .limit(getInt(faqMap.get("size"), 0))
                .fetch();

        faqList.forEach(list -> faqList.get(faqList.indexOf(list))
                .setRowNum(getInt(faqMap.get("startPage"), 1) * (getInt(faqMap.get("size"), 1)) - (2 - faqList.indexOf(list))));

        return FrontFaqEntity.toDtoList(faqList);
    }

    /**
     * <pre>
     * 1. MethodName : findOneFaq
     * 2. ClassName  : FrontFaqJpaRepository.java
     * 3. Comment    : 관리자 FAQ 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 23.
     * </pre>
     */
    public FrontFaqDTO findOneFaq(Long idx) {
        FrontFaqEntity findOneFaq = queryFactory
                .selectFrom(frontFaqEntity)
                .orderBy(frontFaqEntity.idx.desc())
                .where(frontFaqEntity.idx.eq(idx)
                        .and(frontFaqEntity.visible.eq("Y")))
                .fetchOne();

        return FrontFaqEntity.toDto(findOneFaq);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneFaq
     * 2. ClassName  : FrontFaqJpaRepository.java
     * 3. Comment    : 이전 Faq 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 17.
     * </pre>
     */
    public FrontFaqDTO findPrevOneFaq(Long idx) {
        // 이전 FAQ 조회
        FrontFaqEntity findPrevOneFaq = queryFactory
                .selectFrom(frontFaqEntity)
                .orderBy(frontFaqEntity.idx.desc())
                .where(frontFaqEntity.idx.lt(idx)
                        .and(frontFaqEntity.visible.eq("Y")))
                .fetchFirst();

        return FrontFaqEntity.toDto(findPrevOneFaq);
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
        FrontFaqEntity findNextOneFaq = queryFactory
                .selectFrom(frontFaqEntity)
                .orderBy(frontFaqEntity.idx.desc())
                .where(frontFaqEntity.idx.gt(idx)
                        .and(frontFaqEntity.visible.eq("Y")))
                .fetchFirst();

        return FrontFaqEntity.toDto(findNextOneFaq);
    }
}
