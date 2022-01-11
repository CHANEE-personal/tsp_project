package com.tsp.new_tsp_front.api.portfolio.controller;

import com.tsp.new_tsp_front.api.portfolio.FrontPortFolioJpaApiService;
import com.tsp.new_tsp_front.api.portfolio.domain.FrontPortFolioDTO;
import com.tsp.new_tsp_front.common.SearchCommon;
import com.tsp.new_tsp_front.common.paging.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@Api(tags = "포트폴리오관련 API")
@RequestMapping("/api/portfolio")
public class FrontPortFolioJpaApiController {

	private final FrontPortFolioJpaApiService frontPortFolioJpaApiService;
	private final SearchCommon searchCommon;

	/**
	 * <pre>
	 * 1. MethodName : lists
	 * 2. ClassName  : FrontPortFolioJpaApiController.java
	 * 3. Comment    : 프론트 > 포트폴리오 조회
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2022. 01. 11.
	 * </pre>
	 *
	 * @param paramMap
	 * @param page
	 * @throws Exception
	 */
	@ApiOperation(value = "포트폴리오 조회", notes = "포트폴리오를 조회한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = Map.class),
			@ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
			@ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
	})
	@GetMapping(value = "/lists")
	public ConcurrentHashMap<String, Object> getPortFolioList(@RequestParam(required = false) Map<String, Object> paramMap,
													   Page page) throws Exception {
		ConcurrentHashMap<String, Object> resultMap = new ConcurrentHashMap<>();
		// 페이징 및 검색
		ConcurrentHashMap<String, Object> portFolioMap = searchCommon.searchCommon(page, paramMap);

		long portFolioListCnt = this.frontPortFolioJpaApiService.getPortFolioListCnt(portFolioMap);

		List<FrontPortFolioDTO> portFolioList = new ArrayList<>();

		if(portFolioListCnt > 0) {
			portFolioList = this.frontPortFolioJpaApiService.getPortFolioList(portFolioMap);
		}

		// 리스트 수
		resultMap.put("pageSize", page.getSize());
		// 전체 페이지 수
		resultMap.put("perPageListCnt", Math.ceil((portFolioListCnt-1)/page.getSize()+1));
		// 전체 아이템 수
		resultMap.put("portFolioListTotalCnt", portFolioListCnt);
		resultMap.put("portFolioList", portFolioList);

		return resultMap;
	}
}
