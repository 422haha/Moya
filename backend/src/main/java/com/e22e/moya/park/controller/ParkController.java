package com.e22e.moya.park.controller;

import com.e22e.moya.park.dto.ParkDetailResponseDto;
import com.e22e.moya.park.dto.ParkListResponseDto;
import com.e22e.moya.park.dto.ParkResponseDto;
import com.e22e.moya.park.service.ParkService;
import com.e22e.moya.common.util.JwtUtil;
import com.e22e.moya.park.service.PopularParkService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/park")
public class ParkController {

    private final PopularParkService popularParkService;
    private final ParkService parkService;
    private final JwtUtil jwtUtil;

    public ParkController(PopularParkService popularParkService, ParkService parkService,
        JwtUtil jwtUtil) {
        this.popularParkService = popularParkService;
        this.parkService = parkService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 홈 화면에서 가장 가까운 공원 정보를 반환
     *
     * @param latitude  위도
     * @param longitude 경도
     * @return 가장 가까운 공원 정보
     */
    @GetMapping("/home")
    public ResponseEntity<Map<String, Object>> getNearestPark(
//        @RequestHeader("Authorization") String token,
        @RequestParam("latitude") double latitude,
        @RequestParam("longitude") double longitude) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Long userId = jwtUtil.getUserIdFromToken(token);
            long userId = 1;  // 주석 처리된 부분을 userId=1로 대체

            // 제일 가까운 공원 조회
            ParkResponseDto parkResponse = parkService.getNearestPark(userId, latitude, longitude);

            response.put("message", "조회 성공");
            response.put("data", parkResponse);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "서버 내부 오류가 발생했습니다: " + e.getMessage());
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 공원 목록을 페이지네이션하여 반환
     *
     * @param page      요청 페이지 번호
     * @param size      페이지 크기
     * @param latitude  위도
     * @param longitude 경도
     * @return 공원 목록과 상태 코드
     */
    @GetMapping("/parks")
    public ResponseEntity<Map<String, Object>> getParks(
//        @RequestHeader("Authorization") String token,
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam("latitude") double latitude,
        @RequestParam("longitude") double longitude) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Long userId = jwtUtil.getUserIdFromToken(token);
            long userId = 1;  // 주석 처리된 부분을 userId=1로 대체

            // 공원 목록 조회
            ParkListResponseDto parks = parkService.getParks(userId, latitude, longitude, page,
                size);

            response.put("message", "공원 목록 조회 성공");
            response.put("data", parks);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "서버 내부 오류가 발생했습니다: " + e.getMessage());
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 특정 공원의 상세 정보를 반환
     *
     * @param parkId 조회하려는 공원의 ID
     * @return 공원의 상세 정보와 상태 코드
     */
    @GetMapping("/parks/{parkId}")
    public ResponseEntity<Map<String, Object>> getParkDetail(
//        @RequestHeader("Authorization") String token,
        @PathVariable("parkId") Long parkId) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Long userId = jwtUtil.getUserIdFromToken(token);
            long userId = 1;  // 주석 처리된 부분을 userId=1로 대체

            // 공원 상세 정보 조회
            ParkDetailResponseDto parkDetail = parkService.getParkDetail(parkId);

            response.put("message", "공원 상세 정보 조회 성공");
            response.put("data", parkDetail);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "서버 내부 오류가 발생했습니다: " + e.getMessage());
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 내 주변 인기 공원
     *
     * @param latitude  위도
     * @param longitude 경도
     * @return 가장 가까운 공원 정보
     */
    @GetMapping("/home/famous")
    public ResponseEntity<Map<String, Object>> getPopularPark(
        @RequestParam("latitude") double latitude,
        @RequestParam("longitude") double longitude) {

        Map<String, Object> response = new HashMap<>();

        try {

            // 내 주변 인기 공원 조회
            List<ParkResponseDto> parkResponse = popularParkService.getPopularParksNearby(latitude,
                longitude);

            response.put("message", "조회 성공");
            response.put("data", parkResponse);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "서버 내부 오류가 발생했습니다: " + e.getMessage());
            response.put("data", new Object[]{});
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
