package com.tsp.new_tsp_front.api.notice.controller;

import com.tsp.new_tsp_front.api.notice.domain.FrontNoticeDTO;
import com.tsp.new_tsp_front.api.notice.service.impl.FrontNoticeJpaService;
import com.tsp.new_tsp_front.common.SearchCommon;
import com.tsp.new_tsp_front.common.paging.Paging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.rmi.ServerError;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.ceil;

@RestController
@RequiredArgsConstructor
@Api(tags = "공지사항관련 API")
@RequestMapping("/api/notice")
public class FrontNoticeJpaApiController {
    private final FrontNoticeJpaService frontNoticeJpaService;
    private final SearchCommon searchCommon;

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
    @GetMapping(value = "/lists")
    public ResponseEntity<Map<String, Object>> findNoticeList(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> noticeMap = searchCommon.searchCommon(paging, paramMap);

        int count = this.frontNoticeJpaService.findFixedNoticeCount(noticeMap) + this.frontNoticeJpaService.findNoticeCount(noticeMap);
        // 리스트 수
        resultMap.put("pageSize", paging.getSize());
        // 전체 페이지 수
        resultMap.put("perPageListCnt", ceil((double) count / paging.getSize()));
        // 전체 아이템 수
        resultMap.put("noticeListTotalCnt", count);
        resultMap.put("noticeList", this.frontNoticeJpaService.findNoticeList(noticeMap));
        resultMap.put("fixedNoticeList", this.frontNoticeJpaService.findFixedNoticeList(noticeMap));

        return ResponseEntity.ok().body(resultMap);
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
