package com.e22e.moya.season.controller;

import com.e22e.moya.season.dto.PopularSpeciesDto;
import com.e22e.moya.park.dto.ParkDistanceDto;
import com.e22e.moya.season.service.PopularSpeciesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/season/famous")
public class SeasonFamousController {

    private final PopularSpeciesService popularSpeciesService;

    public SeasonFamousController(PopularSpeciesService popularSpeciesService) {
        this.popularSpeciesService = popularSpeciesService;
    }

    /**
     * 현재 계절의 인기 동식물 목록을 반환
     *
     * @return 인기 동식물 목록
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getPopularSpecies() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<PopularSpeciesDto> popularSpecies = popularSpeciesService.getPopularSpecies(10);
            response.put("message", "인기 동식물 조회 성공");
            response.put("data", popularSpecies);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "서버 내부 오류가 발생했습니다");
            response.put("data", new Object[]{});
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 특정 동식물이 있는 공원 목록을 반환
     *
     * @param speciesId 조회할 동식물 ID
     * @param latitude  사용자 위치의 위도
     * @param longitude 사용자 위치의 경도
     * @return 해당 동식물이 존재하는 공원 목록
     */
    @GetMapping("/{speciesId}")
    public ResponseEntity<Map<String, Object>> getParksBySpecies(
        @PathVariable Long speciesId,
        @RequestParam double latitude,
        @RequestParam double longitude) {

        Map<String, Object> response = new HashMap<>();

        try {
            List<ParkDistanceDto> parks = popularSpeciesService.getParksBySpecies(speciesId,
                latitude, longitude);
            response.put("message", "공원 조회 성공");
            response.put("data", parks);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "서버 내부 오류가 발생했습니다");
            response.put("data", new Object[]{});
            return ResponseEntity.status(500).body(response);
        }
    }
}
