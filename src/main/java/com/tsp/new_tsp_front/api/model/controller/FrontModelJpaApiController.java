package com.tsp.new_tsp_front.api.model.controller;

import com.tsp.new_tsp_front.api.model.domain.FrontModelDTO;
import com.tsp.new_tsp_front.api.model.service.FrontModelJpaApiService;
import com.tsp.new_tsp_front.common.SearchCommon;
import com.tsp.new_tsp_front.common.paging.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.rmi.ServerError;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_front.api.model.domain.FrontModelEntity.*;

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
     * 1. MethodName : lists/main/{categoryCd}/{idx}
     * 2. ClassName  : FrontModelJpaApiController.java
     * 3. Comment    : 프론트 > 메인 모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 03. 27.
     * </pre>
     */
    @ApiOperation(value = "메인 모델 배너", notes = "메인 배너 모델을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists/main")
    public ConcurrentHashMap<String, Object> getMainModelList() {
        ConcurrentHashMap<String, Object> mainModelMap = new ConcurrentHashMap<>();

        // 전체 아이템 수
        mainModelMap.put("modelListTotalCnt", this.frontModelJpaApiService.getMainModelList().size());
        mainModelMap.put("modelList", this.frontModelJpaApiService.getMainModelList());
        return mainModelMap;
    }

    /**
     * <pre>
     * 1. MethodName : lists/{categoryCd}
     * 2. ClassName  : FrontModelJpaApiController.java
     * 3. Comment    : 프론트 > 모델 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 02.
     * </pre>
     *
     * @param categoryCd
     * @param paramMap
     * @param page
     */
    @ApiOperation(value = "모델 조회", notes = "모델을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists/{categoryCd}")
    public ConcurrentHashMap<String, Object> getModelList(@PathVariable("categoryCd")
                                                          @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                                          @RequestParam(required = false) Map<String, Object> paramMap,
                                                          Page page) throws Exception {
        ConcurrentHashMap<String, Object> resultMap = new ConcurrentHashMap<>();
        // 페이징 및 검색
        ConcurrentHashMap<String, Object> modelMap = searchCommon.searchCommon(page, paramMap);
        modelMap.put("categoryCd", categoryCd);

        // 리스트 수
        resultMap.put("pageSize", page.getSize());
        // 전체 페이지 수
        resultMap.put("perPageListCnt", Math.ceil((this.frontModelJpaApiService.getModelList(modelMap).size() - 1) / page.getSize() + 1));
        // 전체 아이템 수
        resultMap.put("modelListTotalCnt", this.frontModelJpaApiService.getModelCount(modelMap));
        resultMap.put("modelList", this.frontModelJpaApiService.getModelList(modelMap));

        return resultMap;
    }

    /**
     * <pre>
     * 1. MethodName : lists/{categoryCd}/{idx}
     * 2. ClassName  : FrontModelJpaApiController.java
     * 3. Comment    : 프론트 > 모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 01. 09.
     * </pre>
     *
     * @param categoryCd
     * @param idx
     */
    @ApiOperation(value = "모델 상세 조회", notes = "모델을 상세 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = Map.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{categoryCd}/{idx}")
    public FrontModelDTO getModelInfo(@PathVariable("categoryCd")
                                      @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                      @PathVariable("idx") Integer idx) throws Exception {
        return this.frontModelJpaApiService.getModelInfo(builder().categoryCd(categoryCd).idx(idx).build());
    }
}
