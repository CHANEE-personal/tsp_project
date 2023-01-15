package com.tsp.new_tsp_front.api.notice.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_front.api.notice.domain.FrontNoticeDTO;
import com.tsp.new_tsp_front.api.notice.domain.FrontNoticeEntity;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.tsp.new_tsp_front.api.notice.domain.FrontNoticeEntity.toDto;
import static com.tsp.new_tsp_front.api.notice.domain.FrontNoticeEntity.toDtoList;
import static com.tsp.new_tsp_front.api.notice.domain.QFrontNoticeEntity.frontNoticeEntity;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getString;
import static com.tsp.new_tsp_front.exception.ApiExceptionType.NOT_FOUND_NOTICE;
import static java.lang.Boolean.TRUE;

@Slf4j
@RequiredArgsConstructor
@Repository
public class FrontNoticeJpaQueryRepository {
    private final JPAQueryFactory queryFactory;

    private BooleanExpression searchNotice(Map<String, Object> noticeMap) {
        String searchType = getString(noticeMap.get("searchType"), "");
        String searchKeyword = getString(noticeMap.get("searchKeyword"), "");

        if (!Objects.equals(searchKeyword, "")) {
            return "0".equals(searchType) ?
                    frontNoticeEntity.title.contains(searchKeyword)
                            .or(frontNoticeEntity.description.contains(searchKeyword)) :
                    "1".equals(searchType) ?
                            frontNoticeEntity.title.contains(searchKeyword) :
                            frontNoticeEntity.description.contains(searchKeyword);
        } else {
            return null;
        }
    }

    private BooleanExpression fixedNotice(Map<String, Object> noticeMap) {
        String topFixed = !"".equals(getString(noticeMap.get("topFixed"), "")) ? TRUE.toString() : null;
        return Objects.equals(topFixed, TRUE.toString()) ? frontNoticeEntity.topFixed.eq(topFixed) : null;
    }

    /**
     * <pre>
     * 1. MethodName : findNoticeList
     * 2. ClassName  : AdminNoticeJpaRepository.java
     * 3. Comment    : 공지사항 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    public Page<FrontNoticeDTO> findNoticeList(Map<String, Object> noticeMap, PageRequest pageRequest) {
        List<FrontNoticeEntity> noticeList = queryFactory
                .selectFrom(frontNoticeEntity)
                .orderBy(frontNoticeEntity.idx.desc())
                .where(searchNotice(noticeMap), fixedNotice(noticeMap))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(toDtoList(noticeList), pageRequest, noticeList.size());
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneNotice
     * 2. ClassName  : FrontNoticeJpaRepository.java
     * 3. Comment    : 이전 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    public FrontNoticeDTO findPrevOneNotice(Long idx) {
        // 이전 공지사항 조회
        FrontNoticeEntity findPrevOneNotice = Optional.ofNullable(queryFactory
                .selectFrom(frontNoticeEntity)
                .orderBy(frontNoticeEntity.idx.desc())
                .where(frontNoticeEntity.idx.lt(idx)
                        .and(frontNoticeEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_NOTICE));

        return toDto(findPrevOneNotice);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneNotice
     * 2. ClassName  : FrontNoticeJpaRepository.java
     * 3. Comment    : 다음 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    public FrontNoticeDTO findNextOneNotice(Long idx) {
        // 다음 공지사항 조회
        FrontNoticeEntity findNextOneNotice = Optional.ofNullable(queryFactory
                .selectFrom(frontNoticeEntity)
                .orderBy(frontNoticeEntity.idx.desc())
                .where(frontNoticeEntity.idx.gt(idx)
                        .and(frontNoticeEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_NOTICE));

        return toDto(findNextOneNotice);
    }
}
