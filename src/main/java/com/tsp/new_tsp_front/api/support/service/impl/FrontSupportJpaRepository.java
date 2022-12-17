package com.tsp.new_tsp_front.api.support.service.impl;

import com.tsp.new_tsp_front.api.support.domain.FrontSupportDTO;
import com.tsp.new_tsp_front.api.support.domain.FrontSupportEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;


@Slf4j
@RequiredArgsConstructor
@Repository
public class FrontSupportJpaRepository {
    private final EntityManager em;

    /**
     * <pre>
     * 1. MethodName : insertSupportModel
     * 2. ClassName  : FrontSupportJpaRepository.java
     * 3. Comment    : 프론트 모델 지원하기
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 09.
     * </pre>
     */
    public FrontSupportDTO insertSupportModel(FrontSupportEntity frontSupportEntity) {
        em.persist(frontSupportEntity);
        return FrontSupportEntity.toDto(frontSupportEntity);
    }
}
