package com.tsp.jwt;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
class JwtUtilTest {
    private final MockHttpServletResponse response = new MockHttpServletResponse();
    private final MyUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private UserDetails userDetails;

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void setUp() {
//        userDetails = userDetailsService.loadUserByUsername(AuthenticationRequest.builder().userId("admin02").password("pass1234").build().getUserId());
    }

    @Test
    @DisplayName("토큰 만료 테스트")
    void isTokenExpiredTest() {
        String token = jwtUtil.doGenerateToken(userDetails.getUsername());
        assertFalse(jwtUtil.isTokenExpired(token));
    }

    @Test
    @DisplayName("토큰 생성 테스트")
    void generateTokenTest() {
        assertNotNull(jwtUtil.doGenerateToken(userDetails.getUsername()));
    }

    @Test
    @DisplayName("리프레시 토큰 생성 테스트")
    void generateRefreshTokenTest() {
        assertNotNull(jwtUtil.doGenerateToken(userDetails.getUsername()));
    }

    @Test
    void extractAllClaimsTest() {
        String token = jwtUtil.doGenerateToken(userDetails.getUsername());
        assertNotNull(jwtUtil.extractAllClaims(token));
    }

    @Test
    @DisplayName("토큰 유효성 테스트")
    void validateTokenTest() {
        String token = jwtUtil.doGenerateToken(userDetails.getUsername());
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    @DisplayName("엑세스 토큰 헤더 설정 테스트")
    void setHeaderAccessTokenTest() {
        String token = jwtUtil.doGenerateToken(userDetails.getUsername());
        jwtUtil.setHeaderAccessToken(response, token);
        assertNotNull(response.getHeader("Authorization"));
        assertThat("Bearer " + token).isEqualTo(response.getHeader("Authorization"));
    }

    @Test
    @DisplayName("엑세스 리프레시 토큰 헤더 설정 테스트")
    void setHeaderRefreshTokenTest() {
        String refreshToken = jwtUtil.doGenerateRefreshToken(userDetails.getUsername());
        jwtUtil.setHeaderRefreshToken(response, refreshToken);
        assertNotNull(response.getHeader("RefreshToken"));
        assertThat("Bearer " + refreshToken).isEqualTo(response.getHeader("refreshToken"));
    }
}
