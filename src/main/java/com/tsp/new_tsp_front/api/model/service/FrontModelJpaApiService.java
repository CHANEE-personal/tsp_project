package com.tsp.new_tsp_front.api.model.service;

import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.api.model.service.impl.FrontModelJpaRepository;
import com.tsp.new_tsp_front.exception.ApiExceptionType;
import com.tsp.new_tsp_front.exception.TspException;
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
	 */
	public Long getModelListCnt(ConcurrentHashMap<String, Object> modelMap) {
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
	 */
	public List<FrontModelDTO> getModelList(ConcurrentHashMap<String, Object> modelMap) {
		return frontModelJpaRepository.getModelList(modelMap);
	}

	/**
	 * <pre>
	 * 1. MethodName : getModelInfo
	 * 2. ClassName  : FrontModelJpaApiService.java
	 * 3. Comment    : 프론트 > 모델 상세 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 09.
	 * </pre>
	 *
	 * @param frontModelEntity
	 */
	public FrontModelDTO getModelInfo(FrontModelEntity frontModelEntity) {
		return this.frontModelJpaRepository.getModelInfo(frontModelEntity);
	}

	/**
	 * <pre>
	 * 1. MethodName : getMainModelListCnt
	 * 2. ClassName  : FrontModelJpaApiService.java
	 * 3. Comment    : 프론트 > 메인 모델 수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 03. 27.
	 * </pre>
	 *
	 * @param modelMap
	 */
	public Long getMainModelListCnt(ConcurrentHashMap<String, Object> modelMap) {
		return this.frontModelJpaRepository.getMainModelListCnt(modelMap);
	}

	/**
	 * <pre>
	 * 1. MethodName : getMainModelList
	 * 2. ClassName  : FrontModelJpaApiService.java
	 * 3. Comment    : 프론트 > 메인 모델 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 03. 27.
	 * </pre>
	 *
	 * @param modelMap
	 */
	public List<FrontModelDTO> getMainModelList(ConcurrentHashMap<String, Object> modelMap) {
		return this.frontModelJpaRepository.getMainModelList(modelMap);
	}
}
