package com.tsp.api.support;

import com.tsp.api.domain.comment.AdminCommentDTO;
import com.tsp.api.domain.support.AdminSupportDTO;
import com.tsp.api.domain.support.AdminSupportEntity;
import com.tsp.api.domain.support.evaluation.EvaluationDTO;
import com.tsp.api.domain.support.evaluation.EvaluationEntity;
import com.tsp.api.support.service.AdminSupportJpaService;
import com.tsp.common.Paging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.net.URI;
import java.rmi.ServerError;
import java.util.List;
import java.util.Map;

import static org.springframework.web.client.HttpClientErrorException.*;

@Validated
@RestController
@Api(tags = "지원모델 관련 API")
@RequestMapping("/api/support")
@RequiredArgsConstructor
public class AdminSupportJpaController {

    private final AdminSupportJpaService adminSupportJpaService;

    /**
     * <pre>
     * 1. MethodName : findSupportList
     * 2. ClassName  : AdminSupportJpaController.java
     * 3. Comment    : 관리자 지원모델 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "지원모델 조회", notes = "지원모델을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "지원모델 조회 성공", response = Page.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/lists")
    public ResponseEntity<Page<AdminSupportDTO>> findSupportList(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        return ResponseEntity.ok().body(adminSupportJpaService.findSupportList(paramMap, PageRequest.of(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : updateSupportModel
     * 2. ClassName  : AdminSupportJpaController.java
     * 3. Comment    : 관리자 지원 모델 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "지원모델 수정", notes = "지원모델을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "지원모델 수정 성공", response = AdminSupportDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public ResponseEntity<AdminSupportDTO> updateSupportModel(@PathVariable Long idx, @Valid @RequestBody AdminSupportEntity adminSupportEntity) {
        return ResponseEntity.ok(adminSupportJpaService.updateSupportModel(idx, adminSupportEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteSupportModel
     * 2. ClassName  : AdminSupportJpaController.java
     * 3. Comment    : 관리자 지원 모델 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "지원모델 삭제", notes = "지원모델을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "지원모델 삭제 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public ResponseEntity<Long> deleteSupportModel(@PathVariable Long idx) {
        adminSupportJpaService.deleteSupportModel(idx);
        return ResponseEntity.noContent().build();
    }

    /**
     * <pre>
     * 1. MethodName : findEvaluationList
     * 2. ClassName  : AdminSupportJpaController.java
     * 3. Comment    : 관리자 지원 모델 평가 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "지원모델 평가 리스트 조회", notes = "지원모델을 평가 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "지원모델 평가 리스트 조회성공", response = Page.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping("/evaluation/lists")
    public ResponseEntity<Page<EvaluationDTO>> findEvaluationList(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        return ResponseEntity.ok().body(adminSupportJpaService.findEvaluationList(paramMap, PageRequest.of(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : findOneEvaluation
     * 2. ClassName  : findEvaluationsList.java
     * 3. Comment    : 관리자 지원모델 평가 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "지원모델 평가 상세 조회", notes = "지원모델 평가를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "지원모델 평가 상세 조회성공", response = EvaluationDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/evaluation/{idx}")
    public ResponseEntity<EvaluationDTO> findOneEvaluation(@PathVariable Long idx) {
        return ResponseEntity.ok(adminSupportJpaService.findOneEvaluation(idx));
    }

    /**
     * <pre>
     * 1. MethodName : evaluationSupportModel
     * 2. ClassName  : AdminSupportJpaController.java
     * 3. Comment    : 관리자 지원 모델 평가
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "지원모델 평가", notes = "지원모델을 평가한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "지원모델 평가성공", response = EvaluationDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping("/{idx}/evaluation")
    public ResponseEntity<EvaluationDTO> evaluationSupportModel(@Valid @RequestBody EvaluationEntity evaluationEntity,
                                                @PathVariable("idx") Long idx) {
        return ResponseEntity.created(URI.create("")).body(adminSupportJpaService.evaluationSupportModel(idx, evaluationEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateEvaluation
     * 2. ClassName  : AdminSupportJpaController.java
     * 3. Comment    : 관리자 지원 모델 평가 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "지원모델 평가 수정", notes = "지원모델을 평가를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "지원모델 평가 수정성공", response = EvaluationDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}/evaluation")
    public ResponseEntity<EvaluationDTO> updateEvaluation(@PathVariable Long idx, @Valid @RequestBody EvaluationEntity evaluationEntity) {
        return ResponseEntity.ok(adminSupportJpaService.updateEvaluation(idx, evaluationEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteEvaluation
     * 2. ClassName  : AdminSupportJpaController.java
     * 3. Comment    : 관리자 지원 모델 평가 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "지원모델 평가 삭제", notes = "지원모델을 평가를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "지원모델 평가 수정성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}/evaluation")
    public ResponseEntity<Long> deleteEvaluation(@PathVariable Long idx) {
        adminSupportJpaService.deleteEvaluation(idx);
        return ResponseEntity.noContent().build();
    }

    /**
     * <pre>
     * 1. MethodName : updatePass
     * 2. ClassName  : AdminSupportJpaController.java
     * 3. Comment    : 관리자 지원 모델 합격 처리
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "지원모델 합격 처리", notes = "지원모델을 합격 처리한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "지원모델 합격 처리성공", response = AdminSupportDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}/pass")
    public ResponseEntity<AdminSupportDTO> updatePass(@PathVariable Long idx) {
        return ResponseEntity.ok(adminSupportJpaService.updatePass(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findSupportAdminComment
     * 2. ClassName  : AdminSupportJpaController.java
     * 3. Comment    : 관리자 지원모델 어드민 코멘트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 26.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "지원모델 어드민 코멘트 조회", notes = "지원모델 어드민 코멘트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "지원모델 어드민 코멘트 조회성공", response = List.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/admin-comment")
    public ResponseEntity<List<AdminCommentDTO>> findSupportAdminComment(@PathVariable Long idx) {
        return ResponseEntity.ok(adminSupportJpaService.findSupportAdminComment(idx));
    }
}
