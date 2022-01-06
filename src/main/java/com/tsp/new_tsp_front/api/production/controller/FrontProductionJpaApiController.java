package com.tsp.new_tsp_front.api.production.controller;

import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionDTO;
import com.tsp.new_tsp_front.api.production.service.FrontProductionJpaApiService;
import com.tsp.new_tsp_front.common.SearchCommon;
import com.tsp.new_tsp_front.common.paging.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/production")
public class FrontProductionJpaApiController {

	private final FrontProductionJpaApiService frontProductionJpaApiService;
	private final SearchCommon searchCommon;

	/**
	 * <pre>
	 * 1. MethodName : lists/{categoryCd}
	 * 2. ClassName  : FrontProductionJpaApiController.java
	 * 3. Comment    : 프론트 > 프로덕션 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 06.
	 * </pre>
	 *
	 * @param categoryCd
	 * @param paramMap
	 * @param page
	 * @throws Exception
	 */
	@ApiOperation(value = "프로덕션 조회", notes = "프로덕션을 조회한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@GetMapping(value = "/lists/{categoryCd}")
	public ConcurrentHashMap<String, Object> getProductionList(@PathVariable("categoryCd") Integer categoryCd,
														  @RequestParam(required = false) Map<String, Object> paramMap,
														  Page page) throws Exception {
		ConcurrentHashMap<String, Object> resultMap = new ConcurrentHashMap<>();
		// 페이징 및 검색
		ConcurrentHashMap<String, Object> modelMap = searchCommon.searchCommon(page, paramMap);
		modelMap.put("categoryCd", categoryCd);

		Long productionListCnt = this.frontProductionJpaApiService.getProductionListCnt(modelMap);

		List<FrontProductionDTO> productionList = new ArrayList<>();

		if (productionListCnt > 0) {
			productionList = this.frontProductionJpaApiService.getProductionList(modelMap);
		}

		// 리스트 수
		resultMap.put("pageSize", page.getSize());
		// 전체 페이지 수
		resultMap.put("perPageListCnt", Math.ceil((productionListCnt-1)/page.getSize()+1));
		// 전체 아이템 수
		resultMap.put("productionListTotalCnt", productionListCnt);

		resultMap.put("productionList", productionList);

		return resultMap;
	}
}
