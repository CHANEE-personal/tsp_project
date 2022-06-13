package com.tsp.new_tsp_front.api.support.service.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_front.api.support.domain.FrontSupportDTO;
import com.tsp.new_tsp_front.api.support.domain.FrontSupportEntity;
import com.tsp.new_tsp_front.api.support.mapper.SupportMapper;
import com.tsp.new_tsp_front.exception.ApiExceptionType;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Slf4j
@RequiredArgsConstructor
@Repository
public class FrontSupportJpaRepository {

	private final JPAQueryFactory queryFactory;
	private final EntityManager em;

	@Transactional
	public FrontSupportDTO insertSupportModel(FrontSupportEntity frontSupportEntity) {
		try {
			em.persist(frontSupportEntity);

			return SupportMapper.INSTANCE.toDto(frontSupportEntity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new TspException(ApiExceptionType.ERROR_MODEL);
		}
	}
}
