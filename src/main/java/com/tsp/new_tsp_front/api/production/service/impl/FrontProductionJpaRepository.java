package com.tsp.new_tsp_front.api.production.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_front.api.common.domain.CommonImageEntity;
import com.tsp.new_tsp_front.api.model.service.impl.ModelImageMapper;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionDTO;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity;
import com.tsp.new_tsp_front.common.utils.StringUtil;
import com.tsp.new_tsp_front.exception.ApiExceptionType;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_front.api.common.domain.QCommonImageEntity.commonImageEntity;
import static com.tsp.new_tsp_front.api.production.domain.QFrontProductionEntity.frontProductionEntity;

@Repository
@RequiredArgsConstructor
public class FrontProductionJpaRepository {

	private final JPAQueryFactory queryFactory;

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
	 */
	public Long getProductionListCnt(Map<String, Object> productionMap) {

		try {
			return queryFactory.selectFrom(frontProductionEntity)
					.where(searchProduction(productionMap))
					.fetchCount();
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_PRODUCTION_LIST);
		}
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
	 */
	public List<FrontProductionDTO> getProductionList(Map<String, Object> productionMap) {

		try {
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
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_PRODUCTION_LIST);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : getProductionInfo
	 * 2. ClassName  : FrontProductionJpaRepository.java
	 * 3. Comment    : 프로덕션 상세 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 12.
	 * </pre>
	 *
	 * @param existFrontProductionEntity
	 */
	public ConcurrentHashMap<String, Object> getProductionInfo(FrontProductionEntity existFrontProductionEntity) {
		try {
			ConcurrentHashMap<String, Object> productionMap = new ConcurrentHashMap<>();

			//프로덕션 상세 조회
			FrontProductionEntity getProductionInfo = queryFactory
					.selectFrom(frontProductionEntity)
					.where(frontProductionEntity.idx.eq(existFrontProductionEntity.getIdx())
							.and(frontProductionEntity.visible.eq("Y")))
					.fetchOne();

			//프로덕션 이미지 조회
			List<CommonImageEntity> productionImageList = queryFactory
					.selectFrom(commonImageEntity)
					.where(commonImageEntity.typeIdx.eq(existFrontProductionEntity.getIdx())
							.and(commonImageEntity.visible.eq("Y")
									.and(commonImageEntity.typeName.eq("production"))))
					.fetch();

			productionMap.put("productionInfo", ProductionMapper.INSTANCE.toDto(getProductionInfo));
			productionMap.put("productionImageList", ModelImageMapper.INSTANCE.toDtoList(productionImageList));

			return productionMap;
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_PRODUCTION);
		}
	}
}
