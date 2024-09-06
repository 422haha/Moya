package com.e22e.moya.common.filter;

import com.e22e.moya.common.constants.JWTConstants;
import com.e22e.moya.common.entity.Users;
import com.e22e.moya.common.util.JwtUtil;
import com.e22e.moya.user.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JWTTokenValidatorFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> data = new HashMap<>();

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JWTTokenValidatorFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(JWTConstants.JWT_HEADER);
        String refreshToken = request.getHeader(JWTConstants.REFRESH_TOKEN_HEADER);

        if (null != jwt && jwt.startsWith("Bearer ")) {
            jwt = jwtUtil.extractToken(jwt);
            try {
                JwtUtil.TokenStatus tokenStatus = jwtUtil.isTokenValid(jwt);
                log.info("토큰 검증 결과: {}", tokenStatus);
                switch (tokenStatus) {
                    case VALID:
                        String email = jwtUtil.getEmailFromToken(jwt);
                        Authentication auth = new UsernamePasswordAuthenticationToken(email, null);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        log.info("유효한 토큰으로 인증 성공: {}", email);
                        filterChain.doFilter(request, response);
                        break;
                    case INVALID:
                        log.warn("유효하지 않은 토큰 감지");
                        sendUnauthorizedResponse(response, "유효하지 않은 토큰입니다.");
                        break;
                    case NOT_FOUND:
                        log.warn("세션을 찾을 수 없음");
                        sendUnauthorizedResponse(response, "세션을 찾을 수 없습니다. 다시 로그인해주세요.");
                        break;
                    case EXPIRED:
                        log.info("만료된 토큰 감지, 리프레시 토큰 처리 시도");
                        if (null == refreshToken) {
                            log.info("리프레시 토큰 요청");
                            sendUnauthorizedResponse(response, "Refresh-Token-Request");
                        } else {
                            log.info("리프레시 토큰 존재. 새로운 토큰 발급");
                            handleRefreshToken(response, refreshToken);
                        }
                        break;
                }

            } catch (Exception e) {
                log.error("액세스 토큰 처리 불가 {}", e.getMessage());
                sendUnauthorizedResponse(response, "액세스 토큰 처리 불가");
            }
        } else {
            log.debug("토큰 없음, 필터 체인 계속 진행");
            filterChain.doFilter(request, response);
        }
    }

    private void handleRefreshToken(HttpServletResponse response, String refreshToken)
        throws IOException {
        if (refreshToken != null) {
            try {
                Claims refreshClaims = jwtUtil.validateToken(refreshToken);

                String email = (String) refreshClaims.get("email");

                Optional<Users> Users = userRepository.findByEmail(email);

                if (Users.isPresent()) {

                    String newAccessToken = jwtUtil.generateAccessToken(email);

                    log.info("리프레시 토큰으로 새 액세스 토큰 생성 성공: {}", email);
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    response.setHeader(JWTConstants.JWT_HEADER, newAccessToken);
                    response.getWriter().flush();
                } else {
                    sendUnauthorizedResponse(response, "사용자를 찾을 수 없습니다.");
                }

            } catch (ExpiredJwtException e) {
                log.warn("만료된 리프레시 토큰 감지: {}", e.getMessage());
                sendRefreshTokenExpired(response, "리프레시 토큰 만료. 재로그인 필요");
            } catch (JwtException e) {
                log.error("리프레시 토큰 처리 중 예외 발생: {}", e.getMessage());
                sendUnauthorizedResponse(response, "리프레시 토큰 처리 실패");
            }
        } else {
            log.warn("리프레시 토큰 없음");
            sendUnauthorizedResponse(response, "리프레시 토큰 없음");
        }
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message)
        throws IOException {
        if (message.equals("Refresh-Token-Request")) {
            response.setHeader("Refresh-Token-Request", "true");
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        data.put("message", message);
        data.put("data", new Object[]{});
        response.getWriter().write(objectMapper.writeValueAsString(data));
        response.getWriter().flush();
    }

    private void sendRefreshTokenExpired(HttpServletResponse response, String message)
        throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        data.put("message", message);
        data.put("data", new Object[]{});
        response.getWriter().write(objectMapper.writeValueAsString(data));
        response.getWriter().flush();
    }
}
