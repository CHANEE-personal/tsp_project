package com.tsp.new_tsp_front.api.model.controller;

import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.domain.FrontModelEntity;
import com.tsp.new_tsp_front.api.model.service.FrontModelJpaApiService;
import com.tsp.new_tsp_front.common.SearchCommon;
import com.tsp.new_tsp_front.common.paging.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.rmi.ServerError;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.ceil;
import static org.springframework.web.client.HttpClientErrorException.*;

@Validated
@RestController
@RequiredArgsConstructor
@Api(tags = "모델관련 API")
@RequestMapping("/api/model")
public class FrontModelJpaApiController {
    private final FrontModelJpaApiService frontModelJpaApiService;
    private final SearchCommon searchCommon;

    /**
     * <pre>
     * 1. MethodName : findMainModelList
     * 2. ClassName  : FrontModelJpaApiController.java
     * 3. Comment    : 프론트 > 메인 모델 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 03. 27.
     * </pre>
     */
    @ApiOperation(value = "메인 모델 배너", notes = "메인 배너 모델을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "메인모델 배너 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists/main")
    public ResponseEntity<Map<String, Object>> findMainModelList() {
        Map<String, Object> mainModelMap = new HashMap<>();

        // 전체 아이템 수
        mainModelMap.put("modelListTotalCnt", this.frontModelJpaApiService.findMainModelList().size());
        mainModelMap.put("modelList", this.frontModelJpaApiService.findMainModelList());
        return ResponseEntity.ok().body(mainModelMap);
    }

    /**
     * <pre>
     * 1. MethodName : findModelList
     * 2. ClassName  : FrontModelJpaApiController.java
     * 3. Comment    : 프론트 > 모델 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 02.
     * </pre>
     */
    @ApiOperation(value = "모델 조회", notes = "모델을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists/{categoryCd}")
    public ResponseEntity<Map<String, Object>> findModelList(@PathVariable @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                                             @RequestParam(required = false) Map<String, Object> paramMap, Page page) {
        Map<String, Object> resultMap = new HashMap<>();
        // 페이징 및 검색
        Map<String, Object> modelMap = searchCommon.searchCommon(page, paramMap);
        modelMap.put("categoryCd", categoryCd);

        // 리스트 수
        resultMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        resultMap.put("perPageListCnt", ceil((double) this.frontModelJpaApiService.findModelCount(modelMap) / page.getSize()));
        // 전체 아이템 수
        resultMap.put("modelListTotalCnt", this.frontModelJpaApiService.findModelCount(modelMap));
        resultMap.put("modelList", this.frontModelJpaApiService.findModelList(modelMap));

        return ResponseEntity.ok().body(resultMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneModel
     * 2. ClassName  : FrontModelJpaApiController.java
     * 3. Comment    : 프론트 > 모델 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 09.
     * </pre>
     */
    @ApiOperation(value = "모델 상세 조회", notes = "모델을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 상세 조회 성공", response = FrontModelDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{categoryCd}/{idx}")
    public ResponseEntity<FrontModelDTO> findOneModel(@PathVariable Long idx) {
        return ResponseEntity.ok(frontModelJpaApiService.findOneModel(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneModel
     * 2. ClassName  : FrontModelJpaController.java
     * 3. Comment    : 이전 모델 상세
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    @ApiOperation(value = "이전 모델 상세 조회", notes = "이전 모델을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "이전 모델 상세조회 성공", response = FrontModelDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{categoryCd}/{idx}/prev")
    public ResponseEntity<FrontModelDTO> findPrevOneModel(@PathVariable @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                                          @PathVariable Long idx) {
        return ResponseEntity.ok(frontModelJpaApiService.findPrevOneModel(FrontModelEntity.builder().idx(idx).categoryCd(categoryCd).build()));
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneModel
     * 2. ClassName  : FrontModelJpaController.java
     * 3. Comment    : 다음 모델 상세
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 17.
     * </pre>
     */
    @ApiOperation(value = "다음 모델 상세 조회", notes = "다음 모델을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "다음 모델 상세조회 성공", response = FrontModelDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{categoryCd}/{idx}/next")
    public ResponseEntity<FrontModelDTO> findNextOneModel(@PathVariable @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                                          @PathVariable Long idx) {
        return ResponseEntity.ok(frontModelJpaApiService.findNextOneModel(FrontModelEntity.builder().idx(idx).categoryCd(categoryCd).build()));
    }

    /**
     * <pre>
     * 1. MethodName : favoriteModel
     * 2. ClassName  : FrontModelJpaApiController.java
     * 3. Comment    : 프론트 > 모델 좋아요 처리
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 09.
     * </pre>
     */
    @ApiOperation(value = "모델 좋아요 처리", notes = "모델을 좋아요 처리한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 좋아요 성공", response = Integer.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping(value = "/{idx}/like")
    public ResponseEntity<Integer> favoriteModel(@PathVariable Long idx) {
        if (frontModelJpaApiService.findOneModel(idx) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(frontModelJpaApiService.favoriteModel(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findNewModelList
     * 2. ClassName  : FrontModelJpaApiController.java
     * 3. Comment    : 프론트 > 새로운 모델 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 29.
     * </pre>
     */
    @ApiOperation(value = "새로운 모델 조회", notes = "모델을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists/new/{categoryCd}")
    public ResponseEntity<Map<String, Object>> findNewModelList(@PathVariable @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                                                @RequestParam(required = false) Map<String, Object> paramMap, Page page) {
        Map<String, Object> resultMap = new HashMap<>();
        // 페이징 및 검색
        Map<String, Object> newModelMap = searchCommon.searchCommon(page, paramMap);
        newModelMap.put("categoryCd", categoryCd);
        newModelMap.put("newYn", "Y");

        // 리스트 수
        resultMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        resultMap.put("perPageListCnt", ceil((double) this.frontModelJpaApiService.findModelCount(newModelMap) / page.getSize()));
        // 전체 아이템 수
        resultMap.put("newModelListTotalCnt", this.frontModelJpaApiService.findModelList(newModelMap));
        resultMap.put("newModelList", this.frontModelJpaApiService.findModelList(newModelMap));

        return ResponseEntity.ok().body(resultMap);
    }
}
