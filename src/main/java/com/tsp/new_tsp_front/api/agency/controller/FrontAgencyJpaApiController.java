package com.tsp.new_tsp_front.api.agency.controller;

import com.tsp.new_tsp_front.api.agency.domain.FrontAgencyDTO;
import com.tsp.new_tsp_front.api.agency.service.FrontAgencyJpaService;
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

@RestController
@RequiredArgsConstructor
@Api(tags = "Agency관련 API")
@RequestMapping("/api/agency")
public class FrontAgencyJpaApiController {
    private final FrontAgencyJpaService frontAgencyJpaService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : findAgencyList
     * 2. ClassName  : FrontAgencyJpaApiController.java
     * 3. Comment    : 프론트 > Agency 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 24.
     * </pre>
     */
    @ApiOperation(value = "Agency 조회", notes = "Agency를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Agency 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public Map<String, Object> findAgencyList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> agencyMap = searchCommon.searchCommon(page, paramMap);

        // 리스트 수
        resultMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        resultMap.put("perPageListCnt", ceil((double)this.frontAgencyJpaService.findAgencyCount(agencyMap) / page.getSize()));
        // 전체 아이템 수
        resultMap.put("agencyListTotalCnt", this.frontAgencyJpaService.findAgencyList(agencyMap));
        resultMap.put("agencyList", this.frontAgencyJpaService.findAgencyList(agencyMap));

        return resultMap;
    }

    /**
     * <pre>
     * 1. MethodName : findOneAgency
     * 2. ClassName  : FrontAgencyJpaApiController.java
     * 3. Comment    : 프론트 > Agency 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 24.
     * </pre>
     */
    @ApiOperation(value = "Agency 상세 조회", notes = "Agency를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Agency 상세 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}")
    public FrontAgencyDTO findOneAgency(@PathVariable Long idx) {
        return this.frontAgencyJpaService.findOneAgency(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneAgency
     * 2. ClassName  : FrontAgencyJpaApiController.java
     * 3. Comment    : 프론트 > 이전 Agency 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 17.
     * </pre>
     */
    @ApiOperation(value = "이전 Agency 상세 조회", notes = "이전 Agency를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "이전 Agency 상세 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}/prev")
    public FrontAgencyDTO findPrevOneAgency(@PathVariable Long idx) {
        return this.frontAgencyJpaService.findPrevOneAgency(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneAgency
     * 2. ClassName  : FrontAgencyJpaApiController.java
     * 3. Comment    : 프론트 > 다음 Agency 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 17.
     * </pre>
     */
    @ApiOperation(value = "다음 Agency 상세 조회", notes = "다음 Agency를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "다음 Agency 상세 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}/next")
    public FrontAgencyDTO findNextOneAgency(@PathVariable Long idx) {
        return this.frontAgencyJpaService.findNextOneAgency(idx);
    }

    /**
     * <pre>
     * 1. MethodName : favoriteAgency
     * 2. ClassName  : FrontAgencyJpaApiController.java
     * 3. Comment    : 프론트 > Agency 좋아요 처리
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 24.
     * </pre>
     */
    @ApiOperation(value = "Agency 좋아요 처리", notes = "Agency를 좋아요 처리한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Agency 좋아요 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping(value = "/{idx}/like")
    public Integer favoriteAgency(@PathVariable Long idx) {
        return this.frontAgencyJpaService.favoriteAgency(idx);
    }
}
