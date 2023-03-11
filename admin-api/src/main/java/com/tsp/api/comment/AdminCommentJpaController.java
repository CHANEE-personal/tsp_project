package com.tsp.api.comment;

import com.tsp.api.comment.service.AdminCommentJpaService;
import com.tsp.api.comment.domain.AdminCommentDto;
import com.tsp.api.comment.domain.AdminCommentEntity;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.rmi.ServerError;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/comment")
@Api(tags = "어드민 코멘트 관련 API")
@RequiredArgsConstructor
public class AdminCommentJpaController {
    private final AdminCommentJpaService adminCommentJpaService;

    /**
     * <pre>
     * 1. MethodName : findAdminCommentList
     * 2. ClassName  : AdminCommentJpaController.java
     * 3. Comment    : 관리자 코멘트 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "어드민 코멘트 조회", notes = "어드민 코멘트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "어드민 코멘트 조회 성공", response = Page.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping
    public ResponseEntity<Page<AdminCommentDto>> findAdminCommentList(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        return ResponseEntity.ok().body(adminCommentJpaService.findAdminCommentList(paramMap, PageRequest.of(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : findOneAdminComment
     * 2. ClassName  : AdminCommentJpaController.java
     * 3. Comment    : 관리자 코멘트 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "어드민 코멘트 상세 조회", notes = "어드민 코멘트를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "어드민 코멘트 상세 조회 성공", response = AdminCommentDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public ResponseEntity<AdminCommentDto> findOneAdminComment(@PathVariable Long idx) {
        return ResponseEntity.ok(adminCommentJpaService.findOneAdminComment(idx));
    }

    /**
     * <pre>
     * 1. MethodName : updateAdminComment
     * 2. ClassName  : AdminCommentJpaController.java
     * 3. Comment    : 관리자 코멘트 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "어드민 코멘트 수정", notes = "어드민 코멘트를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "어드민 코멘트 수정 성공", response = AdminCommentDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping(value = "/{idx}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminCommentDto> updateAdminComment(@PathVariable Long idx, @Valid @RequestBody AdminCommentEntity adminCommentEntity) {
        return ResponseEntity.ok(adminCommentJpaService.updateAdminComment(idx, adminCommentEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteAdminComment
     * 2. ClassName  : AdminCommentJpaController.java
     * 3. Comment    : 관리자 코멘트 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "어드민 코멘트 삭제", notes = "어드민 코멘트를 삭제 한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "어드민 코멘트 삭제 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping(value = "/{idx}")
    public ResponseEntity<Long> deleteAdminComment(@PathVariable Long idx) {
        adminCommentJpaService.deleteAdminComment(idx);
        return ResponseEntity.noContent().build();
    }
}
