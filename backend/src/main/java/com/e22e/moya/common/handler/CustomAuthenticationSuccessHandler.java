package com.e22e.moya.common.handler;

import com.e22e.moya.common.constants.JWTConstants;
import com.e22e.moya.common.util.JwtUtil;
import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 인증 성공 시 처리
 * 액세스 토큰과 리프레시 토큰을 생성
 */
@Slf4j
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    public CustomAuthenticationSuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        String email = authentication.getName();
        log.info("사용자 인증 성공: {}", email);
        jwtUtil.invalidateToken(email);

        log.info("JWT 토큰 생성 완료: {}", email);
        String accessToken = jwtUtil.generateAccessToken(email);
        String refreshToken = jwtUtil.generateRefreshToken(email);
        response.setHeader(JWTConstants.JWT_HEADER, accessToken);
        response.setHeader(JWTConstants.REFRESH_TOKEN_HEADER, refreshToken);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(getResponseBody());
    }

    private String getResponseBody() {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "로그인 성공");
        responseBody.put("data", "true");
        return new Gson().toJson(responseBody);
    }
}