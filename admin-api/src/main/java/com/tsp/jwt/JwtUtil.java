package com.tsp.jwt;

import com.tsp.api.domain.user.AdminUserEntity;
import com.tsp.api.user.service.repository.AdminUserJpaRepository;
import com.tsp.exception.TspException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.security.Key;
import java.util.Date;
import java.util.Objects;

import static com.tsp.exception.ApiExceptionType.NOT_FOUND_USER;
import static io.jsonwebtoken.Jwts.*;
import static io.jsonwebtoken.Jwts.parserBuilder;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil implements Serializable {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ACCESS_TOKEN = "X-ACCESS-TOKEN";
    public static final String REFRESH_TOKEN = "X-REFRESH-TOKEN";
    public final static long TOKEN_VALIDATION_SECOND = 1000L * 10;
    public final static long REFRESH_TOKEN_VALIDATION_SECOND = 1000L * 60 * 24 * 2;
    private final MyUserDetailsService myUserDetailsService;
    private final AdminUserJpaRepository adminUserJpaRepository;

    @Getter
    public static class TokenInfo {

        private static final String type = TOKEN_PREFIX;
        private String accessToken;
        private String refreshToken;

        public TokenInfo(String accessToken, String refreshToken) {
            this.accessToken = TOKEN_PREFIX + accessToken;
            this.refreshToken = TOKEN_PREFIX + refreshToken;
        }
    }

    public TokenInfo getJwtTokens(String accessToken, String refreshToken) {
        return new TokenInfo(accessToken, refreshToken);
    }

    @Value("${spring.jwt.secret}")
    private String SECRET_KEY;

    private Key getSigningKey(String secretKey) {
        return hmacShaKeyFor(secretKey.getBytes(UTF_8));
    }

    /**
     * <pre>
     * 1. MethodName : extractAllClaims
     * 2. ClassName  : JwtUtil.java
     * 3. Comment    : JWT 토큰이 유효한 토큰인지 검사한 후, 토큰에 담긴 Payload 값을 가져온다
     * 4. 작성자      : CHO
     * 5. 작성일      : 2021. 07. 07.
     * </pre>
     */
    public Claims extractAllClaims(String token) throws ExpiredJwtException {
        return parserBuilder()
                .setSigningKey(getSigningKey(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String resolveAccessToken(HttpServletRequest request) {
        if (request.getHeader("Authorization") != null && !Objects.equals(request.getHeader("Authorization"), "")) {
            return request.getHeader("Authorization").substring(7);
        }
        return null;
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        if (request.getHeader("refreshToken") != null && !Objects.equals(request.getHeader("refreshToken"), "")) {
            return request.getHeader("refreshToken").substring(7);
        }
        return null;
    }

    /**
     * <pre>
     * 1. MethodName : isTokenExpired
     * 2. ClassName  : JwtUtil.java
     * 3. Comment    : 만료된 토큰인지 체크
     * 4. 작성자      : CHO
     * 5. 작성일      : 2021. 07. 07.
     * </pre>
     */
    public Boolean isTokenExpired(String token) {
        try {
            return extractAllClaims(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    /**
     * <pre>
     * 1. MethodName : generateToken
     * 2. ClassName  : JwtUtil.java
     * 3. Comment    : 토큰 발급
     * 4. 작성자      : CHO
     * 5. 작성일      : 2021. 07. 07.
     * </pre>
     */
    public String generateToken(UserDetails userDetails) {
        return doGenerateToken(userDetails.getUsername());
    }

    /**
     * <pre>
     * 1. MethodName : generateRefreshToken
     * 2. ClassName  : JwtUtil.java
     * 3. Comment    : 리프레시 토큰 재발급
     * 4. 작성자      : CHO
     * 5. 작성일      : 2021. 07. 07.
     * </pre>
     */
    public String generateRefreshToken(UserDetails userDetails) {
        return doGenerateToken(userDetails.getUsername());
    }

    /**
     * <pre>
     * 1. MethodName : doGenerateToken
     * 2. ClassName  : JwtUtil.java
     * 3. Comment    : 토큰 발급
     * 4. 작성자      : CHO
     * 5. 작성일      : 2021. 07. 07.
     * </pre>
     */
    public String doGenerateToken(String userPk) {
        Claims claims = Jwts.claims().setSubject(userPk);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TOKEN_VALIDATION_SECOND))
                .signWith(getSigningKey(SECRET_KEY), HS256)
                .compact();
    }

    public String doGenerateRefreshToken(String userPk) {
        Claims claims = Jwts.claims().setSubject(userPk);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALIDATION_SECOND))
                .signWith(getSigningKey(SECRET_KEY), HS256)
                .compact();
    }

    /**
     * <pre>
     * 1. MethodName : validateToken
     * 2. ClassName  : JwtUtil.java
     * 3. Comment    : JWT 토큰 검증
     * 4. 작성자      : CHO
     * 5. 작성일      : 2021. 07. 07.
     * </pre>
     */
    public Boolean validateToken(String token) {
        try {
            parser().setSigningKey(getSigningKey(SECRET_KEY)).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    /**
     * <pre>
     * 1. MethodName : getAuthentication
     * 2. ClassName  : JwtUtil.java
     * 3. Comment    : JWT 에서 인증 정보 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2021. 07. 07.
     * </pre>
     */
    public Authentication getAuthentication(String token) throws TspException {
        try {
            AdminUserEntity oneUser = adminUserJpaRepository.findByUserToken(token)
                    .orElseThrow(() -> new TspException(NOT_FOUND_USER));

            UserDetails userDetails = myUserDetailsService.loadUserByUsername(oneUser.getUserId());
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_USER);
        }
    }

    /**
     * <pre>
     * 1. MethodName : setHeaderAccessToken
     * 2. ClassName  : JwtUtil.java
     * 3. Comment    : Header 토큰 정보 세팅
     * 4. 작성자      : CHO
     * 5. 작성일      : 2021. 07. 07.
     * </pre>
     */
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader(ACCESS_TOKEN, TOKEN_PREFIX + accessToken);
    }

    /**
     * <pre>
     * 1. MethodName : setHeaderRefreshToken
     * 2. ClassName  : JwtUtil.java
     * 3. Comment    : Header 리프레시 토큰 정보 세팅
     * 4. 작성자      : CHO
     * 5. 작성일      : 2021. 07. 07.
     * </pre>
     */
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader(REFRESH_TOKEN, TOKEN_PREFIX + refreshToken);
    }
}
