package com.tsp.jwt;

import com.tsp.api.domain.user.AdminUserEntity;
import com.tsp.api.user.service.repository.AdminUserJpaRepository;
import com.tsp.exception.TspException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.tsp.exception.ApiExceptionType.NOT_FOUND_USER;
import static java.lang.Boolean.TRUE;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MyUserDetailsService userDetailsService;
    private final AdminUserJpaRepository adminUserJpaRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String accessToken = jwtUtil.resolveAccessToken(request);
            String refreshToken = jwtUtil.resolveRefreshToken(request);

            // 유효한 토큰인지 검사
            if (accessToken != null) {
                if (TRUE.equals(jwtUtil.validateToken(accessToken))) {
                    this.setAuthentication(accessToken);
                } else {
                    if (refreshToken != null) {
                        boolean validateRefreshToken = jwtUtil.validateToken(refreshToken);

                        AdminUserEntity user = adminUserJpaRepository.findByUserRefreshToken(refreshToken)
                                .orElseThrow(() -> new TspException(NOT_FOUND_USER));
                        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserId());

                        if (validateRefreshToken) {
                            String newAccessToken = jwtUtil.doGenerateToken(userDetails.getUsername());
                            jwtUtil.setHeaderAccessToken(response, newAccessToken);
                            this.setAuthentication(newAccessToken);
                        }
                    }
                }
            }
        } catch (ExpiredJwtException e) {
            log.info("Security exception for user {} - {}", e.getClaims().getSubject(), e.getMessage());
            response.setStatus(SC_UNAUTHORIZED);
            log.debug("Exception " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new TspException(NOT_FOUND_USER);
        }
        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String token) {
        Authentication authentication = jwtUtil.getAuthentication(token);
        // SecurityContext에 Authentication 객체 저장
        getContext().setAuthentication(authentication);
    }
}
