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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.rmi.ServerError;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.ceil;
import static org.springframework.web.client.HttpClientErrorException.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "포트폴리오관련 API")
@RequestMapping("/api/portfolio")
public class FrontPortFolioJpaApiController {
    private final FrontPortFolioJpaApiService frontPortFolioJpaApiService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : findPortfolioList
     * 2. ClassName  : FrontPortFolioJpaApiController.java
     * 3. Comment    : 프론트 > 포트폴리오 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 11.
     * </pre>
     */
    @ApiOperation(value = "포트폴리오 조회", notes = "포트폴리오를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "포트폴리오 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public Map<String, Object> findPortfolioList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> portfolioMap = searchCommon.searchCommon(page, paramMap);

        // 리스트 수
        resultMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        resultMap.put("perPageListCnt", ceil((double) this.frontPortFolioJpaApiService.findPortfolioCount(portfolioMap) / page.getSize()));
        // 전체 아이템 수
        resultMap.put("portFolioListTotalCnt", this.frontPortFolioJpaApiService.findPortfolioList(portfolioMap).size());
        resultMap.put("portFolioList", this.frontPortFolioJpaApiService.findPortfolioList(portfolioMap));
        return resultMap;
    }

    /**
     * <pre>
     * 1. MethodName : findOnePortfolio
     * 2. ClassName  : FrontPortFolioJpaApiController.java
     * 3. Comment    : 프론트 > 포트폴리오 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 11.
     * </pre>
     */
    @ApiOperation(value = "포트폴리오 상세 조회", notes = "포트폴리오를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "포트폴리오 상세 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}")
    public FrontPortFolioDTO findOnePortfolio(@PathVariable Long idx) {
        return this.frontPortFolioJpaApiService.findOnePortfolio(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOnePortfolio
     * 2. ClassName  : FrontProductionJpaController.java
     * 3. Comment    : 이전 포트폴리오 상세
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    @ApiOperation(value = "이전 포트폴리오 상세 조회", notes = "이전 포트폴리오를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "이전 포트폴리오 상세조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/prev")
    public FrontPortFolioDTO findPrevOnePortfolio(@PathVariable Long idx) {
        return this.frontPortFolioJpaApiService.findPrevOnePortfolio(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOnePortfolio
     * 2. ClassName  : FrontPortfolioJpaController.java
     * 3. Comment    : 다음 포트폴리오 상세
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    @ApiOperation(value = "다음 포트폴리오 상세 조회", notes = "다음 포트폴리오를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "다음 포트폴리오 상세조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/next")
    public FrontPortFolioDTO findNextOnePortfolio(@PathVariable Long idx) {
        return this.frontPortFolioJpaApiService.findNextOnePortfolio(idx);
    }
}
