package com.tsp.new_tsp_front.api.model.service;

import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.api.model.service.impl.FrontModelJpaRepository;
import com.tsp.new_tsp_front.exception.ApiExceptionType;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class FrontModelJpaApiService {

	private final FrontModelJpaRepository frontModelJpaRepository;

	/**
	 * <pre>
	 * 1. MethodName : getModelCount
	 * 2. ClassName  : FrontModelJpaApiService.java
	 * 3. Comment    : 프론트 > 모델 리스트 갯수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 02.
	 * </pre>
	 *
	 */
	public Long getModelCount(Map<String, Object> modelMap) throws Exception {
		try {
			return frontModelJpaRepository.getModelCount(modelMap);
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_MODEL_LIST);
		}
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
	@Transactional(readOnly = true)
	public List<FrontModelDTO> getModelList(ConcurrentHashMap<String, Object> modelMap) throws Exception {
		try {
			return frontModelJpaRepository.getModelList(modelMap);
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_MODEL_LIST);
		}
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
	@Transactional(readOnly = true)
	public FrontModelDTO getModelInfo(FrontModelEntity frontModelEntity) throws Exception {
		try {
			return this.frontModelJpaRepository.getModelInfo(frontModelEntity);
		} catch (Exception e) {
			throw new TspException(ApiExceptionType.NOT_FOUND_MODEL);
		}
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
	 */
	@Transactional(readOnly = true)
	public List<FrontModelDTO> getMainModelList() throws Exception {
		return this.frontModelJpaRepository.getMainModelList();
	}
}
