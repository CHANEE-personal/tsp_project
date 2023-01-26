package com.tsp.api.user;

import com.tsp.api.user.domain.*;
import com.tsp.api.user.service.AdminUserJpaService;
import com.tsp.common.CurrentUser;
import com.tsp.common.Paging;
import com.tsp.jwt.AuthenticationResponse;
import com.tsp.jwt.JwtUtil;
import com.tsp.jwt.MyUserDetailsService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.rmi.ServerError;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.client.HttpClientErrorException.*;

@RestController
@Api(tags = "관리자 회원 관련 API")
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AdminUserJpaController {
    private final AdminUserJpaService adminUserJpaService;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final JwtUtil jwtTokenUtil;

    /**
     * <pre>
     * 1. MethodName : findUserList
     * 2. ClassName  : AdminUserJpaController.java
     * 3. Comment    : Admin User 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ApiOperation(value = "Admin 회원 조회", notes = "Admin 회원을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "관리자 회원 조회 성공", response = Page.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @GetMapping
    public ResponseEntity<Page<AdminUserDTO>> findUserList(@RequestParam(required = false) Map<String, Object> paramMap, Paging paging) {
        return ok(adminUserJpaService.findUserList(paramMap, PageRequest.of(paging.getPageNum(), paging.getSize())));
    }

    /**
     * <pre>
     * 1. MethodName : login
     * 2. ClassName  : AdminUserJpaController.java
     * 3. Comment    : Admin User 로그인 처리
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @ApiOperation(value = "Admin 회원 로그인 처리", notes = "Admin 회원을 로그인 처리한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "관리자 회원 상세 조회 성공", response = Map.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping("/login")
    public ResponseEntity<JwtUtil.TokenInfo> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) throws Exception {
        JwtUtil.TokenInfo jwtResponse = adminUserJpaService.adminLogin(loginRequest);
        jwtTokenUtil.setHeaderAccessToken(response, jwtResponse.getAccessToken());
        jwtTokenUtil.setHeaderRefreshToken(response, jwtResponse.getRefreshToken());
        return ok().body(jwtResponse);
        }

    @ApiOperation(value = "JWT 토큰 재발급", notes = "JWT 토큰을 재발급")
    @PostMapping(value = "/refresh")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "X-REFRESH-TOKEN", value = "refresh-token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<?> createAuthenticationRefreshToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        // id, password 인증
        authenticate(authenticationRequest.getUserId(), authenticationRequest.getPassword());

        // 사용자 정보 조회 후 token 생성
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetailsService.loadUserByUsername(authenticationRequest.getUserId()));

        return ok(new AuthenticationResponse(refreshToken));
    }

    /**
     * <pre>
     * 1. MethodName : authenticate
     * 2. ClassName  : AdminUserJpaController.java
     * 3. Comment    : 관리자 로그인 시 인증
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    private void authenticate(String id, String password) throws Exception {
        try {
            Authentication request = new UsernamePasswordAuthenticationToken(id, password);
            if (request.getName().equals(request.getCredentials())) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getName(), request.getCredentials()));
            }
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED");
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS");
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertAdminUser
     * 2. ClassName  : AdminUserJpaController.java
     * 3. Comment    : 관리자 회원가입
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 11.
     * </pre>
     */
    @ApiOperation(value = "Admin 회원가입 처리", notes = "Admin 회원가입을 처리한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "회원가입 성공", response = AdminUserDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PostMapping("/signUp")
    public ResponseEntity<AdminUserDTO> insertAdminUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.created(URI.create("")).body(adminUserJpaService.insertAdminUser(signUpRequest));
    }

    /**
     * <pre>
     * 1. MethodName : updateAdminUser
     * 2. ClassName  : AdminUserJpaController.java
     * 3. Comment    : 관리자 회원 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 11.
     * </pre>
     */
    @ApiOperation(value = "Admin 회원 수정 처리", notes = "Admin 회원 수정 처리한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "회원 수정 성공", response = AdminUserDTO.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @PutMapping("/{idx}")
    public ResponseEntity<AdminUserDTO> updateAdminUser(@PathVariable Long idx, @CurrentUser AdminUserEntity adminUserEntity) {
        return ok(adminUserJpaService.updateAdminUser(idx, adminUserEntity));
    }

    /**
     * <pre>
     * 1. MethodName : deleteAdminUser
     * 2. ClassName  : AdminUserJpaController.java
     * 3. Comment    : 관리자 회원 탈퇴
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 11.
     * </pre>
     */
    @ApiOperation(value = "Admin 회원 탈퇴 처리", notes = "Admin 회원 탈퇴 처리한다.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "회원 탈퇴 성공", response = Long.class),
            @ApiResponse(code = 400, message = "잘못된 요청", response = BadRequest.class),
            @ApiResponse(code = 401, message = "허용되지 않는 관리자", response = Unauthorized.class),
            @ApiResponse(code = 403, message = "접근거부", response = HttpClientErrorException.class),
            @ApiResponse(code = 404, message = "존재 하지 않음", response = NotFound.class),
            @ApiResponse(code = 500, message = "서버 에러", response = ServerError.class)
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteAdminUser(@CurrentUser AdminUserEntity adminUserEntity) {
        adminUserJpaService.deleteAdminUser(adminUserEntity);
        return ResponseEntity.noContent().build();
    }
}
