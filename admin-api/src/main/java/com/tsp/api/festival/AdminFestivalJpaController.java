package com.tsp.api.festival;

import com.tsp.api.festival.domain.AdminFestivalDto;
import com.tsp.api.festival.domain.AdminFestivalEntity;
import com.tsp.api.festival.service.AdminFestivalJpaService;
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


@RestController
@RequestMapping("/api/festival")
@Api(tags = "행사 관련 API")
@RequiredArgsConstructor
public class AdminFestivalJpaController {

    private final AdminFestivalJpaService adminFestivalJpaService;

    /**
     * <pre>
     * 1. MethodName : findFestivalList
     * 2. ClassName  : AdminFestivalJpaController.java
     * 3. Comment    : 관리자 행사 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "행사 리스트 조회", notes = "행사 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "행사 리스트 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping
    public ResponseEntity<Page<AdminFestivalDto>> findFestivalList(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        return ResponseEntity.ok().body(adminFestivalJpaService.findFestivalList(paramMap, PageRequest.of(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : findOneFestival
     * 2. ClassName  : AdminFestivalJpaController.java
     * 3. Comment    : 관리자 행사 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "행사 상세 조회", notes = "행사 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "행사 상세 조회 성공", response = AdminFestivalDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}")
    public ResponseEntity<AdminFestivalDto> findOneFestival(@PathVariable Long idx) {
        return ResponseEntity.ok(adminFestivalJpaService.findOneFestival(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertFestival
     * 2. ClassName  : AdminFestivalJpaController.java
     * 3. Comment    : 관리자 행사 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "행사 등록", notes = "행사를 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "행사 등록 성공", response = AdminFestivalDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public ResponseEntity<AdminFestivalDto> insertFestival(@Valid @RequestBody AdminFestivalEntity adminFestivalEntity) {
        return ResponseEntity.created(URI.create("")).body(adminFestivalJpaService.insertFestival(adminFestivalEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateFestival
     * 2. ClassName  : AdminFestivalJpaController.java
     * 3. Comment    : 관리자 행사 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "행사 수정", notes = "행사를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "행사 수정 성공", response = AdminFestivalDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public ResponseEntity<AdminFestivalDto> updateFestival(@PathVariable Long idx, @Valid @RequestBody AdminFestivalEntity adminFestivalEntity) {
        return ResponseEntity.ok(adminFestivalJpaService.updateFestival(idx, adminFestivalEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteFestival
     * 2. ClassName  : AdminFestivalJpaController.java
     * 3. Comment    : 관리자 행사 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 09.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "행사 삭제", notes = "행사를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "행사 삭제 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = HttpClientErrorException.NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public ResponseEntity<Long> deleteFestival(@PathVariable Long idx) {
        adminFestivalJpaService.deleteFestival(idx);
        return ResponseEntity.noContent().build();
    }
}
