package com.e22e.moya.diary.controller;

import com.e22e.moya.common.util.JwtUtil;
import com.e22e.moya.diary.dto.DiaryHomeResponseDto;
import com.e22e.moya.diary.dto.DiaryListResponseDto;
import com.e22e.moya.diary.dto.DiaryDetailResponseDto;
import com.e22e.moya.diary.service.DiaryService;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;
    private final JwtUtil jwtUtil;

    /**
     * 홈 화면에서 가장 최근 탐험 정보를 반환
     *
     * @return 가장 최근 탐험 정보
     */
    @GetMapping("/home")
    public ResponseEntity<Map<String, Object>> getLatestExploration(
        // @RequestHeader("Authorization") String token
    ) {
        Map<String, Object> response = new HashMap<>();

        try {
//            Long userId = jwtUtil.getUserIdFromToken(token);
            long userId = 1;  // 주석 처리된 부분을 userId=1로 대체

            DiaryHomeResponseDto homeResponse = diaryService.getLatestExploration(userId);

            response.put("message", "조회 성공");
            response.put("data", homeResponse);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            response.put("message", "최근 탐험이 없습니다");
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            response.put("message", "서버 내부 오류가 발생했습니다");
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 탐험 일지 목록을 페이지네이션하여 반환
     *
     * @param page 요청 페이지 번호
     * @param size 페이지 크기
     * @return 탐험 일지 목록
     */
    @GetMapping("/diarys")
    public ResponseEntity<Map<String, Object>> getExplorations(
//        @RequestHeader("Authorization") String token,
        @RequestParam("page") int page,
        @RequestParam("size") int size) {

        Map<String, Object> response = new HashMap<>();

        try {
//            Long userId = jwtUtil.getUserIdFromToken(token);
            long userId = 1;  // 주석 처리된 부분을 userId=1로 대체

            DiaryListResponseDto diaryList = diaryService.getExplorations(userId, page, size);

            response.put("message", "모험 일지 조회 성공");
            response.put("data", diaryList);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "서버 내부 오류가 발생했습니다");
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 특정 탐험의 상세 정보를 반환
     *
     * @param explorationId 조회하려는 탐험의 ID
     * @return 탐험의 상세 정보
     */
    @GetMapping("/diarys/{explorationId}")
    public ResponseEntity<Map<String, Object>> getExplorationDetail(
//        @RequestHeader("Authorization") String token,
        @PathVariable("explorationId") Long explorationId) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Long userId = jwtUtil.getUserIdFromToken(token);
            long userId = 1;  // 주석 처리된 부분을 userId=1로 대체

            DiaryDetailResponseDto diaryDetail = diaryService.getExplorationDetail(userId,
                explorationId);

            response.put("message", "모험 일지 상세 조회 성공");
            response.put("data", diaryDetail);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            response.put("message", "탐험을 찾을 수 없습니다");
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            response.put("message", "서버 내부 오류가 발생했습니다");
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
