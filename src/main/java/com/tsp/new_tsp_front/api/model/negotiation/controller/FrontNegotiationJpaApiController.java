package com.tsp.new_tsp_front.api.model.negotiation.controller;

import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.negotiation.FrontNegotiationDTO;
import com.tsp.new_tsp_front.api.model.domain.negotiation.FrontNegotiationEntity;
import com.tsp.new_tsp_front.api.model.negotiation.service.FrontNegotiationJpaApiService;
import com.tsp.new_tsp_front.common.SearchCommon;
import com.tsp.new_tsp_front.common.paging.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.ceil;

@Validated
@RestController
@Api(tags = "모델 섭외 관련 API")
@RequestMapping("/api/negotiation")
@RequiredArgsConstructor
public class FrontNegotiationJpaApiController {
    private final FrontNegotiationJpaApiService frontNegotiationJpaApiService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : findModelNegotiationList
     * 2. ClassName  : FrontNegotiationJpaController.java
     * 3. Comment    : 모델 섭외 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 11.
     * </pre>
     */
    @ApiOperation(value = "모델 섭외 조회", notes = "모델 섭외를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 섭외 조회성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public Map<String, Object> findModelNegotiationList(@RequestParam(required = false) Map<String, Object> paramMap,
                                                        @RequestParam(value = "searchStartTime", required = false) String searchStartTime,
                                                        @RequestParam(value = "searchEndTime", required = false) String searchEndTime,
                                                        Page page) {
        // 페이징 및 검색
        Map<String, Object> negotiationMap = searchCommon.searchCommon(page, paramMap);

        if (searchStartTime != null && searchEndTime != null) {
            negotiationMap.put("searchStartTime", searchStartTime);
            negotiationMap.put("searchEndTime", searchEndTime);
        }

        Integer negotiationCount = this.frontNegotiationJpaApiService.findNegotiationCount(negotiationMap);
        List<FrontModelDTO> negotiationList = new ArrayList<>();

        if (negotiationCount > 0) {
            negotiationList = this.frontNegotiationJpaApiService.findModelNegotiationList(negotiationMap);
        }

        // 리스트 수
        negotiationMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        negotiationMap.put("perPageListCnt", ceil((double) negotiationCount / page.getSize()));
        // 전체 아이템 수
        negotiationMap.put("negotiationListTotalCnt", negotiationCount);

        negotiationMap.put("negotiationList", negotiationList);

        return negotiationMap;
    }

    /**
     * <pre>
     * 1. MethodName : findOneNegotiation
     * 2. ClassName  : FrontNegotiationJpaController.java
     * 3. Comment    : 모델 섭외 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 11.
     * </pre>
     */
    @ApiOperation(value = "모델 섭외 상세 조회", notes = "모델 섭외을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 섭외 상세 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public FrontNegotiationDTO findOneNegotiation(@PathVariable Integer idx) {
        return this.frontNegotiationJpaApiService.findOneNegotiation(FrontNegotiationEntity.builder().idx(idx).build());
    }

    /**
     * <pre>
     * 1. MethodName : insertModelNegotiation
     * 2. ClassName  : FrontNegotiationJpaController.java
     * 3. Comment    : 모델 섭외 저장
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 11.
     * </pre>
     */
    @ApiOperation(value = "모델 섭외 저장", notes = "모델 섭외를 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 섭외 등록성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public FrontNegotiationDTO insertModelNegotiation(@Valid @RequestBody FrontNegotiationEntity frontNegotiationEntity) {
        return this.frontNegotiationJpaApiService.insertModelNegotiation(frontNegotiationEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateModelNegotiation
     * 2. ClassName  : FrontNegotiationJpaController.java
     * 3. Comment    : 모델 섭외 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 11.
     * </pre>
     */
    @ApiOperation(value = "모델 섭외 수정", notes = "모델 섭외를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 섭외 수정성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public FrontNegotiationDTO updateModelNegotiation(@Valid @RequestBody FrontNegotiationEntity frontNegotiationEntity) {
        return this.frontNegotiationJpaApiService.updateModelNegotiation(frontNegotiationEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteModelNegotiation
     * 2. ClassName  : FrontNegotiationJpaController.java
     * 3. Comment    : 모델 섭외 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 11.
     * </pre>
     */
    @ApiOperation(value = "모델 섭외 삭제", notes = "모델 섭외를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 섭외 삭제성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public Integer deleteModelNegotiation(@PathVariable Integer idx) {
        return this.frontNegotiationJpaApiService.deleteModelNegotiation(idx);
    }
}