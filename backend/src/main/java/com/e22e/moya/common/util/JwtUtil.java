package com.e22e.moya.common.util;

import com.e22e.moya.common.entity.Users;
import com.e22e.moya.user.UserRepository;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {

    public enum TokenStatus {
        VALID,
        INVALID,
        NOT_FOUND,
        EXPIRED
    }

    private final UserRepository userRepository;

    public JwtUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private long accessTokenExpiration;
    private long refreshTokenExpiration;
    private SecretKey key;

    @PostConstruct
    public void init() {
        Dotenv dotenv = Dotenv.load();
        String jwtSecret = dotenv.get("JWT_SECRET");
        if (jwtSecret == null || jwtSecret.isEmpty()) {
            throw new IllegalStateException("JWT_SECRET가 환경 변수에 설정되지 않았음.");
        }
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        String accessTokenExpirationStr = dotenv.get("ACCESS_TOKEN_EXPIRATION");
        if (accessTokenExpirationStr == null || accessTokenExpirationStr.isEmpty()) {
            throw new IllegalStateException("ACCESS_TOKEN_EXPIRATION이 환경 변수에 설정되지 않았음.");
        }
        this.accessTokenExpiration = Long.parseLong(accessTokenExpirationStr);

        String refreshTokenExpirationStr = dotenv.get("REFRESH_TOKEN_EXPIRATION");
        if (refreshTokenExpirationStr == null || refreshTokenExpirationStr.isEmpty()) {
            throw new IllegalStateException("REFRESH_TOKEN_EXPIRATION이 환경 변수에 설정되지 않았음.");
        }
        this.refreshTokenExpiration = Long.parseLong(refreshTokenExpirationStr);

        log.info("JWT 액세스 토큰 만료 시간: {}ms, 리프레시 토큰 만료 시간: {}ms",
            this.accessTokenExpiration, this.refreshTokenExpiration);
    }

    // refresh token 생성
    public String generateRefreshToken(String email) {
        return Jwts.builder()
            .issuer("MoYa")
            .subject("Refresh Token")
            .claim("email", email)
            .issuedAt(new Date())
            .expiration(
                new Date(System.currentTimeMillis() + refreshTokenExpiration))
            .signWith(key)
            .compact();
    }

    // 토큰 검증
    public Claims validateToken(String token) {
        try {
            return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (JwtException e) {
            log.error("토큰 검증 실패: {}", e.getMessage());
            throw e;
        }
    }

    public String extractToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return bearerToken; // 또는 null을 반환하거나 예외를 던질 수 있습니다.
    }

    // 토큰에서 이메일 추출
    public String getEmailFromToken(String bearerToken) {
        String token = extractToken(bearerToken);
        Claims claims = validateToken(token);
        return claims.get("email", String.class);

    }

    public long getUserIdFromToken(String bearerToken) {
        String token = extractToken(bearerToken);
        Claims claims = validateToken(token);
        String email = claims.get("email", String.class);

        Optional<Users> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            return userOptional.get().getId();
        } else {
            throw new RuntimeException("해당 email의 유저를 찾을 수 없음: " + email);
        }
    }


    public String generateAccessToken(String email) {
        String token = Jwts.builder()
            .issuer("MoYa")
            .subject("JWT Token")
            .claim("email", email)
            .issuedAt(new Date())
            .expiration(
                new Date(System.currentTimeMillis() + accessTokenExpiration))
            .signWith(key)
            .compact();

        // Redis에 토큰 저장 (동시성 고려)
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForValue()
                    .set(email, token, accessTokenExpiration,
                        TimeUnit.MILLISECONDS);
                return operations.exec();
            }
        });

        return token;
    }

    public void invalidateToken(String email) {
        // Redis에서 토큰 삭제 (동시성 고려)
        redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.delete(email);
                return operations.exec();
            }
        });
    }


    public TokenStatus isTokenValid(String token) {
        try {

            Claims claims = validateToken(token);
            String email = claims.get("email", String.class);

            return redisTemplate.execute(new SessionCallback<TokenStatus>() {
                @Override
                public TokenStatus execute(RedisOperations operations) throws DataAccessException {
                    operations.multi();
                    String storedToken = (String) operations.opsForValue().get(email);
                    List<Object> results = operations.exec();
                    if (results != null && !results.isEmpty()) {
                        String retrievedToken = (String) results.get(0);
                        if (retrievedToken == null) {
                            return TokenStatus.NOT_FOUND;
                        }
                        return token.equals(retrievedToken) ? TokenStatus.VALID
                            : TokenStatus.INVALID;
                    }
                    return TokenStatus.NOT_FOUND;
                }
            });
        } catch (ExpiredJwtException e) {
            return TokenStatus.EXPIRED;
        } catch (Exception e) {
            return TokenStatus.INVALID;
        }
    }

    public boolean logout(String token) {
        try {
            String email = getEmailFromToken(token);
            log.info("로그아웃된 유저: {}", email);

            Long result = redisTemplate.execute(new SessionCallback<Long>() {
                @Override
                public Long execute(RedisOperations operations) throws DataAccessException {
                    operations.multi();
                    operations.delete(email);
                    List<Object> results = operations.exec();
                    return (Long) results.get(0);
                }
            });

            if (result != null && result > 0) {
                log.info("사용자 {}가 로그아웃 됨" + email);
                return true;
            } else {
                log.warn(
                    "사용자{} 로그아웃 실패. redis에 토큰이 존재하지 않습니다." + email);
                return false;
            }
        } catch (Exception e) {
            log.error("로그아웃중 에러 발생: " + e);
            return false;
        }
    }
}
