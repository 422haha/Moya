package com.e22e.moya.user.service.jwt;

import com.e22e.moya.common.constants.JWTConstants;
import com.e22e.moya.common.util.JwtUtil;
import com.nimbusds.jose.shaded.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginSuccessServiceImpl implements LoginSuccessService {

    private final JwtUtil jwtUtil;

    public LoginSuccessServiceImpl(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Map<String, String> generateTokens(String email) {
        log.info("JWT 토큰 생성 완료: {}", email);
        jwtUtil.invalidateToken(email);

        String accessToken = jwtUtil.generateAccessToken(email);
        String refreshToken = jwtUtil.generateRefreshToken(email);

        Map<String, String> tokens = new HashMap<>();
        tokens.put(JWTConstants.JWT_HEADER, accessToken);
        tokens.put(JWTConstants.REFRESH_TOKEN_HEADER, refreshToken);

        return tokens;
    }

    @Override
    public String getSuccessResponseBody() {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "로그인 성공");
        responseBody.put("data", "true");
        return new Gson().toJson(responseBody);
    }
}
