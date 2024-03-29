package com.tsp.api.notice.controller;

import com.tsp.api.notice.domain.FrontNoticeDTO;
import com.tsp.api.notice.service.FrontNoticeJpaService;
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
@Api(tags = "공지사항관련 API")
@RequestMapping("/api/notice")
public class FrontNoticeJpaApiController {
    private final FrontNoticeJpaService frontNoticeJpaService;

    /**
     * <pre>
     * 1. MethodName : findNoticeList
     * 2. ClassName  : FrontNoticeJpaApiController.java
     * 3. Comment    : 프론트 > 공지사항 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    @ApiOperation(value = "공지사항 조회", notes = "공지사항을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping
    public ResponseEntity<Page<FrontNoticeDTO>> findNoticeList(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        return ResponseEntity.ok().body(frontNoticeJpaService.findNoticeList(paramMap, PageRequest.of(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : findOneNotice
     * 2. ClassName  : FrontNoticeJpaApiController.java
     * 3. Comment    : 프론트 > 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    @ApiOperation(value = "공지사항 상세 조회", notes = "공지사항을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 상세 조회 성공", response = FrontNoticeDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}")
    public ResponseEntity<FrontNoticeDTO> findOneNotice(@PathVariable Long idx) {
        return ResponseEntity.ok(frontNoticeJpaService.findOneNotice(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneNotice
     * 2. ClassName  : FrontNoticeJpaApiController.java
     * 3. Comment    : 프론트 > 이전 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    @ApiOperation(value = "이전 공지사항 상세 조회", notes = "공지사항을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "이전 공지사항 상세 조회 성공", response = FrontNoticeDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}/prev")
    public ResponseEntity<FrontNoticeDTO> findPrevOneNotice(@PathVariable Long idx) {
        return ResponseEntity.ok(frontNoticeJpaService.findPrevOneNotice(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneNotice
     * 2. ClassName  : FrontNoticeJpaApiController.java
     * 3. Comment    : 프론트 > 다음 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    @ApiOperation(value = "다음 공지사항 상세 조회", notes = "공지사항을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "다음 공지사항 상세 조회 성공", response = FrontNoticeDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}/next")
    public ResponseEntity<FrontNoticeDTO> findNextOneNotice(@PathVariable Long idx) {
        return ResponseEntity.ok(frontNoticeJpaService.findNextOneNotice(idx));
    }
}
