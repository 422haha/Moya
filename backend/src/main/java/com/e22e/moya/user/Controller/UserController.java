package com.e22e.moya.user.Controller;

import com.e22e.moya.common.util.JwtUtil;
import com.e22e.moya.user.service.UserService;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

}
