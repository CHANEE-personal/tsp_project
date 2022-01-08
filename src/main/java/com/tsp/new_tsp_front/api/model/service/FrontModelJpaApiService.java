package com.tsp.new_tsp_front.api.model.service;

import com.tsp.new_tsp_front.api.common.domain.CommonImageDTO;
import com.tsp.new_tsp_front.api.common.domain.CommonImageEntity;
import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.api.model.service.impl.FrontModelJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class FrontModelJpaApiService {

	private final FrontModelJpaRepository frontModelJpaRepository;

	/**
	 * <pre>
	 * 1. MethodName : getModelListCnt
	 * 2. ClassName  : FrontModelJpaApiService.java
	 * 3. Comment    : 프론트 > 모델 수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 02.
	 * </pre>
	 *
	 * @param modelMap
	 * @throws Exception
	 */
	public long getModelListCnt(ConcurrentHashMap modelMap) throws Exception {
		return frontModelJpaRepository.getModelListCnt(modelMap);
	}

	/**
	 * <pre>
	 * 1. MethodName : getModelList
	 * 2. ClassName  : FrontModelJpaApiService.java
	 * 3. Comment    : 프론트 > 모델 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 02.
	 * </pre>
	 *
	 * @param modelMap
	 * @throws Exception
	 */
	public List<FrontModelDTO> getModelList(ConcurrentHashMap modelMap) throws Exception {
		return frontModelJpaRepository.getModelList(modelMap);
	}

	/**
	 * <pre>
	 * 1. MethodName : getModelImageListCnt
	 * 2. ClassName  : FrontModelJpaApiService.java
	 * 3. Comment    : 프론트 > 모델 이미지 수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 02.
	 * </pre>
	 *
	 * @param modelMap
	 * @throws Exception
	 */
	public long getModelImageListCnt(ConcurrentHashMap modelMap) throws Exception {
		return frontModelJpaRepository.getModelImageListCnt(modelMap);
	}

	/**
	 * <pre>
	 * 1. MethodName : getModelImageList
	 * 2. ClassName  : FrontModelJpaApiService.java
	 * 3. Comment    : 프론트 > 모델 이미지 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 02.
	 * </pre>
	 *
	 * @param modelMap
	 * @throws Exception
	 */
	public List<CommonImageDTO> getModelImageList(ConcurrentHashMap modelMap) throws Exception {
		return frontModelJpaRepository.getModelImageList(modelMap);
	}
}
