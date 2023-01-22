package com.tsp.api.production.controller;

import com.tsp.api.production.domain.FrontProductionDTO;
import com.tsp.api.production.service.FrontProductionJpaApiService;
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
@Api(tags = "프로덕션관련 API")
@RequestMapping("/api/production")
public class FrontProductionJpaApiController {
    private final FrontProductionJpaApiService frontProductionJpaApiService;

    /**
     * <pre>
     * 1. MethodName : findProductionList
     * 2. ClassName  : FrontProductionJpaApiController.java
     * 3. Comment    : 프론트 > 프로덕션 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 06.
     * </pre>
     */
    @ApiOperation(value = "프로덕션 조회", notes = "프로덕션을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "프로덕션 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public ResponseEntity<Page<FrontProductionDTO>> findProductionList(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        return ResponseEntity.ok().body(frontProductionJpaApiService.findProductionList(paramMap, PageRequest.of(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : findOneProduction
     * 2. ClassName  : FrontProductionJpaApiController.java
     * 3. Comment    : 프론트 > 프로덕션 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 11.
     * </pre>
     */
    @ApiOperation(value = "프로덕션 상세 조회", notes = "프로덕션을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "프로덕션 상세 조회 성공", response = FrontProductionDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}")
    public ResponseEntity<FrontProductionDTO> findOneProduction(@PathVariable Long idx) {
        return ResponseEntity.ok(frontProductionJpaApiService.findOneProduction(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneProduction
     * 2. ClassName  : FrontProductionJpaController.java
     * 3. Comment    : 이전 프로덕션 상세
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    @ApiOperation(value = "이전 프로덕션 상세 조회", notes = "이전 프로덕션을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "이전 프로덕션 상세조회 성공", response = FrontProductionDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/prev")
    public ResponseEntity<FrontProductionDTO> findPrevOneProduction(@PathVariable Long idx) {
        return ResponseEntity.ok(frontProductionJpaApiService.findPrevOneProduction(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneProduction
     * 2. ClassName  : FrontProductionJpaController.java
     * 3. Comment    : 다음 프로덕션 상세
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    @ApiOperation(value = "다음 프로덕션 상세 조회", notes = "다음 프로덕션을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "다음 프로덕션 상세조회 성공", response = FrontProductionDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/next")
    public ResponseEntity<FrontProductionDTO> findNextOneProduction(@PathVariable Long idx) {
        return ResponseEntity.ok(frontProductionJpaApiService.findNextOneProduction(idx));
    }
}
