package com.tsp.new_tsp_front.api.model.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.common.utils.StringUtil;
import com.tsp.new_tsp_front.exception.ApiExceptionType;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.api.common.domain.QCommonImageEntity.commonImageEntity;
import static com.tsp.new_tsp_front.api.model.domain.QFrontModelEntity.frontModelEntity;

@Slf4j
@RequiredArgsConstructor
@Repository
public class FrontModelJpaRepository {

	private final JPAQueryFactory queryFactory;

	private BooleanExpression searchModel(Map<String, Object> modelMap) {
		String searchType = StringUtil.getString(modelMap.get("searchType"),"");
		String searchKeyword = StringUtil.getString(modelMap.get("searchKeyword"),"");
		Integer categoryCd = StringUtil.getInt(modelMap.get("categoryCd"),0);

		if ("0".equals(searchType)) {
			return frontModelEntity.modelKorName.contains(searchKeyword)
					.or(frontModelEntity.modelEngName.contains(searchKeyword)
							.or(frontModelEntity.modelDescription.contains(searchKeyword)))
					.and(frontModelEntity.categoryCd.eq(categoryCd));
		} else if ("1".equals(searchType)) {
			return frontModelEntity.modelKorName.contains(searchKeyword)
					.or(frontModelEntity.modelEngName.contains(searchKeyword))
					.and(frontModelEntity.categoryCd.eq(categoryCd));
		} else {
			if(!"".equals(searchKeyword)) {
				return frontModelEntity.modelDescription.contains(searchKeyword).and(frontModelEntity.categoryCd.eq(categoryCd));
			} else {
				return frontModelEntity.categoryCd.eq(categoryCd);
			}
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : getModelListCnt
	 * 2. ClassName  : FrontModelJpaRepository.java
	 * 3. Comment    : 프론트 모델 리스트 갯수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 02.
	 * </pre>
	 *
	 * @param modelMap
	 * @return
	 */
	public Long getModelListCnt(Map<String, Object> modelMap) {

		try {
			return queryFactory.selectFrom(frontModelEntity)
					.where(searchModel(modelMap))
					.fetchCount();
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_MODEL_LIST);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : getModelList
	 * 2. ClassName  : FrontModelJpaRepository.java
	 * 3. Comment    : 프론트 모델 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 02.
	 * </pre>
	 *
	 * @param modelMap
	 * @return
	 */
	public List<FrontModelDTO> getModelList(Map<String, Object> modelMap) {

		try {
			List<FrontModelEntity> modelList = queryFactory
					.selectFrom(frontModelEntity)
					.orderBy(frontModelEntity.idx.desc())
					.leftJoin(frontModelEntity.commonImageEntityList, commonImageEntity)
					.fetchJoin()
					.where(searchModel(modelMap).and(frontModelEntity.modelMainYn.eq("Y")))
					.offset(StringUtil.getInt(modelMap.get("jpaStartPage"),0))
					.limit(StringUtil.getInt(modelMap.get("size"),0))
					.fetch();

			for(int i = 0; i < modelList.size(); i++) {
				modelList.get(i).setRnum(StringUtil.getInt(modelMap.get("startPage"),1)*(StringUtil.getInt(modelMap.get("size"),1))-(2-i));
			}

			return ModelMapper.INSTANCE.toDtoList(modelList);
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_MODEL_LIST);
		}
	}

	/**
	 * <pre>
	 * 1. MethodName : getModelInfo
	 * 2. ClassName  : FrontModelJpaRepository.java
	 * 3. Comment    : 프론트 모델 상세 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 09.
	 * </pre>
	 *
	 * @param existFrontModelEntity
	 * @return
	 */
	public FrontModelDTO getModelInfo(FrontModelEntity existFrontModelEntity) {

		try {
			//모델 상세 조회
			FrontModelEntity getModelInfo = queryFactory
					.selectFrom(frontModelEntity)
					.orderBy(frontModelEntity.idx.desc())
					.leftJoin(frontModelEntity.commonImageEntityList, commonImageEntity)
					.fetchJoin()
					.where(frontModelEntity.idx.eq(existFrontModelEntity.getIdx())
							.and(frontModelEntity.visible.eq("Y"))
							.and(commonImageEntity.typeName.eq("model")))
					.fetchOne();

//			//모델 이미지 조회
//			List<CommonImageEntity> modelImageList = queryFactory
//					.selectFrom(commonImageEntity)
//					.where(commonImageEntity.typeIdx.eq(existFrontModelEntity.getIdx())
//							.and(commonImageEntity.visible.eq("Y"))
//							.and(commonImageEntity.typeName.eq("model"))).fetch();

//			modelMap.put("modelInfo", ModelMapper.INSTANCE.toDto(getModelInfo));
//			modelMap.put("modelImageList", ModelImageMapper.INSTANCE.toDtoList(modelImageList));

			return ModelMapper.INSTANCE.toDto(getModelInfo);
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_MODEL);
		}
	}
}
