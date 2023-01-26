package com.tsp.api.notice;

import com.tsp.api.notice.domain.AdminNoticeDTO;
import com.tsp.api.notice.domain.AdminNoticeEntity;
import com.tsp.api.notice.service.AdminNoticeJpaService;
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
import java.net.URI;
import java.rmi.ServerError;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/notice")
@Api(tags = "공지사항 관련 API")
@RequiredArgsConstructor
public class AdminNoticeJpaController {
    private final AdminNoticeJpaService adminNoticeJpaService;

    /**
     * <pre>
     * 1. MethodName : findNoticeList
     * 2. ClassName  : AdminNoticeJpaController.java
     * 3. Comment    : 관리자 공지사항 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "공지사항 조회", notes = "공지사항을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping
    public ResponseEntity<Page<AdminNoticeDTO>> findNoticeList(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        return ResponseEntity.ok().body(adminNoticeJpaService.findNoticeList(paramMap, PageRequest.of(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : findOneNotice
     * 2. ClassName  : AdminNoticeJpaController.java
     * 3. Comment    : 관리자 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "공지사항 상세 조회", notes = "공지사항을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 상세 조회 성공", response = AdminNoticeDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public ResponseEntity<AdminNoticeDTO> findOneNotice(@PathVariable Long idx) {
        return ResponseEntity.ok(adminNoticeJpaService.findOneNotice(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneNotice
     * 2. ClassName  : AdminNoticeJpaController.java
     * 3. Comment    : 관리자 이전 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 18.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "이전 공지사항 상세 조회", notes = "이전 공지사항을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "이전 공지사항 상세 조회 성공", response = AdminNoticeDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/prev")
    public ResponseEntity<AdminNoticeDTO> findPrevOneNotice(@PathVariable Long idx) {
        return ResponseEntity.ok(adminNoticeJpaService.findPrevOneNotice(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneNotice
     * 2. ClassName  : AdminNoticeJpaController.java
     * 3. Comment    : 관리자 다음 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 18.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "다음 공지사항 상세 조회", notes = "다음 공지사항을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "다음 공지사항 상세 조회 성공", response = AdminNoticeDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/next")
    public ResponseEntity<AdminNoticeDTO> findNextOneNotice(@PathVariable Long idx) {
        return ResponseEntity.ok(adminNoticeJpaService.findNextOneNotice(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertNotice
     * 2. ClassName  : AdminNoticeJpaController.java
     * 3. Comment    : 관리자 공지사항 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "공지사항 저장", notes = "공지사항을 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "공지사항 등록성공", response = AdminNoticeDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminNoticeDTO> insertNotice(@Valid @RequestBody AdminNoticeEntity adminNoticeEntity) {
        return ResponseEntity.created(URI.create("")).body(adminNoticeJpaService.insertNotice(adminNoticeEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateNotice
     * 2. ClassName  : AdminNoticeJpaController.java
     * 3. Comment    : 관리자 공지사항 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "공지사항 수정", notes = "공지사항을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 수정 성공", response = AdminNoticeDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping(value = "/{idx}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminNoticeDTO> updateNotice(@PathVariable Long idx, @Valid @RequestBody AdminNoticeEntity adminNoticeEntity) {
        return ResponseEntity.ok(adminNoticeJpaService.updateNotice(idx, adminNoticeEntity));
    }

    /**
     * <pre>
     * 1. MethodName : toggleFixed
     * 2. ClassName  : AdminNoticeJpaController.java
     * 3. Comment    : 관리자 공지사항 상단 고정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 23.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "공지사항 상단 고정", notes = "공지사항을 상단 고정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공지사항 상단 고정 성공", response = Boolean.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping(value = "/{idx}/fixed")
    public ResponseEntity<Boolean> toggleFixed(@PathVariable Long idx) {
        return ResponseEntity.ok(adminNoticeJpaService.toggleFixed(idx));
    }

    /**
     * <pre>
     * 1. MethodName : deleteNotice
     * 2. ClassName  : AdminNoticeJpaController.java
     * 3. Comment    : 관리자 공지사항 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "공지사항 삭제", notes = "공지사항을 삭제 한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "공지사항 삭제 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping(value = "/{idx}")
    public ResponseEntity<Long> deleteNotice(@PathVariable Long idx) {
        adminNoticeJpaService.deleteNotice(idx);
        return ResponseEntity.noContent().build();
    }
}
