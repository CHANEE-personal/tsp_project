package com.tsp.new_tsp_front.api.notice.controller;

import com.tsp.new_tsp_front.api.notice.domain.FrontNoticeDTO;
import com.tsp.new_tsp_front.api.notice.domain.FrontNoticeEntity;
import com.tsp.new_tsp_front.api.notice.service.impl.FrontNoticeJpaService;
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
@Api(tags = "공지사항관련 API")
@RequestMapping("/api/notice")
public class FrontNoticeJpaApiController {
    private final FrontNoticeJpaService frontNoticeJpaService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : lists
     * 2. ClassName  : FrontNoticeJpaApiController.java
     * 3. Comment    : 프론트 > 공지사항 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    @ApiOperation(value = "공지사항 조회", notes = "공지사항을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public Map<String, Object> findNoticesList(@RequestParam(required = false) Map<String, Object> paramMap, Page page) {
        Map<String, Object> resultMap = new HashMap<>();

        // 리스트 수
        resultMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        resultMap.put("perPageListCnt", ceil((this.frontNoticeJpaService.findNoticesList(searchCommon.searchCommon(page, paramMap)).size() - 1) / page.getSize() + 1));
        // 전체 아이템 수
        resultMap.put("noticeListTotalCnt", this.frontNoticeJpaService.findNoticesList(searchCommon.searchCommon(page, paramMap)));
        resultMap.put("noticeList", this.frontNoticeJpaService.findNoticesList(searchCommon.searchCommon(page, paramMap)));

        return resultMap;
    }

    /**
     * <pre>
     * 1. MethodName : {idx}
     * 2. ClassName  : FrontNoticeJpaApiController.java
     * 3. Comment    : 프론트 > 공지사항 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    @ApiOperation(value = "공지사항 상세 조회", notes = "공지사항을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 상세 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}")
    public FrontNoticeDTO findOneNotice(@PathVariable Integer idx) {
        return this.frontNoticeJpaService.findOneNotice(FrontNoticeEntity.builder().idx(idx).build());
    }
}
