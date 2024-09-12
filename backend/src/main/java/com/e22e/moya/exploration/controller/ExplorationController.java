package com.e22e.moya.exploration.controller;

import com.e22e.moya.common.constants.JWTConstants;
import com.e22e.moya.common.util.JwtUtil;
import com.e22e.moya.exploration.dto.ExplorationStartDto;
import com.e22e.moya.exploration.service.ExplorationService;
import io.jsonwebtoken.JwtException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

@Slf4j
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
    @GetMapping("/start/{parkId}")
    public ResponseEntity<Map<String, Object>> startExploration(
        @RequestHeader("Authorization") String token,
        @RequestHeader("Refresh-Token") String refreshToken,
        @PathVariable("parkId") Long parkId) {

        Map<String, Object> response = new HashMap<>();
        HttpHeaders headers = new HttpHeaders(); // HttpHeader

        try {
            long userId = jwtUtil.getUserIdFromToken(token);

            // 리프레시 토큰 갱신
            String newRefreshToken = jwtUtil.refreshToken(refreshToken);
            headers.add(JWTConstants.REFRESH_TOKEN_HEADER, newRefreshToken); // 새 리프레시 토큰을 헤더에 추가

            // 탐험 시작시 필요한 정보 조회
            ExplorationStartDto explorationStartDto = explorationService.getInitInfo(parkId, userId);

            log.info("탐험에 필요한 정보 불러오기 성공 : {}", parkId);

            response.put("message", "탐험 시작");
            response.put("data", explorationStartDto);
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(response);

        } catch (JwtException e) {
            log.error("리프레시 토큰 재발급 불가 : {}", e.getMessage());
            response.put("message", "리프레시 토큰 재발급 불가");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (Exception e) {
            log.error("탐험에 필요한 정보 불러오기 실패 : {}", e.getMessage());
            response.put("message", "탐험에 필요한 정보 불러올 수 없음");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    //탐험 중 촬영한 사진 도감에 등록 컨트롤러
    @PostMapping("/exploration/{explorationId}/camera")
    public ResponseEntity<Map<String, String>> addDictionary(
        @RequestHeader("Authorization") String token) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "등록 완료");
        response.put("data", null);
        return ResponseEntity.ok().body(response);
    }

}
