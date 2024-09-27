package com.e22e.moya.user.controller;

import com.e22e.moya.common.util.JwtUtil;
import com.e22e.moya.user.dto.UserNameResponseDto;
import com.e22e.moya.user.service.UserService;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public UserController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    /**
     * 로그아웃
     *
     * @param token 사용자 인증 토큰
     * @return 로그아웃 처리 결과
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(
        @RequestHeader("Authorization") String token) {
        log.info("로그아웃 요청 : 사용자 {}", jwtUtil.getUserIdFromToken(token));
        Map<String, Object> response = new HashMap<>();
        try {
            boolean logoutSuccess = jwtUtil.logout(token);
            if (logoutSuccess) {
                log.info("로그아웃 성공");
                response.put("message", "로그아웃 성공");
                response.put("data", new Object[]{});
                return ResponseEntity.ok(response);
            } else {
                log.warn("로그아웃 실패");
                response.put("message", "로그아웃 실패");
                response.put("data", new Object[]{});
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            log.error("로그아웃중 에러 발생: " + e);
            response.put("message", "로그아웃 처리 중 오류 발생");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 사용자 이름 및 프로필 이미지 조회
     *
     * @return 사용자 이름과 프로필 이미지
     */
    @GetMapping("/name")
    public ResponseEntity<Map<String, Object>> getUserName(
        // @RequestHeader("Authorization") String token
    ) {
//        log.info("사용자 이름 조회 요청: {}", token);

        Map<String, Object> response = new HashMap<>();

        try {
//            Long userId = jwtUtil.getUserIdFromToken(token);
            long userId = 1;  // 주석 처리된 부분을 userId=1로 대체
            UserNameResponseDto userDto = userService.getUserName(userId);

            response.put("message", "조회 성공");
            response.put("data", userDto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("사용자 조회 실패: {}", e.getMessage());
            response.put("message", "사용자를 찾을 수 없습니다");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            log.error("사용자 이름 조회 실패: {}", e.getMessage());
            response.put("message", "권한이 없습니다");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
