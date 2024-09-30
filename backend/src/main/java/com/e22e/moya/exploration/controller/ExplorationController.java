package com.e22e.moya.exploration.controller;

import com.e22e.moya.common.util.JwtUtil;
import com.e22e.moya.exploration.dto.exploration.AddRequestDto;
import com.e22e.moya.exploration.dto.exploration.AddResponseDto;
import com.e22e.moya.exploration.dto.exploration.EndRequestDto;
import com.e22e.moya.exploration.dto.exploration.EndResponseDto;
import com.e22e.moya.exploration.dto.info.ExplorationInfoDto;
import com.e22e.moya.exploration.dto.quest.complete.QuestCompleteResponseDto;
import com.e22e.moya.exploration.dto.quest.list.QuestListResponseDto;
import com.e22e.moya.exploration.service.exploration.ExplorationService;
import com.e22e.moya.exploration.service.info.InfoService;
import com.e22e.moya.exploration.service.quest.QuestService;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.web.bind.annotation.RequestMapping;
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
        @PathVariable Long parkId) {

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
            ExplorationInfoDto explorationStartDto = infoService.getInitInfo(parkId,
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

        } catch (EntityNotFoundException e) {
            log.error("공원을 찾을 수 없습니다. : {}", e.getMessage());
            response.put("message", "공원을 찾을 수 없습니다");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            log.error("탐험에 필요한 정보 불러오기 실패 : {}", e.getMessage());
            response.put("message", "탐험에 필요한 정보 불러올 수 없습니다.");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 탐험중 정보 로드
    @GetMapping("{parkId}/load/{explorationId}")
    public ResponseEntity<Map<String, Object>> loadExploration(
        //        @RequestHeader("Authorization") String token,
        @PathVariable Long parkId,
        @PathVariable Long explorationId) {
        Map<String, Object> response = new HashMap<>();
        try {

            long userId = 1;

            // 탐험 시작시 필요한 정보 조회
            ExplorationInfoDto explorationInfoDto = infoService.getInfo(parkId, explorationId,
                userId);

            log.info("탐험에 필요한 정보 불러오기 성공 : {}", explorationId);

            response.put("message", "탐험 정보 로드");
            response.put("data", explorationInfoDto);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (EntityNotFoundException e) {
            log.error("공원을 찾을 수 없습니다. : {}", e.getMessage());
            response.put("message", "공원을 찾을 수 없습니다");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            log.error("탐험에 필요한 정보 불러오기 실패 : {}", e.getMessage());
            response.put("message", "탐험에 필요한 정보 불러올 수 없습니다.");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //탐험 중 촬영한 사진 도감에 등록 컨트롤러
    @PostMapping("/{explorationId}/camera")
    public ResponseEntity<Map<String, Object>> addDictionary(
//        @RequestHeader("Authorization") String token,
        @PathVariable Long explorationId,
        @RequestBody AddRequestDto addRequestDto) {
        Map<String, Object> response = new HashMap<>();

        try {
//            long userId = jwtUtil.getUserIdFromToken(token);
            long userId = 1;
            AddResponseDto addResponseDto = explorationService.addOnDictionary(userId,
                explorationId, addRequestDto);

            response.put("message", "등록 완료");
            response.put("data", addResponseDto);
            return ResponseEntity.ok().body(response);

        } catch (EntityNotFoundException e) {
            log.error("리소스를 찾을 수 없음 : {}", e.getMessage());
            response.put("message", e.getMessage());
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            log.error("도감에 저장 실패 : {}", e.getMessage());
            response.put("message", "도감에 저장할 수 없습니다.");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    //탐험 종료 및 기록 저장 컨트롤러
    @PostMapping("/{explorationId}/end")
    public ResponseEntity<Map<String, Object>> endExploration(
//        @RequestHeader("Authorization") String token,
        @PathVariable Long explorationId,
        @RequestBody EndRequestDto endRequestDto) {
        Map<String, Object> response = new HashMap<>();

        try {
//            long userId = jwtUtil.getUserIdFromToken(token);
            long userId = 1;
            EndResponseDto endResponseDto = explorationService.endExploration(userId, explorationId,
                endRequestDto);

            response.put("message", "탐험 기록 저장 완료");
            response.put("data", endResponseDto);
            return ResponseEntity.ok().body(response);

        } catch (EntityNotFoundException e) {
            log.error("탐험을 찾을 수 없음 : {}", e.getMessage());
            response.put("message", "탐험을 찾을 수 없습니다.");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalArgumentException e) {
            log.error("권한 없음 : {}", e.getMessage());
            response.put("message", "탐험 종료 권한이 없습니다.");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            log.error("탐험 기록 저장 실패 : {}", e.getMessage());
            response.put("message", "탐험 기록 저장에 실패했습니다.");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    // 도전과제 목록 조회
    @GetMapping("/{explorationId}/quest/list")
    public ResponseEntity<Map<String, Object>> questList(
//        @RequestHeader("Authorization") String token,
        @PathVariable Long explorationId) {
        Map<String, Object> response = new HashMap<>();

        try {
            long userId = 1; //jwtUtil.getUserIdFromToken(token);
            QuestListResponseDto questListResponseDto = questService.getQuestList(userId,
                explorationId);

            response.put("message", "도전과제 목록 조회 완료");
            response.put("data", questListResponseDto);
            return ResponseEntity.ok().body(response);

        } catch (EntityNotFoundException e) {
            log.error("도전과제 목록을 찾을 수 없음 :", e);
            response.put("message", e.getMessage());
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            log.error("도전과제 목록 조회 실패", e);
            response.put("message", "도전과제 목록 조회에 실패했습니다.");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 도전과제 상태 변경
    @GetMapping("/quest/{questId}/start")
    public ResponseEntity<Map<String, Object>> changeStatus(
        @PathVariable Long questId) {
        Map<String, Object> response = new HashMap<>();

        try {
            questService.changeStatus(questId);

            response.put("message", "도전과제 상태 변경 완료");
            response.put("data", new Object[]{});
            return ResponseEntity.ok().body(response);
        } catch (EntityNotFoundException e) {
            log.error("도전과제 찾을 수 없음 :", e);
            response.put("message", e.getMessage());
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            log.error("도전과제 조회 실패", e);
            response.put("message", "도전과제 목록 조회에 실패했습니다.");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    // 도전과제 성공 처리
    @PostMapping("/{explorationId}/quest/{questId}/complete")
    public ResponseEntity<Map<String, Object>> completeQuest(
        @PathVariable Long explorationId,
        @PathVariable Long questId
    ) {
        Map<String, Object> response = new HashMap<>();

        try {
            QuestCompleteResponseDto questCompleteResponseDto = questService.completeQuest(
                explorationId, questId);

            response.put("message", "도전과제 완료");
            response.put("data", questCompleteResponseDto);
            return ResponseEntity.ok().body(response);

        } catch (EntityNotFoundException e) {
            log.error("도전과제를 찾을 수 없음", e);
            response.put("message", "퀘스트를 찾을 수 없습니다.");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalStateException e) {
            log.error("도전과제 이미 완료됨", e);
            response.put("message", "도전과제가 이미 완료되었습니다.");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            log.error("도전과제 완료 처리 실패", e);
            response.put("message", "도전과제 완료 처리에 실패했습니다.");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
