package com.tsp.api.model.controller;

import com.tsp.api.model.domain.FrontModelDTO;
import com.tsp.api.model.domain.negotiation.FrontNegotiationDTO;
import com.tsp.api.model.domain.negotiation.FrontNegotiationEntity;
import com.tsp.api.model.domain.recommend.FrontRecommendDTO;
import com.tsp.api.model.domain.search.FrontSearchDTO;
import com.tsp.api.model.negotiation.service.FrontNegotiationJpaApiService;
import com.tsp.api.model.service.FrontModelJpaApiService;
import com.tsp.common.paging.Paging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.net.URI;
import java.rmi.ServerError;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.client.HttpClientErrorException.*;

@Validated
@RestController
@RequiredArgsConstructor
@Api(tags = "모델관련 API")
@RequestMapping("/api/model")
public class FrontModelJpaApiController {
    private final FrontModelJpaApiService frontModelJpaApiService;
    private final FrontNegotiationJpaApiService frontNegotiationJpaApiService;

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
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/main")
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
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{categoryCd}")
    public ResponseEntity<Page<FrontModelDTO>> findModelList(@PathVariable @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                                             @RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        paramMap.put("categoryCd", categoryCd);
        return ResponseEntity.ok().body(frontModelJpaApiService.findModelList(paramMap, PageRequest.of(paging.getPageNum(), paging.getSize())));
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
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}")
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
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{categoryCd}/{idx}/prev")
    public ResponseEntity<FrontModelDTO> findPrevOneModel(@PathVariable @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                                          @PathVariable Long idx) {
        return ResponseEntity.ok(frontModelJpaApiService.findPrevOneModel(idx, categoryCd));
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
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{categoryCd}/{idx}/next")
    public ResponseEntity<FrontModelDTO> findNextOneModel(@PathVariable @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                                          @PathVariable Long idx) {
        return ResponseEntity.ok(frontModelJpaApiService.findNextOneModel(idx, categoryCd));
    }

    /**
     * <pre>
     * 1. MethodName : favoriteModel
     * 2. ClassName  : FrontModelJpaApiController.java
     * 3. Comment    : 프론트 > 모델 좋아요 처리
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 01. 09.
     * </pre>
     */
    @ApiOperation(value = "모델 좋아요 처리", notes = "모델을 좋아요 처리한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 좋아요 성공", response = Integer.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping(value = "/{idx}/like")
    public ResponseEntity<Integer> favoriteModel(@PathVariable Long idx) {
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
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/new/{categoryCd}")
    public ResponseEntity<Page<FrontModelDTO>> findNewModelList(@PathVariable @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                                                @RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        paramMap.put("categoryCd", categoryCd);
        paramMap.put("newYn", "Y");
        return ResponseEntity.ok().body(frontModelJpaApiService.findModelList(paramMap, PageRequest.of(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : findRecommendList
     * 2. ClassName  : FrontModelJpaApiController.java
     * 3. Comment    : 프론트 > 추천 검색어 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 05.
     * </pre>
     */
    @ApiOperation(value = "추천 검색어 리스트 조회", notes = "추천 검색어 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "추천 검색어 리스트 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/recommend")
    public ResponseEntity<Page<FrontRecommendDTO>> findRecommendList() {
        return ResponseEntity.ok().body(frontModelJpaApiService.findRecommendList(PageRequest.of(1, 10)));
    }

    /**
     * <pre>
     * 1. MethodName : rankingKeywordList
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 모델 검색어 랭킹 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 07.
     * </pre>
     */
    @ApiOperation(value = "모델 검색어 랭킹 조회", notes = "모델 검색어 랭킹을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 검색어 랭킹 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/rank")
    public ResponseEntity<Page<FrontSearchDTO>> rankingKeywordList() {
        return ResponseEntity.ok().body(frontModelJpaApiService.rankingKeywordList(PageRequest.of(1, 10)));
    }

    /**
     * <pre>
     * 1. MethodName : findModelKeyword
     * 2. ClassName  : TravelController.java
     * 3. Comment    : 추천 검색어 or 랭킹 검색어를 통한 모델 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 07.
     * </pre>
     */
    @ApiOperation(value = "추천 or 랭킹 검색어를 통한 모델 조회", notes = "추천 or 랭킹 검색어를 통해 모델을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 검색 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/keyword")
    public ResponseEntity<Map<String, Object>> findModelKeyword(@RequestParam String keyword) {
        Map<String, Object> travelMap = new HashMap<>();
        travelMap.put("modelList", frontModelJpaApiService.findModelKeyword(keyword));
        return ResponseEntity.ok().body(travelMap);
    }

    /**
     * <pre>
     * 1. MethodName : insertModelNegotiation
     * 2. ClassName  : FrontModelJpaApiController.java
     * 3. Comment    : 모델 섭외 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 11.
     * </pre>
     */
    @ApiOperation(value = "모델 섭외 저장", notes = "모델 섭외를 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "모델 섭외 등록성공", response = FrontNegotiationDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping("/{idx}/negotiation")
    public ResponseEntity<FrontNegotiationDTO> insertModelNegotiation(@PathVariable Long idx, @Valid @RequestBody FrontNegotiationEntity frontNegotiationEntity) {
        return ResponseEntity.created(URI.create("")).body(frontNegotiationJpaApiService.insertModelNegotiation(idx, frontNegotiationEntity));
    }
}
