package com.e22e.moya.exploration.controller;

import com.e22e.moya.common.util.JwtUtil;
import com.e22e.moya.common.util.JwtUtil.TokenStatus;
import com.e22e.moya.exploration.service.ExplorationService;
import io.jsonwebtoken.JwtException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exploration")
public class ExplorationController {

    private final JwtUtil jwtUtil;
    private final ExplorationService explorationService;

    public ExplorationController(JwtUtil jwtUtil, ExplorationService explorationService) {
        this.jwtUtil = jwtUtil;
        this.explorationService = explorationService;
    }

    //탐험 시작 컨트롤러
    @PostMapping("/start")
    public ResponseEntity<Map<String, String>> startExploration(
        @RequestHeader("Authorization") String token,
        @RequestHeader("Refresh-Token") String refreshToken) {
        try {
            // 리프레시 토큰 갱신
            String newRefreshToken = jwtUtil.refreshToken(refreshToken);

            Map<String, String> response = new HashMap<>();
            response.put("message", "탐험 시작");
            response.put("data", null);

            return ResponseEntity.ok()
                .header("Refresh-Token", newRefreshToken)
                .body(response);
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Collections.singletonMap("error", "Invalid refresh token"));
        }
    }

}
