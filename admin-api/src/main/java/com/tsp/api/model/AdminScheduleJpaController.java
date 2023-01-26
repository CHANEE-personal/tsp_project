package com.tsp.api.model;

import com.tsp.api.model.domain.schedule.AdminScheduleDTO;
import com.tsp.api.model.domain.schedule.AdminScheduleEntity;
import com.tsp.api.model.service.schedule.AdminScheduleJpaService;
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
@Api(tags = "모델 스케줄 관련 API")
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class AdminScheduleJpaController {

    private final AdminScheduleJpaService adminScheduleJpaService;

    /**
     * <pre>
     * 1. MethodName : findScheduleList
     * 2. ClassName  : AdminScheduleJpaController.java
     * 3. Comment    : 관리자 모델 스케줄 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "모델 스케줄 조회", notes = "모델 스케줄을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 스케줄 조회성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping
    public ResponseEntity<Page<AdminScheduleDTO>> findScheduleList(@RequestParam(required = false) Map<String, Object> paramMap,
                                                                   @RequestParam(value = "searchStartTime", required = false) String searchStartTime,
                                                                   @RequestParam(value = "searchEndTime", required = false) String searchEndTime,
                                                                   Paging paging) {

        if (searchStartTime != null && searchEndTime != null) {
            paramMap.put("searchStartTime", searchStartTime);
            paramMap.put("searchEndTime", searchEndTime);
        }

        return ResponseEntity.ok().body(adminScheduleJpaService.findScheduleList(paramMap, PageRequest.of(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : findOneSchedule
     * 2. ClassName  : AdminScheduleJpaController.java
     * 3. Comment    : 관리자 모델 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "모델 스케줄 상세 조회", notes = "모델 스케줄을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 스케줄 상세 조회 성공", response = AdminScheduleDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}")
    public ResponseEntity<AdminScheduleDTO> findOneSchedule(@PathVariable Long idx) {
        return ResponseEntity.ok(adminScheduleJpaService.findOneSchedule(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneSchedule
     * 2. ClassName  : AdminScheduleJpaController.java
     * 3. Comment    : 관리자 모델 이전 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 22.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "이전 모델 스케줄 상세 조회", notes = "이전 모델 스케줄을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "이전 모델 스케줄 상세 조회 성공", response = AdminScheduleDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/prev")
    public ResponseEntity<AdminScheduleDTO> findPrevOneSchedule(@PathVariable Long idx) {
        return ResponseEntity.ok(adminScheduleJpaService.findPrevOneSchedule(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneSchedule
     * 2. ClassName  : AdminScheduleJpaController.java
     * 3. Comment    : 관리자 모델 다음 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 22.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "다음 모델 스케줄 상세 조회", notes = "다음 모델 스케줄을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "다음 모델 스케줄 상세 조회 성공", response = AdminScheduleDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/next")
    public ResponseEntity<AdminScheduleDTO> findNextOneSchedule(@PathVariable Long idx) {
        return ResponseEntity.ok(adminScheduleJpaService.findNextOneSchedule(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertSchedule
     * 2. ClassName  : AdminScheduleJpaController.java
     * 3. Comment    : 관리자 모델 스켸줄 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "모델 스케줄 저장", notes = "모델 스케줄을 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "모델 스케줄 등록성공", response = AdminScheduleDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping("/model/{idx}")
    public ResponseEntity<AdminScheduleDTO> insertSchedule(@PathVariable Long idx, @Valid @RequestBody AdminScheduleEntity adminScheduleEntity) throws Exception {
        return ResponseEntity.created(URI.create("")).body(adminScheduleJpaService.insertSchedule(idx, adminScheduleEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updateSchedule
     * 2. ClassName  : AdminScheduleJpaController.java
     * 3. Comment    : 관리자 모델 스케줄 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "모델 스케줄 수정", notes = "모델 스케줄을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "모델 스케줄 수정성공", response = AdminScheduleDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public ResponseEntity<AdminScheduleDTO> updateSchedule(@Valid @RequestBody AdminScheduleEntity adminScheduleEntity) {
        return ResponseEntity.ok(adminScheduleJpaService.updateSchedule(adminScheduleEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteSchedule
     * 2. ClassName  : AdminScheduleJpaController.java
     * 3. Comment    : 관리자 모델 스케줄 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "모델 스케줄 삭제", notes = "모델 스케줄을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "모델 스케줄 삭제성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = HttpClientErrorException.BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = HttpClientErrorException.Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public ResponseEntity<Long> deleteSchedule(@PathVariable Long idx) {
        adminScheduleJpaService.deleteSchedule(idx);
        return ResponseEntity.noContent().build();
    }
}
