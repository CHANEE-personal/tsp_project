package com.tsp.new_tsp_front.api.model.controller;

import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.api.model.service.FrontModelJpaApiService;
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
@RequestMapping("/api/model")
public class FrontModelJpaApiController {

	private final FrontModelJpaApiService frontModelJpaApiService;
	private final SearchCommon searchCommon;

	/**
	 * <pre>
	 * 1. MethodName : lists/{categoryCd}
	 * 2. ClassName  : FrontModelJpaApiController.java
	 * 3. Comment    : 프론트 > 모델 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 02.
	 * </pre>
	 *
	 * @param categoryCd
	 * @param paramMap
	 * @param page
	 * @throws Exception
	 */
	@ApiOperation(value = "모델 조회", notes = "모델을 조회한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@GetMapping(value = "/lists/{categoryCd}")
	public ConcurrentHashMap getModelList(@PathVariable("categoryCd") Integer categoryCd,
										  @RequestParam(required = false) Map<String, Object> paramMap,
										  Page page) throws Exception {
		ConcurrentHashMap resultMap = new ConcurrentHashMap();
		// 페이징 및 검색
		ConcurrentHashMap<String, Object> modelMap = searchCommon.searchCommon(page, paramMap);
		modelMap.put("categoryCd", categoryCd);

		long modelListCnt = this.frontModelJpaApiService.getModelListCnt(modelMap);

		List<FrontModelEntity> modelList = new ArrayList<>();

		if (modelListCnt > 0) {
			modelList = this.frontModelJpaApiService.getModelList(modelMap);
		}

		// 리스트 수
		resultMap.put("pageSize", page.getSize());
		// 전체 페이지 수
		resultMap.put("perPageListCnt", Math.ceil((modelListCnt - 1) / page.getSize() + 1));
		// 전체 아이템 수
		resultMap.put("modelListTotalCnt", modelListCnt);

		resultMap.put("modelList", modelList);

		return resultMap;
	}
}
