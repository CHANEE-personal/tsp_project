package com.tsp.api.portfolio.controller;

import com.tsp.api.portfolio.domain.FrontPortFolioDTO;
import com.tsp.api.portfolio.service.FrontPortFolioJpaApiService;
import com.tsp.common.paging.Paging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.rmi.ServerError;
import java.util.Map;

import static org.springframework.web.client.HttpClientErrorException.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "포트폴리오관련 API")
@RequestMapping("/api/portfolio")
public class FrontPortFolioJpaApiController {
    private final FrontPortFolioJpaApiService frontPortFolioJpaApiService;

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
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping
    public ResponseEntity<Page<FrontPortFolioDTO>> findPortfolioList(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        return ResponseEntity.ok().body(frontPortFolioJpaApiService.findPortfolioList(paramMap, PageRequest.of(paging.getPageNum(), paging.getSize())));
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
            @ApiResponse(code = 200, message = "포트폴리오 상세 조회 성공", response = FrontPortFolioDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}")
    public ResponseEntity<FrontPortFolioDTO> findOnePortfolio(@PathVariable Long idx) {
        return ResponseEntity.ok(frontPortFolioJpaApiService.findOnePortfolio(idx));
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
            @ApiResponse(code = 200, message = "이전 포트폴리오 상세조회 성공", response = FrontPortFolioDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/prev")
    public ResponseEntity<FrontPortFolioDTO> findPrevOnePortfolio(@PathVariable Long idx) {
        return ResponseEntity.ok(frontPortFolioJpaApiService.findPrevOnePortfolio(idx));
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
            @ApiResponse(code = 200, message = "다음 포트폴리오 상세조회 성공", response = FrontPortFolioDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/next")
    public ResponseEntity<FrontPortFolioDTO> findNextOnePortfolio(@PathVariable Long idx) {
        return ResponseEntity.ok(frontPortFolioJpaApiService.findNextOnePortfolio(idx));
    }
}
