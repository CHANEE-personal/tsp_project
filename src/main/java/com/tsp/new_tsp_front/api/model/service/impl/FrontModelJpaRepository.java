package com.tsp.new_tsp_front.api.model.service.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.tsp.new_tsp_front.api.common.domain.CommonImageDTO;
import com.tsp.new_tsp_front.api.common.domain.CommonImageEntity;
import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.common.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_front.api.common.domain.QCommonImageEntity.commonImageEntity;
import static com.tsp.new_tsp_front.api.model.domain.QFrontModelEntity.frontModelEntity;

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

		System.out.println("===modelMap==="+modelMap);
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
	public Long getModelListCnt(Map<String, Object> modelMap) throws Exception {

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
		List<FrontModelEntity> modelList = queryFactory
				.selectFrom(frontModelEntity)
				.orderBy(frontModelEntity.idx.desc())
				.leftJoin(frontModelEntity.newCommonImageJpaDTO, commonImageEntity)
				.fetchJoin()
				.where(searchModel(modelMap).and(commonImageEntity.imageType.eq("main")))
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
	 * 1. MethodName : getModelImageListCnt
	 * 2. ClassName  : FrontModelJpaRepository.java
	 * 3. Comment    : 프론트 모델 이미지 리스트 개수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 02.
	 * </pre>
	 *
	 * @param modelMap
	 * @throws Exception
	 * @return
	 */
	public Long getModelImageListCnt(Map<String, Object> modelMap) throws Exception {

		return queryFactory.selectFrom(commonImageEntity)
				.where(commonImageEntity.typeName.eq("model").
						and(commonImageEntity.imageType.eq("main")))
				.fetchCount();
	}

	/**
	 * <pre>
	 * 1. MethodName : getModelImageList
	 * 2. ClassName  : FrontModelJpaRepository.java
	 * 3. Comment    : 프론트 모델 이미지 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 02.
	 * </pre>
	 *
	 * @param modelMap
	 * @throws Exception
	 * @return
	 */
	public List<CommonImageDTO> getModelImageList(ConcurrentHashMap modelMap) throws Exception {
		List<CommonImageEntity> modelImageList = queryFactory
				.selectFrom(commonImageEntity)
				.orderBy(commonImageEntity.idx.desc())
				.where(commonImageEntity.typeName.eq("model").and(commonImageEntity.imageType.eq("main")))
				.offset(StringUtil.getInt(modelMap.get("jpaStartPage"),0))
				.limit(StringUtil.getInt(modelMap.get("size"),0))
				.fetch();

		for(int i = 0; i < modelImageList.size(); i++) {
			modelImageList.get(i).setRnum(StringUtil.getInt(modelMap.get("startPage"),1)*(StringUtil.getInt(modelMap.get("size"),1))-(2-i));
		}

		return ModelImageMapper.INSTANCE.toDtoList(modelImageList);
	}
}
