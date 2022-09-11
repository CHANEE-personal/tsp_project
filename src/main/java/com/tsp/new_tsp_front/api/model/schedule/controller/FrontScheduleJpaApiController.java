package com.tsp.new_tsp_front.api.model.schedule.controller;

import com.tsp.new_tsp_front.api.model.schedule.service.FrontScheduleJpaApiService;
import com.tsp.new_tsp_front.common.SearchCommon;
import com.tsp.new_tsp_front.common.paging.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.rmi.ServerError;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.ceil;

@Validated
@RestController
@Api(tags = "모델 스케줄 관련 API")
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class FrontScheduleJpaApiController {
    private final FrontScheduleJpaApiService frontScheduleJpaApiService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : findScheduleList
     * 2. ClassName  : FrontScheduleJpaApiController.java
     * 3. Comment    : 프론트 > 모델 스케줄 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 01.
     * </pre>
     */
    @ApiOperation(value = "모델 스케줄 조회", notes = "모델 스케줄을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 스케줄 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public Map<String, Object> findScheduleList(@RequestParam(required = false) Map<String, Object> paramMap,
                                                @RequestParam(value = "searchStartTime", required = false) String searchStartTime,
                                                @RequestParam(value = "searchEndTime", required = false) String searchEndTime,
                                                Page page) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> scheduleMap = searchCommon.searchCommon(page, paramMap);

        if (searchStartTime != null && searchEndTime != null) {
            scheduleMap.put("searchStartTime", searchStartTime);
            scheduleMap.put("searchEndTime", searchEndTime);
        }

        // 리스트 수
        resultMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        resultMap.put("perPageListCnt", ceil((double)this.frontScheduleJpaApiService.findScheduleCount(scheduleMap) / page.getSize()));
        // 전체 아이템 수
        resultMap.put("scheduleListTotalCnt", this.frontScheduleJpaApiService.findModelScheduleList(scheduleMap));
        resultMap.put("scheduleList", this.frontScheduleJpaApiService.findModelScheduleList(scheduleMap));

        return resultMap;
    }
}
