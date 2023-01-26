package com.tsp.api.support.controller;

import com.tsp.api.support.domain.FrontSupportDTO;
import com.tsp.api.support.domain.FrontSupportEntity;
import com.tsp.api.support.service.FrontSupportJpaApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.net.URI;
import java.rmi.ServerError;
import java.util.Map;

import static org.springframework.web.client.HttpClientErrorException.*;

@Validated
@RestController
@RequiredArgsConstructor
@Api(tags = "지원모델관련 API")
@RequestMapping("/api/support")
public class FrontSupportJpaApiController {
    private final FrontSupportJpaApiService frontSupportJpaApiService;

    /**
     * <pre>
     * 1. MethodName : insertSupportModel
     * 2. ClassName  : FrontSupportJpaApiController.java
     * 3. Comment    : 모델 지원하기
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 07.
     * </pre>
     */
    @ApiOperation(value = "지원모델 저장", notes = "지원모델을 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "지원모델 등록성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping
    public ResponseEntity<FrontSupportDTO> insertSupportModel(@Valid @RequestBody FrontSupportEntity frontSupportEntity) {
        return ResponseEntity.created(URI.create("")).body(frontSupportJpaApiService.insertSupportModel(frontSupportEntity));
    }
}
