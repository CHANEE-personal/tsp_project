package com.tsp.api.common;

import com.tsp.api.common.service.AdminCommonJpaService;
import com.tsp.api.domain.common.NewCodeDTO;
import com.tsp.api.domain.common.NewCodeEntity;
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
import java.util.Map;

@Validated
@RestController
@Api(tags = "공통코드 관련 API")
@RequestMapping("/api/common")
@RequiredArgsConstructor
public class AdminCommonJpaController {

    private final AdminCommonJpaService adminCommonJpaService;

    /**
     * <pre>
     * 1. MethodName : commonCodeList
     * 2. ClassName  : AdminCommonJpaController.java
     * 3. Comment    : 관리자 공통 코드 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "공통 코드 리스트 조회", notes = "공통 코드 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공통 코드 리스트 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/lists")
    public ResponseEntity<Page<NewCodeDTO>> commonCodeList(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        return ResponseEntity.ok().body(adminCommonJpaService.findCommonCodeList(paramMap, PageRequest.of(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : findOneCommon
     * 2. ClassName  : AdminCommonJpaController.java
     * 3. Comment    : 관리자 공통 코드 상세
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "공통코드 상세 조회", notes = "공통코드를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공통코드 상세 조회 성공", response = NewCodeDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public ResponseEntity<NewCodeDTO> findOneCommon(@PathVariable Long idx) {
        return ResponseEntity.ok(adminCommonJpaService.findOneCommonCode(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertCommonCode
     * 2. ClassName  : AdminCommonJpaController.java
     * 3. Comment    : 관리자 공통 코드 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "공통코드 저장", notes = "공통코드를 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "공통코드 등록성공", response = NewCodeDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public ResponseEntity<NewCodeDTO> insertCommonCode(@Valid @RequestBody NewCodeEntity newCodeEntity) {
        return ResponseEntity.created(URI.create("")).body(adminCommonJpaService.insertCommonCode(newCodeEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateCommonCode
     * 2. ClassName  : AdminCommonJpaController.java
     * 3. Comment    : 관리자 공통 코드 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "공통코드 수정", notes = "공통코드를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "공통코드 수정성공", response = NewCodeDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public ResponseEntity<NewCodeDTO> updateCommonCode(@PathVariable Long idx, @Valid @RequestBody NewCodeEntity commonCodeEntity) {
        return ResponseEntity.ok(adminCommonJpaService.updateCommonCode(idx, commonCodeEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteCommonCode
     * 2. ClassName  : AdminCommonJpaController.java
     * 3. Comment    : 관리자 공통코드 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "공통코드 삭제", notes = "공통코드를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "공통코드 삭제성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public ResponseEntity<Long> deleteCommonCode(@PathVariable Long idx) {
        adminCommonJpaService.deleteCommonCode(idx);
        return ResponseEntity.noContent().build();
    }
}
