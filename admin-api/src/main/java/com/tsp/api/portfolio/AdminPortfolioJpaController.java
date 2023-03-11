package com.tsp.api.portfolio;

import com.tsp.api.portfolio.domain.AdminPortFolioDto;
import com.tsp.api.portfolio.domain.AdminPortFolioEntity;
import com.tsp.api.portfolio.service.AdminPortfolioJpaService;
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

import static org.springframework.web.client.HttpClientErrorException.*;

@RestController
@RequestMapping("/api/portfolio")
@Api(tags = "포트폴리오 관련 API")
@RequiredArgsConstructor
public class AdminPortfolioJpaController {

    private final AdminPortfolioJpaService adminPortfolioJpaService;

    /**
     * <pre>
     * 1. MethodName : findPortfolioList
     * 2. ClassName  : AdminPortfolioJpaController.java
     * 3. Comment    : 관리자 포트폴리오 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 14.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "포트폴리오 조회", notes = "포트폴리오를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "포트폴리오 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping
    public ResponseEntity<Page<AdminPortFolioDto>> findPortfolioList(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        return ResponseEntity.ok().body(adminPortfolioJpaService.findPortfolioList(paramMap, PageRequest.of(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : findOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaController.java
     * 3. Comment    : 관리자 포트폴리오 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 18.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "포트폴리오 상세 조회", notes = "포트폴리오를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "포트폴리오 상세 조회 성공", response = AdminPortFolioDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping(value = "/{idx}")
    public ResponseEntity<AdminPortFolioDto> findOnePortfolio(@PathVariable Long idx) {
        return ResponseEntity.ok(adminPortfolioJpaService.findOnePortfolio(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOnePortfolio
     * 2. ClassName  : AdminProductionJpaController.java
     * 3. Comment    : 관리자 이전 포트폴리오 상세
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 14.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "이전 포트폴리오 상세 조회", notes = "이전 포트폴리오를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "이전 포트폴리오 상세조회 성공", response = AdminPortFolioDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/prev")
    public ResponseEntity<AdminPortFolioDto> findPrevOnePortfolio(@PathVariable Long idx) {
        return ResponseEntity.ok(adminPortfolioJpaService.findPrevOnePortfolio(idx));
    }

    /**
     * <pre>
     * 1. MethodName : findNextOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaController.java
     * 3. Comment    : 관리자 다음 포트폴리오 상세
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 14.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "다음 포트폴리오 상세 조회", notes = "다음 포트폴리오를 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "다음 포트폴리오 상세조회 성공", response = AdminPortFolioDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping("/{idx}/next")
    public ResponseEntity<AdminPortFolioDto> findNextOnePortfolio(@PathVariable Long idx) {
        return ResponseEntity.ok(adminPortfolioJpaService.findNextOnePortfolio(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertPortfolio
     * 2. ClassName  : AdminPortfolioJpaController.java
     * 3. Comment    : 관리자 포트폴리오 draft 상태로 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 18.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "포트폴리오 저장", notes = "포트폴리오를 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "포트폴리오 등록성공", response = AdminPortFolioDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public ResponseEntity<AdminPortFolioDto> insertPortfolio(@Valid @RequestBody AdminPortFolioEntity adminPortFolioEntity) {
        return ResponseEntity.created(URI.create("")).body(adminPortfolioJpaService.insertPortfolio(adminPortFolioEntity));
    }

    /**
     * <pre>
     * 1. MethodName : updatePortfolio
     * 2. ClassName  : AdminPortfolioJpaController.java
     * 3. Comment    : 관리자 포트폴리오 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 18.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "포트폴리오 수정", notes = "포트폴리오를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "포트폴리오 수정성공", response = AdminPortFolioDto.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public ResponseEntity<AdminPortFolioDto> updatePortfolio(@PathVariable Long idx, @Valid @RequestBody AdminPortFolioEntity adminPortFolioEntity) {
        return ResponseEntity.ok(adminPortfolioJpaService.updatePortfolio(idx, adminPortFolioEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deletePortfolio
     * 2. ClassName  : AdminPortfolioJpaController.java
     * 3. Comment    : 관리자 포트폴리오 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 18.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "포트폴리오 삭제", notes = "포트폴리오를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "포트폴리오 삭제성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping("/{idx}")
    public ResponseEntity<Long> deletePortfolio(@PathVariable Long idx) {
        adminPortfolioJpaService.deletePortfolio(idx);
        return ResponseEntity.noContent().build();
    }
}
