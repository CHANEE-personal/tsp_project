package com.tsp.new_tsp_front.api.production.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionDTO;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity;
import com.tsp.new_tsp_front.api.production.domain.QFrontProductionEntity;
import com.tsp.new_tsp_front.api.production.service.impl.jpa.ProductionMapper;
import com.tsp.new_tsp_front.common.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.api.production.domain.QFrontProductionEntity.frontProductionEntity;

@Repository
@RequiredArgsConstructor
public class FrontProductionJpaRepository {

	private final JPAQueryFactory queryFactory;
	private final EntityManager em;


	private BooleanExpression searchProduction(Map<String, Object> productionMap) {
		String searchType = StringUtil.getString(productionMap.get("searchType"),"");
		String searchKeyword = StringUtil.getString(productionMap.get("searchKeyword"),"");

		if ("0".equals(searchType)) {
			return frontProductionEntity.title.contains(searchKeyword)
					.or(frontProductionEntity.description.contains(searchKeyword));
		} else if ("1".equals(searchType)) {
			return frontProductionEntity.title.contains(searchKeyword);
		} else {
			return frontProductionEntity.description.contains(searchKeyword);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : getProductionListCnt
	 * 2. ClassName  : FrontProductionJpaRepository.java
	 * 3. Comment    : 프로덕션 리스트 갯수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 06.
	 * </pre>
	 *
	 * @param productionMap
	 * @throws Exception
	 */
	public Long getProductionListCnt(Map<String, Object> productionMap) throws Exception {

		return queryFactory.selectFrom(frontProductionEntity)
				.where(searchProduction(productionMap))
				.fetchCount();
	}

	/**
	 * <pre>
	 * 1. MethodName : getProductionList
	 * 2. ClassName  : FrontProductionJpaRepository.java
	 * 3. Comment    : 프로덕션 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 06.
	 * </pre>
	 *
	 * @param productionMap
	 * @throws Exception
	 */
	public List<FrontProductionDTO> getProductionList(Map<String, Object> productionMap) throws Exception {

		List<FrontProductionEntity> productionList = queryFactory
				.selectFrom(frontProductionEntity)
				.where(searchProduction(productionMap))
				.orderBy(frontProductionEntity.idx.desc())
				.offset(StringUtil.getInt(productionMap.get("jpaStartPage"),0))
				.limit(StringUtil.getInt(productionMap.get("size"),0))
				.fetch();

		List<FrontProductionDTO> productionDtoList = ProductionMapper.INSTANCE.toDtoList(productionList);

		for(int i = 0; i < productionDtoList.size(); i++) {
			productionDtoList.get(i).setRnum(StringUtil.getInt(productionMap.get("startPage"),1)*(StringUtil.getInt(productionMap.get("size"),1))-(2-i));
		}

		return productionDtoList;

	}
}
