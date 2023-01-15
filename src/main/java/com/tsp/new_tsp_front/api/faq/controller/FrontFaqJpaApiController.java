package com.tsp.new_tsp_front.api.faq.controller;

import com.tsp.new_tsp_front.api.faq.domain.FrontFaqDTO;
import com.tsp.new_tsp_front.api.faq.service.FrontFaqJpaService;
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
@Api(tags = "FAQ관련 API")
@RequestMapping("/api/faq")
public class FrontFaqJpaApiController {
    private final FrontFaqJpaService frontFaqJpaService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : findFaqList
     * 2. ClassName  : FrontFaqJpaApiController.java
     * 3. Comment    : 프론트 > FAQ 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 23.
     * </pre>
     */
    @ApiOperation(value = "FAQ 조회", notes = "FAQ를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FAQ 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public ResponseEntity<Map<String, Object>> findFaqList(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> faqMap = searchCommon.searchCommon(paging, paramMap);

        // 리스트 수
        resultMap.put("pageSize", paging.getSize());
        // 전체 페이지 수
        resultMap.put("perPageListCnt", ceil((double) this.frontFaqJpaService.findFaqCount(faqMap) / paging.getSize()));
        // 전체 아이템 수
        resultMap.put("faqListTotalCnt", this.frontFaqJpaService.findFaqList(faqMap));
        resultMap.put("faqList", this.frontFaqJpaService.findFaqList(faqMap));

        return ResponseEntity.ok().body(resultMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneFaq
     * 2. ClassName  : FrontNoticeJpaApiController.java
     * 3. Comment    : 프론트 > 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 23.
     * </pre>
     */
    @ApiOperation(value = "FAQ 상세 조회", notes = "FAQ를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FAQ 상세 조회 성공", response = FrontFaqDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}")
    public ResponseEntity<FrontFaqDTO> findOneFaq(@PathVariable Long idx) {
        return ResponseEntity.ok(frontFaqJpaService.findOneFaq(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneFaq
     * 2. ClassName  : FrontFaqJpaApiController.java
     * 3. Comment    : 프론트 > 이전 FAQ 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    @ApiOperation(value = "이전 FAQ 상세 조회", notes = "이전 FAQ를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "이전 FAQ 상세 조회 성공", response = FrontFaqDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}/prev")
    public ResponseEntity<FrontFaqDTO> findPrevOneFaq(@PathVariable Long idx) {
        return ResponseEntity.ok(frontFaqJpaService.findPrevOneFaq(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneFaq
     * 2. ClassName  : FrontFaqJpaApiController.java
     * 3. Comment    : 프론트 > 다음 FAQ 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    @ApiOperation(value = "다음 FAQ 상세 조회", notes = "다음 FAQ를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "다음 FAQ 상세 조회 성공", response = FrontFaqDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}/next")
    public ResponseEntity<FrontFaqDTO> findNextOneFaq(@PathVariable Long idx) {
        return ResponseEntity.ok(frontFaqJpaService.findNextOneFaq(idx));
    }
}
