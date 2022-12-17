package com.tsp.new_tsp_front.api.notice.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_front.api.notice.domain.FrontNoticeDTO;
import com.tsp.new_tsp_front.api.notice.domain.FrontNoticeEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.api.notice.domain.QFrontNoticeEntity.frontNoticeEntity;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getInt;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getString;
import static java.lang.Boolean.TRUE;

@Slf4j
@RequiredArgsConstructor
@Repository
public class FrontNoticeJpaRepository {
    private final JPAQueryFactory queryFactory;

    private BooleanExpression searchNotice(Map<String, Object> noticeMap) {
        String searchType = getString(noticeMap.get("searchType"), "");
        String searchKeyword = getString(noticeMap.get("searchKeyword"), "");

        if ("0".equals(searchType)) {
            return frontNoticeEntity.title.contains(searchKeyword)
                    .or(frontNoticeEntity.description.contains(searchKeyword));
        } else if ("1".equals(searchType)) {
            return frontNoticeEntity.title.contains(searchKeyword);
        } else {
            return frontNoticeEntity.description.contains(searchKeyword);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findNoticeCount
     * 2. ClassName  : AdminNoticeJpaRepository.java
     * 3. Comment    : 공지사항 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    public Integer findNoticeCount(Map<String, Object> noticeMap) {
        return queryFactory.selectFrom(frontNoticeEntity).where(searchNotice(noticeMap)).fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findNoticesList
     * 2. ClassName  : AdminNoticeJpaRepository.java
     * 3. Comment    : 공지사항 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    public List<FrontNoticeDTO> findNoticesList(Map<String, Object> noticeMap) {
        List<FrontNoticeEntity> noticeList = queryFactory
                .selectFrom(frontNoticeEntity)
                .orderBy(frontNoticeEntity.idx.desc())
                .where(searchNotice(noticeMap))
                .offset(getInt(noticeMap.get("jpaStartPage"), 0))
                .limit(getInt(noticeMap.get("size"), 0))
                .fetch();

        noticeList.forEach(list -> noticeList.get(noticeList.indexOf(list))
                .setRowNum(getInt(noticeMap.get("startPage"), 1) * (getInt(noticeMap.get("size"), 1)) - (2 - noticeList.indexOf(list))));

        return FrontNoticeEntity.toDtoList(noticeList);
    }

    /**
     * <pre>
     * 1. MethodName : findFixedNoticeCount
     * 2. ClassName  : AdminNoticeJpaRepository.java
     * 3. Comment    : 상단고정 공지사항 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 24.
     * </pre>
     */
    public Integer findFixedNoticeCount(Map<String, Object> noticeMap) {
        return queryFactory.selectFrom(frontNoticeEntity)
                .where(searchNotice(noticeMap)
                        .and(frontNoticeEntity.topFixed.eq(TRUE.toString()))).fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findFixedNoticesList
     * 2. ClassName  : AdminNoticeJpaRepository.java
     * 3. Comment    : 상단 고정 공지사항 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 24.
     * </pre>
     */
    public List<FrontNoticeDTO> findFixedNoticesList(Map<String, Object> noticeMap) {
        List<FrontNoticeEntity> noticeList = queryFactory
                .selectFrom(frontNoticeEntity)
                .orderBy(frontNoticeEntity.idx.desc())
                .where(searchNotice(noticeMap)
                        .and(frontNoticeEntity.topFixed.eq(TRUE.toString())))
                .offset(getInt(noticeMap.get("jpaStartPage"), 0))
                .limit(getInt(noticeMap.get("size"), 0))
                .fetch();

        noticeList.forEach(list -> noticeList.get(noticeList.indexOf(list))
                .setRowNum(getInt(noticeMap.get("startPage"), 1) * (getInt(noticeMap.get("size"), 1)) - (2 - noticeList.indexOf(list))));

        return FrontNoticeEntity.toDtoList(noticeList);
    }

    /**
     * <pre>
     * 1. MethodName : findOneNotice
     * 2. ClassName  : AdminNoticeJpaRepository.java
     * 3. Comment    : 공지사항 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    FrontNoticeDTO findOneNotice(FrontNoticeEntity existFrontNoticeEntity) {
        FrontNoticeEntity findOneNotice = queryFactory
                .selectFrom(frontNoticeEntity)
                .orderBy(frontNoticeEntity.idx.desc())
                .where(frontNoticeEntity.idx.eq(existFrontNoticeEntity.getIdx())
                        .and(frontNoticeEntity.visible.eq("Y")))
                .fetchOne();

        return FrontNoticeEntity.toDto(findOneNotice);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneNotice
     * 2. ClassName  : FrontNoticeJpaRepository.java
     * 3. Comment    : 이전 공지사항 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 17.
     * </pre>
     */
    public FrontNoticeDTO findPrevOneNotice(FrontNoticeEntity existFrontNoticeEntity) {
        // 이전 공지사항 조회
        FrontNoticeEntity findPrevOneNotice = queryFactory
                .selectFrom(frontNoticeEntity)
                .orderBy(frontNoticeEntity.idx.desc())
                .where(frontNoticeEntity.idx.lt(existFrontNoticeEntity.getIdx())
                        .and(frontNoticeEntity.visible.eq("Y")))
                .fetchFirst();

        return FrontNoticeEntity.toDto(findPrevOneNotice);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneNotice
     * 2. ClassName  : FrontNoticeJpaRepository.java
     * 3. Comment    : 다음 공지사항 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 17.
     * </pre>
     */
    public FrontNoticeDTO findNextOneNotice(FrontNoticeEntity existFrontNoticeEntity) {
        // 다음 공지사항 조회
        FrontNoticeEntity findNextOneNotice = queryFactory
                .selectFrom(frontNoticeEntity)
                .orderBy(frontNoticeEntity.idx.desc())
                .where(frontNoticeEntity.idx.gt(existFrontNoticeEntity.getIdx())
                        .and(frontNoticeEntity.visible.eq("Y")))
                .fetchFirst();

        return FrontNoticeEntity.toDto(findNextOneNotice);
    }
}
