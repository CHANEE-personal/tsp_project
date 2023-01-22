package com.tsp.api.model.negotiation.controller;

import com.tsp.api.model.domain.negotiation.FrontNegotiationDTO;
import com.tsp.api.model.domain.negotiation.FrontNegotiationEntity;
import com.tsp.api.model.negotiation.service.FrontNegotiationJpaApiService;
import com.tsp.common.paging.Paging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.rmi.ServerError;
import java.util.Map;

@Validated
@RestController
@Api(tags = "모델 섭외 관련 API")
@RequestMapping("/api/negotiation")
@RequiredArgsConstructor
public class FrontNegotiationJpaApiController {
    private final FrontNegotiationJpaApiService frontNegotiationJpaApiService;

    /**
     * <pre>
     * 1. MethodName : findModelNegotiationList
     * 2. ClassName  : FrontNegotiationJpaController.java
     * 3. Comment    : 모델 섭외 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 11.
     * </pre>
     */
    @ApiOperation(value = "모델 섭외 조회", notes = "모델 섭외를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 섭외 조회성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public ResponseEntity<Page<FrontNegotiationDTO>> findModelNegotiationList(@RequestParam(required = false) Map<String, Object> paramMap,
                                                                              @RequestParam(value = "searchStartTime", required = false) String searchStartTime,
                                                                              @RequestParam(value = "searchEndTime", required = false) String searchEndTime,
                                                                              Paging paging) {
        if (searchStartTime != null && searchEndTime != null) {
            paramMap.put("searchStartTime", searchStartTime);
            paramMap.put("searchEndTime", searchEndTime);
        }

        return ResponseEntity.ok().body(frontNegotiationJpaApiService.findModelNegotiationList(paramMap, PageRequest.of(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : findOneNegotiation
     * 2. ClassName  : FrontNegotiationJpaController.java
     * 3. Comment    : 모델 섭외 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 11.
     * </pre>
     */
    @ApiOperation(value = "모델 섭외 상세 조회", notes = "모델 섭외을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 섭외 상세 조회 성공", response = FrontNegotiationDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public ResponseEntity<FrontNegotiationDTO> findOneNegotiation(@PathVariable Long idx) {
        return ResponseEntity.ok(frontNegotiationJpaApiService.findOneNegotiation(idx));
    }

    /**
     * <pre>
     * 1. MethodName : updateModelNegotiation
     * 2. ClassName  : FrontNegotiationJpaController.java
     * 3. Comment    : 모델 섭외 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 11.
     * </pre>
     */
    @ApiOperation(value = "모델 섭외 수정", notes = "모델 섭외를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 섭외 수정성공", response = FrontNegotiationDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public ResponseEntity<FrontNegotiationDTO> updateModelNegotiation(@PathVariable Long idx, @Valid @RequestBody FrontNegotiationEntity frontNegotiationEntity) {
        return ResponseEntity.ok(frontNegotiationJpaApiService.updateModelNegotiation(idx, frontNegotiationEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteModelNegotiation
     * 2. ClassName  : FrontNegotiationJpaController.java
     * 3. Comment    : 모델 섭외 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 11.
     * </pre>
     */
    @ApiOperation(value = "모델 섭외 삭제", notes = "모델 섭외를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "모델 섭외 삭제성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public ResponseEntity<Long> deleteModelNegotiation(@PathVariable Long idx) {
        frontNegotiationJpaApiService.deleteModelNegotiation(idx);
        return ResponseEntity.noContent().build();
    }
}
