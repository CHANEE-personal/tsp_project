package com.tsp.new_tsp_front.api.production.controller;

import com.tsp.new_tsp_front.api.production.domain.FrontProductionDTO;
import com.tsp.new_tsp_front.api.production.domain.FrontProductionEntity;
import com.tsp.new_tsp_front.api.production.service.FrontProductionJpaApiService;
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
@Api(tags = "프로덕션관련 API")
@RequestMapping("/api/production")
public class FrontProductionJpaApiController {
    private final FrontProductionJpaApiService frontProductionJpaApiService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : getProductionList
     * 2. ClassName  : FrontProductionJpaApiController.java
     * 3. Comment    : 프론트 > 프로덕션 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 06.
     * </pre>
     */
    @ApiOperation(value = "프로덕션 조회", notes = "프로덕션을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "프로덕션 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public Map<String, Object> getProductionList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> productionMap = searchCommon.searchCommon(page, paramMap);

        // 리스트 수
        resultMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        resultMap.put("perPageListCnt", ceil((double)this.frontProductionJpaApiService.getProductionCount(productionMap) / page.getSize()));
        // 전체 아이템 수
        resultMap.put("productionListTotalCnt", this.frontProductionJpaApiService.getProductionCount(productionMap));
        resultMap.put("productionList", this.frontProductionJpaApiService.getProductionList(productionMap));

        return resultMap;
    }

    /**
     * <pre>
     * 1. MethodName : getProductionInfo
     * 2. ClassName  : FrontProductionJpaApiController.java
     * 3. Comment    : 프론트 > 프로덕션 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 11.
     * </pre>
     */
    @ApiOperation(value = "프로덕션 상세 조회", notes = "프로덕션을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "프로덕션 상세 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}")
    public FrontProductionDTO getProductionInfo(@PathVariable Integer idx) {
        return this.frontProductionJpaApiService.getProductionInfo(FrontProductionEntity.builder().idx(idx).build());
    }

    /**
     * <pre>
     * 1. MethodName : getPrevProductionEdit
     * 2. ClassName  : FrontProductionJpaController.java
     * 3. Comment    : 이전 프로덕션 상세
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 17.
     * </pre>
     */
    @ApiOperation(value = "이전 프로덕션 상세 조회", notes = "이전 프로덕션을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "이전 프로덕션 상세조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/prev")
    public FrontProductionDTO getPrevProductionEdit(@PathVariable Integer idx) {
        return this.frontProductionJpaApiService.findPrevOneProduction(FrontProductionEntity.builder().idx(idx).build());
    }

    /**
     * <pre>
     * 1. MethodName : getNextProductionEdit
     * 2. ClassName  : FrontProductionJpaController.java
     * 3. Comment    : 다음 프로덕션 상세
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 17.
     * </pre>
     */
    @ApiOperation(value = "다음 프로덕션 상세 조회", notes = "다음 프로덕션을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "다음 프로덕션 상세조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/next")
    public FrontProductionDTO getNextProductionEdit(@PathVariable Integer idx) {
        return this.frontProductionJpaApiService.findNextOneProduction(FrontProductionEntity.builder().idx(idx).build());
    }
}
