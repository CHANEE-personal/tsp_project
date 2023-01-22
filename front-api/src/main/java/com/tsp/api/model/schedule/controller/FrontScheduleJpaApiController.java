package com.tsp.api.model.schedule.controller;

import com.tsp.api.model.domain.schedule.FrontScheduleDTO;
import com.tsp.api.model.schedule.service.FrontScheduleJpaApiService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.rmi.ServerError;
import java.util.Map;


@Validated
@RestController
@Api(tags = "모델 스케줄 관련 API")
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class FrontScheduleJpaApiController {
    private final FrontScheduleJpaApiService frontScheduleJpaApiService;

    /**
     * <pre>
     * 1. MethodName : findScheduleList
     * 2. ClassName  : FrontScheduleJpaApiController.java
     * 3. Comment    : 프론트 > 모델 스케줄 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 01.
     * </pre>
     */
    @ApiOperation(value = "모델 스케줄 조회", notes = "모델 스케줄을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 스케줄 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping
    public ResponseEntity<Page<FrontScheduleDTO>> findScheduleList(@RequestParam(required = false) Map<String, Object> paramMap,
                                                                   @RequestParam(value = "searchStartTime", required = false) String searchStartTime,
                                                                   @RequestParam(value = "searchEndTime", required = false) String searchEndTime,
                                                                   Paging paging) {
        if (searchStartTime != null && searchEndTime != null) {
            paramMap.put("searchStartTime", searchStartTime);
            paramMap.put("searchEndTime", searchEndTime);
        }

        return ResponseEntity.ok().body(frontScheduleJpaApiService.findScheduleList(paramMap, PageRequest.of(paging.getPageNum(), paging.getSize())));
    }
}
