package com.tsp.api.notice.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.api.notice.domain.AdminNoticeDTO;
import com.tsp.api.notice.domain.AdminNoticeEntity;
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

import static com.tsp.api.notice.domain.AdminNoticeEntity.toDto;
import static com.tsp.api.notice.domain.AdminNoticeEntity.toDtoList;
import static com.tsp.api.notice.domain.QAdminNoticeEntity.adminNoticeEntity;
import static com.tsp.common.StringUtil.getString;
import static com.tsp.exception.ApiExceptionType.NOT_FOUND_NOTICE;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminNoticeJpaQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchNotice(Map<String, Object> noticeMap) {
        String searchType = getString(noticeMap.get("searchType"), "");
        String searchKeyword = getString(noticeMap.get("searchKeyword"), "");

        if (!Objects.equals(searchKeyword, "")) {
            return "0".equals(searchType) ?
                    adminNoticeEntity.title.contains(searchKeyword)
                            .or(adminNoticeEntity.description.contains(searchKeyword)) :
                    "1".equals(searchType) ?
                            adminNoticeEntity.title.contains(searchKeyword) :
                            adminNoticeEntity.description.contains(searchKeyword);
        } else {
            return null;
        }
    }

    /**
     * <pre>
     * 1. MethodName : findNoticeList
     * 2. ClassName  : AdminNoticeJpaRepository.java
     * 3. Comment    : 관리자 공지사항 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    public Page<AdminNoticeDTO> findNoticeList(Map<String, Object> noticeMap, PageRequest pageRequest) {
        List<AdminNoticeEntity> noticeList = queryFactory
                .selectFrom(adminNoticeEntity)
                .orderBy(adminNoticeEntity.idx.desc())
                .where(searchNotice(noticeMap))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(toDtoList(noticeList), pageRequest, noticeList.size());
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneNotice
     * 2. ClassName  : AdminNoticeJpaRepository.java
     * 3. Comment    : 관리자 이전 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 18.
     * </pre>
     */
    AdminNoticeDTO findPrevOneNotice(Long idx) {
        AdminNoticeEntity findPrevOneNotice = Optional.ofNullable(queryFactory
                .selectFrom(adminNoticeEntity)
                .orderBy(adminNoticeEntity.idx.desc())
                .where(adminNoticeEntity.idx.lt(idx)
                        .and(adminNoticeEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_NOTICE));

        return toDto(findPrevOneNotice);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneNotice
     * 2. ClassName  : AdminNoticeJpaRepository.java
     * 3. Comment    : 관리자 이전 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 18.
     * </pre>
     */
    AdminNoticeDTO findNextOneNotice(Long idx) {
        AdminNoticeEntity findNextOneNotice = Optional.ofNullable(queryFactory
                .selectFrom(adminNoticeEntity)
                .orderBy(adminNoticeEntity.idx.desc())
                .where(adminNoticeEntity.idx.gt(idx)
                        .and(adminNoticeEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_NOTICE));

        return toDto(findNextOneNotice);
    }

    /**
     * <pre>
     * 1. MethodName : toggleFixed
     * 2. ClassName  : AdminNoticeJpaRepository.java
     * 3. Comment    : 관리자 공지사항 상단 고정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 23.
     * </pre>
     */
    public Boolean toggleFixed(Long idx) {
        AdminNoticeEntity oneNotice = em.find(AdminNoticeEntity.class, idx);
        Boolean fixed = !oneNotice.getTopFixed();

        queryFactory
                .update(adminNoticeEntity)
                .where(adminNoticeEntity.idx.eq(idx))
                .set(adminNoticeEntity.topFixed, fixed)
                .execute();

        em.flush();
        em.clear();

        return fixed;
    }
}
