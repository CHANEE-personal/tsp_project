package com.tsp.new_tsp_front.api.portfolio;

import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioDTO;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioEntity;
import com.tsp.new_tsp_front.api.portfolio.service.impl.FrontPortFolioJpaRepository;
import com.tsp.new_tsp_front.exception.ApiExceptionType;
import com.tsp.new_tsp_front.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class FrontPortFolioJpaApiService {

	private final FrontPortFolioJpaRepository frontPortFolioJpaRepository;

	/**
	 * <pre>
	 * 1. MethodName : getPortFolioListCnt
	 * 2. ClassName  : FrontPortFolioJpaApiService.java
	 * 3. Comment    : 프론트 > 포트폴리오 수 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 11.
	 * </pre>
	 *
	 * @param portFolioMap
	 */
	public long getPortFolioListCnt(ConcurrentHashMap portFolioMap) {
		return frontPortFolioJpaRepository.getPortFolioListCnt(portFolioMap);
	}

	/**
	 * <pre>
	 * 1. MethodName : getPortFolio
	 * 2. ClassName  : FrontPortFolioJpaApiService.java
	 * 3. Comment    : 프론트 > 포트폴리오 리스트 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 11.
	 * </pre>
	 *
	 * @param portFolioMap
	 */
	public List<FrontPortFolioDTO> getPortFolioList(ConcurrentHashMap portFolioMap) {
		return frontPortFolioJpaRepository.getPortFolioList(portFolioMap);
	}

	/**
	 * <pre>
	 * 1. MethodName : getPortFolioInfo
	 * 2. ClassName  : FrontPortFolioJpaApiService.java
	 * 3. Comment    : 프론트 > 포트폴리오 상세 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 12.
	 * </pre>
	 *
	 * @param frontPortFolioEntity
	 */
	public FrontPortFolioDTO getPortFolioInfo(FrontPortFolioEntity frontPortFolioEntity) {
		return frontPortFolioJpaRepository.getPortFolioInfo(frontPortFolioEntity);
	}
}
