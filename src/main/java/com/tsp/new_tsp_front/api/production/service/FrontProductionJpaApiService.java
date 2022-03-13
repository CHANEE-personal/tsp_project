package com.tsp.new_tsp_front.api.production.service;

import com.tsp.new_tsp_front.api.production.domain.FrontProductionDTO;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity;
import com.tsp.new_tsp_front.api.production.service.impl.FrontProductionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class FrontProductionJpaApiService {

	private final FrontProductionJpaRepository frontProductionJpaRepository;

	/**
	 * <pre>
	 * 1. MethodName : getProductionListCnt
	 * 2. ClassName  : FrontProductionJpaService.java
	 * 3. Comment    : 프론트 > 프로덕션 수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 06.
	 * </pre>
	 *
	 * @param productionMap
	 */
	public Long getProductionListCnt(ConcurrentHashMap<String, Object> productionMap) {
		return frontProductionJpaRepository.getProductionListCnt(productionMap);
	}

	/**
	 * <pre>
	 * 1. MethodName : getProductionList
	 * 2. ClassName  : FrontProductionJpaService.java
	 * 3. Comment    : 프론트 > 프로덕션 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 06.
	 * </pre>
	 *
	 * @param productionMap
	 */
	public List<FrontProductionDTO> getProductionList(ConcurrentHashMap<String, Object> productionMap) {
		return frontProductionJpaRepository.getProductionList(productionMap);
	}

	/**
	 * <pre>
	 * 1. MethodName : getProductionInfo
	 * 2. ClassName  : FrontProductionJpaService.java
	 * 3. Comment    : 프론트 > 프로덕션 상세 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 11.
	 * </pre>
	 *
	 * @param frontProductionEntity
	 */
	public FrontProductionDTO getProductionInfo(FrontProductionEntity frontProductionEntity) {
		return frontProductionJpaRepository.getProductionInfo(frontProductionEntity);
	}
}
