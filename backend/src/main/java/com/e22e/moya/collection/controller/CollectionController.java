package com.e22e.moya.collection.controller;

import com.e22e.moya.collection.dto.CollectionDetailDto;
import com.e22e.moya.collection.dto.CollectionResponseDto;
import com.e22e.moya.collection.service.CollectionService;
import com.e22e.moya.common.util.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/collection")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;
    private final JwtUtil jwtUtil;

    /**
     * 전체 도감 목록 조회
     * @param token 사용자 인증 토큰
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @param filter 완료된/미발견/전체 필터
     * @return 도감 목록
     */
    @GetMapping("/collections")
    public ResponseEntity<Map<String, Object>> getAllCollections(
        @RequestHeader("Authorization") String token,
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam(value = "filter", defaultValue = "all") String filter
    ) {
        Map<String, Object> response = new HashMap<>();

        try {
            Long userId = jwtUtil.getUserIdFromToken(token);

            CollectionResponseDto responseDto = collectionService.getAllCollections(userId, page, size, filter);

            response.put("message", "도감 목록 조회 성공");
            response.put("data", responseDto);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            response.put("message", "권한이 없습니다");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);

        } catch (Exception e) {
            log.error("서버 내부 오류: ", e);
            response.put("message", "서버 내부 오류가 발생했습니다");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 특정 공원의 도감 목록 조회
     * @param token 사용자 인증 토큰
     * @param parkId 공원 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @param filter 완료된/미발견/전체 필터
     * @return 공원의 도감 목록
     */
    @GetMapping("/{parkId}")
    public ResponseEntity<Map<String, Object>> getParkCollections(
        @RequestHeader("Authorization") String token,
        @PathVariable("parkId") Long parkId,
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam(value = "filter", defaultValue = "all") String filter
    ) {
        Map<String, Object> response = new HashMap<>();

        try {
            Long userId = jwtUtil.getUserIdFromToken(token);

            CollectionResponseDto responseDto = collectionService.getParkCollections(userId, parkId, page, size, filter);

            response.put("message", "도감 목록 조회 성공");
            response.put("data", responseDto);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            response.put("message", "권한이 없습니다");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);

        } catch (Exception e) {
            log.error("서버 내부 오류: ", e);
            response.put("message", "서버 내부 오류가 발생했습니다");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 도감 상세 정보 조회
     * @param token 사용자 인증 토큰
     * @param itemId 도감 ID
     * @return 도감 상세 정보
     */
    @GetMapping("/{itemId}/detail")
    public ResponseEntity<Map<String, Object>> getCollectionDetail(
        @RequestHeader("Authorization") String token,
        @PathVariable("itemId") Long itemId
    ) {
        Map<String, Object> response = new HashMap<>();

        try {
            Long userId = jwtUtil.getUserIdFromToken(token);

            CollectionDetailDto responseDto = collectionService.getCollectionDetail(userId, itemId);

            response.put("message", "도감 상세 정보 조회 성공");
            response.put("data", responseDto);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            response.put("message", "권한이 없습니다");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);

        } catch (Exception e) {
            log.error("서버 내부 오류: ", e);
            response.put("message", "서버 내부 오류가 발생했습니다");
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
