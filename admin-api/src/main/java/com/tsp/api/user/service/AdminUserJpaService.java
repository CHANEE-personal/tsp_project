package com.tsp.api.user.service;

import com.tsp.api.user.domain.AdminUserDTO;
import com.tsp.api.user.domain.AdminUserEntity;
import com.tsp.api.user.domain.LoginRequest;
import com.tsp.api.user.domain.SignUpRequest;
import com.tsp.jwt.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;

public interface AdminUserJpaService {

    /**
     * <pre>
     * 1. MethodName : findUserList
     * 2. ClassName  : AdminUserJpaService.java
     * 3. Comment    : Admin User 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    Page<AdminUserDTO> findUserList(Map<String, Object> userMap, PageRequest pageRequest);

    /**
     * <pre>
     * 1. MethodName : findOneUser
     * 2. ClassName  : AdminUserJpaService.java
     * 3. Comment    : Admin User 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    AdminUserDTO findOneUser(String id);

    /**
     * <pre>
     * 1. MethodName : adminLogin
     * 2. ClassName  : AdminUserJpaService.java
     * 3. Comment    : 관리자 로그인 처리
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    JwtUtil.TokenInfo adminLogin(LoginRequest loginRequest);

    /**
     * <pre>
     * 1. MethodName : insertAdminUser
     * 2. ClassName  : AdminUserJpaService.java
     * 3. Comment    : 관리자 회원가입
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 11.
     * </pre>
     */
    AdminUserDTO insertAdminUser(SignUpRequest signUpRequest);

    /**
     * <pre>
     * 1. MethodName : updateAdminUser
     * 2. ClassName  : AdminUserJpaService.java
     * 3. Comment    : 관리자 회원 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 11.
     * </pre>
     */
    AdminUserDTO updateAdminUser(Long idx, AdminUserEntity adminUserEntity);

    /**
     * <pre>
     * 1. MethodName : deleteAdminUser
     * 2. ClassName  : AdminUserJpaService.java
     * 3. Comment    : 관리자 회원 탈퇴
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 11.
     * </pre>
     */
    void deleteAdminUser(AdminUserEntity adminUserEntity);
}
