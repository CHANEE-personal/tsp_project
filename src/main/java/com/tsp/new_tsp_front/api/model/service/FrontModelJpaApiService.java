package com.tsp.new_tsp_front.api.model.service;

import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.api.model.service.impl.FrontModelJpaRepository;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_front.exception.ApiExceptionType.NOT_FOUND_MODEL;
import static com.tsp.new_tsp_front.exception.ApiExceptionType.NOT_FOUND_MODEL_LIST;

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
	public int getModelCount(Map<String, Object> modelMap) throws TspException {
		try {
			return frontModelJpaRepository.getModelCount(modelMap);
		} catch (Exception e) {
			throw new TspException(NOT_FOUND_MODEL_LIST, e);
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
	 */
	@Transactional(readOnly = true)
	public List<FrontModelDTO> getModelList(Map<String, Object> modelMap) throws TspException {
		try {
			return frontModelJpaRepository.getModelList(modelMap);
		} catch (Exception e) {
			throw new TspException(NOT_FOUND_MODEL_LIST, e);
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
	 */
	@Transactional(readOnly = true)
	public FrontModelDTO getModelInfo(FrontModelEntity frontModelEntity) throws TspException {
		try {
			return this.frontModelJpaRepository.getModelInfo(frontModelEntity);
		} catch (Exception e) {
			throw new TspException(NOT_FOUND_MODEL, e);
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
	public List<FrontModelDTO> getMainModelList() throws TspException {
		try {
			return this.frontModelJpaRepository.getMainModelList();
		} catch (Exception e) {
			throw new TspException(NOT_FOUND_MODEL_LIST, e);
		}
	}
}
