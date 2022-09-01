package com.tsp.new_tsp_front.api.model.schedule.service.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_front.api.model.domain.schedule.FrontScheduleDTO;
import com.tsp.new_tsp_front.api.model.domain.schedule.FrontScheduleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.api.model.domain.schedule.QFrontScheduleEntity.*;
import static com.tsp.new_tsp_front.common.utils.StringUtil.getInt;

@Slf4j
@RequiredArgsConstructor
@Repository
public class FrontScheduleJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    /**
     * <pre>
     * 1. MethodName : findScheduleCount
     * 2. ClassName  : FrontScheduleJpaRepository.java
     * 3. Comment    : 모델 스케줄 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 01.
     * </pre>
     */
    public Integer findScheduleCount() {
        return queryFactory.selectFrom(frontScheduleEntity).fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findScheduleList
     * 2. ClassName  : FrontScheduleJpaRepository.java
     * 3. Comment    : 모델 스케줄 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 01.
     * </pre>
     */
    public List<FrontScheduleDTO> findScheduleList(Map<String, Object> scheduleMap) {
        List<FrontScheduleEntity> scheduleList = queryFactory
                .selectFrom(frontScheduleEntity)
                .orderBy(frontScheduleEntity.idx.desc())
                .where(frontScheduleEntity.visible.eq("Y"))
                .offset(getInt(scheduleMap.get("jpaStartPage"), 0))
                .limit(getInt(scheduleMap.get("size"), 0))
                .fetch();

        scheduleList.forEach(list -> scheduleList.get(scheduleList.indexOf(list))
                .setRnum(getInt(scheduleMap.get("startPage"), 1) * (getInt(scheduleMap.get("size"), 1)) - (2 - scheduleList.indexOf(list))));

        return FrontScheduleMapper.INSTANCE.toDtoList(scheduleList);
    }
}
