package com.tsp.api.model.agency.controller;

import com.tsp.api.model.agency.service.FrontAgencyJpaService;
import com.tsp.api.model.domain.agency.FrontAgencyDTO;
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

@RestController
@RequiredArgsConstructor
@Api(tags = "소속사 관련 API")
@RequestMapping("/api/agency")
public class FrontAgencyJpaApiController {
    private final FrontAgencyJpaService frontAgencyJpaService;

    /**
     * <pre>
     * 1. MethodName : findAgencyList
     * 2. ClassName  : FrontAgencyJpaApiController.java
     * 3. Comment    : 프론트 > Agency 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @ApiOperation(value = "Agency 조회", notes = "Agency 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Agency 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping
    public ResponseEntity<Page<FrontAgencyDTO>> findAgencyList(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        return ResponseEntity.ok().body(frontAgencyJpaService.findAgencyList(paramMap, PageRequest.of(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : findOneAgency
     * 2. ClassName  : FrontAgencyJpaApiController.java
     * 3. Comment    : 프론트 > Agency 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @ApiOperation(value = "Agency 상세 조회", notes = "Agency 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Agency 상세 조회 성공", response = FrontAgencyDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}")
    public ResponseEntity<FrontAgencyDTO> findOneAgency(@PathVariable Long idx) {
        return ResponseEntity.ok(frontAgencyJpaService.findOneAgency(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneAgency
     * 2. ClassName  : FrontAgencyJpaApiController.java
     * 3. Comment    : 프론트 > 이전 Agency 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    @ApiOperation(value = "이전 Agency 상세 조회", notes = "이전 Agency 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "이전 Agency 상세 조회 성공", response = FrontAgencyDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}/prev")
    public ResponseEntity<FrontAgencyDTO> findPrevOneAgency(@PathVariable Long idx) {
        return ResponseEntity.ok(frontAgencyJpaService.findPrevOneAgency(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneAgency
     * 2. ClassName  : FrontAgencyJpaApiController.java
     * 3. Comment    : 프론트 > 다음 Agency 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    @ApiOperation(value = "다음 Agency 상세 조회", notes = "다음 Agency 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "다음 Agency 상세 조회 성공", response = FrontAgencyDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}/next")
    public ResponseEntity<FrontAgencyDTO> findNextOneAgency(@PathVariable Long idx) {
        return ResponseEntity.ok(frontAgencyJpaService.findNextOneAgency(idx));
    }

    /**
     * <pre>
     * 1. MethodName : favoriteAgency
     * 2. ClassName  : FrontAgencyJpaApiController.java
     * 3. Comment    : 프론트 > Agency 좋아요 처리
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @ApiOperation(value = "Agency 좋아요 처리", notes = "Agency 좋아요 처리한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Agency 좋아요 성공", response = Integer.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping(value = "/{idx}/like")
    public ResponseEntity<Integer> favoriteAgency(@PathVariable Long idx) {
        return ResponseEntity.ok(frontAgencyJpaService.favoriteAgency(idx));
    }
}
