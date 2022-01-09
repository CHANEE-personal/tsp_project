package com.tsp.new_tsp_front.api.model.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.tsp.new_tsp_front.api.common.domain.CommonImageEntity;
import com.tsp.new_tsp_front.api.common.domain.QCommonImageEntity;
import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.api.model.domain.FrontModelImageEntity;
import com.tsp.new_tsp_front.common.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_front.api.common.domain.QCommonImageEntity.commonImageEntity;
import static com.tsp.new_tsp_front.api.model.domain.QFrontModelEntity.frontModelEntity;
//import static com.tsp.new_tsp_front.api.model.domain.QFrontModelImageEntity.*;

@Slf4j
@RequiredArgsConstructor
@Repository
public class FrontModelJpaRepository {

	private final JPAQueryFactory queryFactory;
	private final EntityManager em;

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
	 * @throws Exception
	 * @return
	 */
	public long getModelListCnt(Map<String, Object> modelMap) throws Exception {

		return queryFactory.selectFrom(frontModelEntity)
				.where(searchModel(modelMap))
				.fetchCount();
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
	 * @throws Exception
	 * @return
	 */
	public List<FrontModelDTO> getModelList(Map<String, Object> modelMap) throws Exception {
//		List<FrontModelEntity> modelList = queryFactory
//				.selectFrom(frontModelEntity)
//				.orderBy(frontModelEntity.idx.desc())
//				.leftJoin(frontModelImageEntity)
//				.on(frontModelEntity.idx.eq(frontModelImageEntity.typeIdx))
//				.fetchJoin()
//				.where(searchModel(modelMap).and(frontModelEntity.model_main_yn.eq("Y")
//						.and(frontModelImageEntity.imageType.eq("main"))))
//				.offset(StringUtil.getInt(modelMap.get("jpaStartPage"),0))
//				.limit(StringUtil.getInt(modelMap.get("size"),0))
//				.fetch();
		List<FrontModelEntity> modelList = queryFactory
				.selectFrom(frontModelEntity)
				.orderBy(frontModelEntity.idx.desc())
				.leftJoin(frontModelEntity.commonImageEntityList, commonImageEntity)
				.fetchJoin()
				.where(searchModel(modelMap).and(frontModelEntity.model_main_yn.eq("Y")))
				.offset(StringUtil.getInt(modelMap.get("jpaStartPage"),0))
				.limit(StringUtil.getInt(modelMap.get("size"),0))
				.fetch();

		for(int i = 0; i < modelList.size(); i++) {
			modelList.get(i).setRnum(StringUtil.getInt(modelMap.get("startPage"),1)*(StringUtil.getInt(modelMap.get("size"),1))-(2-i));
		}

		return ModelMapper.INSTANCE.toDtoList(modelList);
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
	 * @throws Exception
	 * @return
	 */
	public ConcurrentHashMap<String, Object> getModelInfo(FrontModelEntity existFrontModelEntity) throws Exception {

		ConcurrentHashMap<String, Object> modelMap = new ConcurrentHashMap<>();

		//모델 상세 조회
		FrontModelEntity getModelInfo = queryFactory
				.selectFrom(frontModelEntity)
				.where(frontModelEntity.idx.eq(existFrontModelEntity.getIdx())
						.and(frontModelEntity.visible.eq("Y")))
				.fetchOne();

		//모델 이미지 조회
		List<CommonImageEntity> modelImageList = queryFactory
				.selectFrom(commonImageEntity)
				.where(commonImageEntity.typeIdx.eq(existFrontModelEntity.getIdx()),
						commonImageEntity.visible.eq("Y"),
						commonImageEntity.typeName.eq("model")).fetch();

		modelMap.put("modelInfo", ModelMapper.INSTANCE.toDto(getModelInfo));
		modelMap.put("modelImageList", ModelImageMapper.INSTANCE.toDtoList(modelImageList));

		return modelMap;
	}
}
