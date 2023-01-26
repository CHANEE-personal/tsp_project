package com.tsp.api.model;

import com.tsp.api.common.EntityType;
import com.tsp.api.comment.domain.AdminCommentDTO;
import com.tsp.api.comment.domain.AdminCommentEntity;
import com.tsp.api.common.domain.CommonImageDTO;
import com.tsp.api.common.domain.CommonImageEntity;
import com.tsp.api.model.domain.AdminModelDTO;
import com.tsp.api.model.domain.AdminModelEntity;
import com.tsp.api.model.domain.recommend.AdminRecommendDTO;
import com.tsp.api.model.domain.recommend.AdminRecommendEntity;
import com.tsp.api.model.domain.schedule.AdminScheduleDTO;
import com.tsp.api.model.service.AdminModelJpaService;
import com.tsp.common.Paging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.rmi.ServerError;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.web.client.HttpClientErrorException.*;

@Validated
@RestController
@Api(tags = "모델 관련 API")
@RequestMapping("/api/model")
@RequiredArgsConstructor
public class AdminModelJpaController {
    private final AdminModelJpaService adminModelJpaService;

    /**
     * <pre>
     * 1. MethodName : findModelList
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
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
    public ResponseEntity<Page<AdminModelDTO>> findModelList(@PathVariable @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                                             @RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        paramMap.put("categoryCd", categoryCd);
        return ResponseEntity.ok().body(adminModelJpaService.findModelList(paramMap, PageRequest.of(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : findOneModel
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 상세
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "모델 상세 조회", notes = "모델을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 상세조회 성공", response = AdminModelDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public ResponseEntity<AdminModelDTO> findOneModel(@PathVariable Long idx) {
        return ResponseEntity.ok(adminModelJpaService.findOneModel(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneModel
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 이전 모델 상세
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 12.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "이전 모델 상세 조회", notes = "이전 모델을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "이전 모델 상세조회 성공", response = AdminModelDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{categoryCd}/{idx}/prev")
    public ResponseEntity<AdminModelDTO> findPrevOneModel(@PathVariable @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                                          @PathVariable Long idx) {
        return ResponseEntity.ok(adminModelJpaService.findPrevOneModel(AdminModelEntity.builder().idx(idx).categoryCd(categoryCd).build()));
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneModel
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 다음 모델 상세
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 12.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "다음 모델 상세 조회", notes = "다음 모델을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "다음 모델 상세조회 성공", response = AdminModelDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{categoryCd}/{idx}/next")
    public ResponseEntity<AdminModelDTO> findNextOneModel(@PathVariable @Range(min = 1, max = 3, message = "{modelCategory.Range}") Integer categoryCd,
                                                          @PathVariable Long idx) {
        return ResponseEntity.ok(adminModelJpaService.findNextOneModel(AdminModelEntity.builder().idx(idx).categoryCd(categoryCd).build()));
    }

    /**
     * <pre>
     * 1. MethodName : insertModel
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 draft 상태로 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 07.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "모델 저장", notes = "모델을 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "모델 등록성공", response = AdminModelDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public ResponseEntity<AdminModelDTO> insertModel(@Valid @RequestBody AdminModelEntity adminModelEntity) {
        return ResponseEntity.created(URI.create("")).body(adminModelJpaService.insertModel(adminModelEntity));
    }

    /**
     * <pre>
     * 1. MethodName : insertModelImage
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 Image 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 07.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "모델 이미지 저장", notes = "모델 이미지를 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "모델 이미지 등록성공", response = List.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(value = "/{idx}/images", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<CommonImageDTO>> insertModelImage(@PathVariable Long idx, @RequestParam("images") List<MultipartFile> fileName) {
        return ResponseEntity.created(URI.create("")).body(adminModelJpaService.insertModelImage(CommonImageEntity.builder().typeName(EntityType.MODEL).typeIdx(idx).build(), fileName));
    }

    /**
     * <pre>
     * 1. MethodName : deleteModelImage
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 Image 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 07.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "모델 이미지 삭제", notes = "모델 이미지를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "모델 이미지 삭제성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping(value = "/{idx}/images")
    public ResponseEntity<Void> deleteModelImage(@PathVariable Long idx) {
        adminModelJpaService.deleteImage(CommonImageEntity.builder().idx(idx).typeName(EntityType.MODEL).build());
        return ResponseEntity.noContent().build();
    }

    /**
     * <pre>
     * 1. MethodName : updateModel
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 07.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "모델 수정", notes = "모델을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 수정성공", response = AdminModelDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public ResponseEntity<AdminModelDTO> updateModel(@PathVariable Long idx, @Valid @RequestBody AdminModelEntity adminModelEntity) {
        return ResponseEntity.ok(adminModelJpaService.updateModel(idx, adminModelEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteModel
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 17.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "모델 삭제", notes = "모델을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "모델 삭제성공", response = void.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public ResponseEntity<Void> deleteModel(@PathVariable Long idx) {
        adminModelJpaService.deleteModel(idx);
        return ResponseEntity.noContent().build();
    }

    /**
     * <pre>
     * 1. MethodName : updateModelAgency
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 소속사 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 14.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "모델 소속사 수정", notes = "모델 소속사를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 소속사 수정성공", response = AdminModelDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}/agency")
    public ResponseEntity<AdminModelDTO> updateModelAgency(@PathVariable Long idx, @RequestParam Long agencyIdx) {
        return ResponseEntity.ok(adminModelJpaService.updateModelAgency(agencyIdx, AdminModelEntity.builder().idx(idx).build()));
    }

    /**
     * <pre>
     * 1. MethodName : insertModelAdminComment
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 코멘트 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 22.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "모델 어드민 코멘트 저장", notes = "어드민 코멘트를 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "모델 어드민 코멘트 등록성공", response = AdminCommentDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(value = "/{idx}/comment", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminCommentDTO> insertModelAdminComment(Long idx, @Valid @RequestBody AdminCommentEntity adminCommentEntity) {
        return ResponseEntity.created(URI.create("")).body(adminModelJpaService.insertModelAdminComment(idx, adminCommentEntity));
    }

    /**
     * <pre>
     * 1. MethodName : findModelAdminComment
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 어드민 코멘트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 26.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "모델 어드민 코멘트 조회", notes = "모델 어드민 코멘트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 어드민 코멘트 조회성공", response = List.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/admin-comment")
    public ResponseEntity<List<AdminCommentDTO>> findModelAdminComment(@PathVariable Long idx) {
        return ResponseEntity.ok(adminModelJpaService.findModelAdminComment(idx));
    }

    /**
     * <pre>
     * 1. MethodName : toggleModelNewYn
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 새로운 모델 설정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 29.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "새로운 모델 설정", notes = "새로운 모델을 설정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "새로운 모델 설정 성공", response = AdminModelDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping(value = "/{idx}/toggle-new")
    public ResponseEntity<AdminModelDTO> toggleModelNewYn(@PathVariable Long idx) {
        return ResponseEntity.ok(adminModelJpaService.toggleModelNewYn(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findOneModelSchedule
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 모델 스케줄 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 03.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "모델 스케줄 조회", notes = "모델 스케줄을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 스케줄 조회성공", response = List.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}/schedule")
    public ResponseEntity<List<AdminScheduleDTO>> findOneModelSchedule(@PathVariable Long idx) {
        return ResponseEntity.ok(adminModelJpaService.findOneModelSchedule(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findRecommendList
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 추천 검색어 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 05.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "추천 검색어 조회", notes = "추천 검색어를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "추천 검색어 조회성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/recommend")
    public ResponseEntity<Map<String, Object>> findRecommendList(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        Map<String, Object> recommendMap = new HashMap<>();
        recommendMap.put("recommendList", adminModelJpaService.findRecommendList(paramMap));
        return ResponseEntity.ok().body(recommendMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneRecommend
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 추천 검색어 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 05.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "추천 검색어 상세 조회", notes = "추천 검색어를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "추천 검색어 상세 조회성공", response = AdminRecommendDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/recommend/{idx}")
    public ResponseEntity<AdminRecommendDTO> findOneRecommend(@PathVariable Long idx) {
        return ResponseEntity.ok(adminModelJpaService.findOneRecommend(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertRecommend
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 추천 검색어 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 05.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "추천 검색어 등록", notes = "추천 검색어를 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "추천 검색어 등록성공", response = AdminRecommendDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping("/recommend")
    public ResponseEntity<AdminRecommendDTO> insertRecommend(@Valid @RequestBody AdminRecommendEntity adminRecommendEntity) {
        return ResponseEntity.created(URI.create("")).body(adminModelJpaService.insertRecommend(adminRecommendEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateRecommend
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 추천 검색어 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 05.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "추천 검색어 등록", notes = "추천 검색어를 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "추천 검색어 등록성공", response = AdminRecommendDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/recommend/{idx}")
    public ResponseEntity<AdminRecommendDTO> updateRecommend(@PathVariable Long idx, @Valid @RequestBody AdminRecommendEntity adminRecommendEntity) {
        return ResponseEntity.ok(adminModelJpaService.updateRecommend(adminRecommendEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteRecommend
     * 2. ClassName  : AdminModelJpaController.java
     * 3. Comment    : 관리자 추천 검색어 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 05.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "추천 검색어 삭제", notes = "추천 검색어를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "추천 검색어 삭제성공", response = AdminRecommendDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/recommend/{idx}")
    public ResponseEntity<AdminRecommendDTO> deleteRecommend(@PathVariable Long idx) {
        adminModelJpaService.deleteRecommend(idx);
        return ResponseEntity.noContent().build();
    }
}
