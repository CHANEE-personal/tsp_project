package com.tsp.jwt;

import com.tsp.api.user.domain.AdminUserEntity;
import com.tsp.api.user.domain.AuthenticationRequest;
import com.tsp.api.user.service.repository.AdminUserJpaRepository;
import com.tsp.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tsp.exception.ApiExceptionType.NOT_FOUND_USER;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyUserDetailsService implements UserDetailsService {

    private final AdminUserJpaRepository adminUserJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        try {
            AdminUserEntity adminUserEntity = adminUserJpaRepository.findByUserId(id)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with id"));

            // 아이디 일치하는지 확인
            return new AuthenticationRequest(adminUserEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_USER);
        }
    }
}
