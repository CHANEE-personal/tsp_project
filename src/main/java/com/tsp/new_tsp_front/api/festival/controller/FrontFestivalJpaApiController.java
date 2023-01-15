package com.tsp.new_tsp_front.api.festival.controller;

import com.tsp.new_tsp_front.api.festival.domain.FrontFestivalDTO;
import com.tsp.new_tsp_front.api.festival.domain.FrontFestivalEntity;
import com.tsp.new_tsp_front.api.festival.service.FrontFestivalJpaService;
import com.tsp.new_tsp_front.common.paging.Paging;
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
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Api(tags = "행사관련 API")
@RequestMapping("/api/festival")
public class FrontFestivalJpaApiController {

    private final FrontFestivalJpaService frontFestivalJpaService;

    /**
     * <pre>
     * 1. MethodName : findFestivalGroup
     * 2. ClassName  : FrontFestivalJpaApiController.java
     * 3. Comment    : 프론트 > 행사 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    @ApiOperation(value = "행사 리스트 조회", notes = "행사 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "행사 리스트 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/list/{month}")
    public ResponseEntity<Map<String, Object>> findFestivalGroup(@PathVariable Integer month) {
        Map<String, Object> festivalMap = new HashMap<>();
        festivalMap.put("festivalGroup", frontFestivalJpaService.findFestivalGroup(month));
        return ResponseEntity.ok().body(festivalMap);
    }

    /**
     * <pre>
     * 1. MethodName : findFestivalList
     * 2. ClassName  : FrontFestivalJpaApiController.java
     * 3. Comment    : 프론트 > 행사 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 23.
     * </pre>
     */
    @ApiOperation(value = "행사 조회", notes = "행사를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "행사 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/list/{month}/{day}")
    public ResponseEntity<Page<FrontFestivalDTO>> findFestivalList(@PathVariable Integer month, @PathVariable Integer day, Paging paging) {
        return ResponseEntity.ok().body(frontFestivalJpaService.findFestivalList(FrontFestivalEntity.builder()
                .festivalMonth(month).festivalDay(day).build(), PageRequest.of(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : findOneFestival
     * 2. ClassName  : FrontFestivalJpaApiController.java
     * 3. Comment    : 행사 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    @ApiOperation(value = "행사 상세 조회", notes = "행사를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "행사 상세 조회 성공", response = FrontFestivalDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public ResponseEntity<FrontFestivalDTO> findOneFestival(@PathVariable Long idx) {
        return ResponseEntity.ok(frontFestivalJpaService.findOneFestival(idx));
    }
}
