package com.e22e.moya.exploration.controller;

import com.e22e.moya.common.util.JwtUtil;
import com.e22e.moya.exploration.dto.exploration.AddRequestDto;
import com.e22e.moya.exploration.dto.exploration.AddResponseDto;
import com.e22e.moya.exploration.dto.exploration.EndRequestDto;
import com.e22e.moya.exploration.dto.exploration.EndResponseDto;
import com.e22e.moya.exploration.dto.info.ExplorationStartDto;
import com.e22e.moya.exploration.dto.quest.QuestListResponseDto;
import com.e22e.moya.exploration.service.exploration.ExplorationService;
import com.e22e.moya.exploration.service.info.InfoService;
import com.e22e.moya.exploration.service.quest.QuestService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/exploration")
public class ExplorationController {

    private final JwtUtil jwtUtil;
    private final InfoService infoService;
    private final ExplorationService explorationService;
    private final QuestService questService;

    public ExplorationController(JwtUtil jwtUtil, InfoService explorationService,
        ExplorationService explorationService1, QuestService questService) {
        this.jwtUtil = jwtUtil;
        this.infoService = explorationService;
        this.explorationService = explorationService1;
        this.questService = questService;
    }

    //탐험 시작 컨트롤러
    @GetMapping("/start/{parkId}")
    public ResponseEntity<Map<String, Object>> startExploration(
//        @RequestHeader("Authorization") String token,
//        @RequestHeader("Refresh-Token") String refreshToken,
        @PathVariable("parkId") Long parkId) {

        Map<String, Object> response = new HashMap<>();
        HttpHeaders headers = new HttpHeaders(); // HttpHeader

        try {
//            long userId = jwtUtil.getUserIdFromToken(token);
//
//            // 리프레시 토큰 갱신
//            String newRefreshToken = jwtUtil.refreshToken(refreshToken);
//            headers.add(JWTConstants.REFRESH_TOKEN_HEADER, newRefreshToken); // 새 리프레시 토큰을 헤더에 추가

            long userId = 1;

            // 탐험 시작시 필요한 정보 조회
            ExplorationStartDto explorationStartDto = infoService.getInitInfo(parkId,
                userId);

            log.info("탐험에 필요한 정보 불러오기 성공 : {}", parkId);

            response.put("message", "탐험 시작");
            response.put("data", explorationStartDto);
            return ResponseEntity.status(HttpStatus.OK).body(response);

//            response.put("message", "탐험 시작");
//            response.put("data", explorationStartDto);
//            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(response);

        } catch (JwtException e) {
            log.error("리프레시 토큰 재발급 불가 : {}", e.getMessage());
            response.put("message", "리프레시 토큰 재발급 불가");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (Exception e) {

            log.error("탐험에 필요한 정보 불러오기 실패 : {}", e);
            response.put("message", "탐험에 필요한 정보 불러올 수 없음");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    //탐험 중 촬영한 사진 도감에 등록 컨트롤러
    @PostMapping("/{explorationId}/camera")
    public ResponseEntity<Map<String, Object>> addDictionary(
        @RequestHeader("Authorization") String token,
        @RequestParam Long explorationId,
        @RequestBody AddRequestDto addRequestDto) {
        Map<String, Object> response = new HashMap<>();

        try {
            long userId = jwtUtil.getUserIdFromToken(token);
            AddResponseDto addResponseDto = explorationService.addOnDictionary(userId,
                explorationId, addRequestDto);

            response.put("message", "등록 완료");
            response.put("data", addResponseDto);
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {

            log.error("도감에 저장 실패 : {}", e.getMessage());
            response.put("message", "탐험에 필요한 정보 불러올 수 없음");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        }

    }

    //탐험 종료 및 기록 저장 컨트롤러
    @PostMapping("/{explorationId}/end")
    public ResponseEntity<Map<String, Object>> endExploration(
        @RequestHeader("Authorization") String token,
        @RequestParam Long explorationId,
        @RequestBody EndRequestDto endRequestDto) {
        Map<String, Object> response = new HashMap<>();

        try {
            long userId = jwtUtil.getUserIdFromToken(token);
            EndResponseDto endResponseDto = explorationService.endExploration(userId, explorationId,
                endRequestDto);

            response.put("message", "탐험 기록 저장 완료");
            response.put("data", endResponseDto);
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {

            log.error("탐험 기록 저장 실패 : {}", e.getMessage());
            response.put("message", "탐험 기록 저장 실패");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        }

    }

    // 도전과제 목록 조회
    @GetMapping("/{explorationId}/quest/list")
    public ResponseEntity<Map<String, Object>> questList(
//        @RequestHeader("Authorization") String token,
        @RequestParam Long explorationId) {
        Map<String, Object> response = new HashMap<>();

        try {
            long userId = 1; //jwtUtil.getUserIdFromToken(token);
            QuestListResponseDto questListResponseDto = questService.getQuestList(userId, explorationId);

            response.put("message", "도전과제 목록 조회 완료");
            response.put("data", questListResponseDto);
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {

            log.error("도전과제 목록 조회 실패 : {}", e.getMessage());
            response.put("message", "도전과제 목록 조회 실패");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        }

    }
}
