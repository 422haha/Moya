package com.e22e.moya.park.service;

import com.e22e.moya.common.s3Service.PresignedUrlService;
import com.e22e.moya.park.dto.ParkResponseDto;
import com.e22e.moya.park.repository.ParkDistanceProjection;
import com.e22e.moya.park.repository.ParkRepositoryPark;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PopularParkServiceImpl implements PopularParkService {

    private static final double MIN_RADIUS = 10000; // 초기 반경 10km
    private static final double MAX_RADIUS = 50000; // 최대 반경 50km
    private static final int MAX_RESULTS = 10;
    private static final String POPULARITY_KEY = "park_popularity";

    private final ParkRepositoryPark parkRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final PresignedUrlService presignedUrlService;

    public PopularParkServiceImpl(ParkRepositoryPark parkRepository,
        StringRedisTemplate stringRedisTemplate, PresignedUrlService presignedUrlService) {
        this.parkRepository = parkRepository;
        this.stringRedisTemplate = stringRedisTemplate;
        this.presignedUrlService = presignedUrlService;
    }

    /**
     * 공원 인기도 증가
     *
     * @param parkId 공원 id
     */
    @Override
    public void increaseParkPopularity(Long parkId) {
        try {
            // 공원의 인기도 점수 1 증가
            Double score = stringRedisTemplate.opsForZSet().incrementScore(POPULARITY_KEY,
                String.valueOf(parkId), 1.0);
            log.info("공원 {}에 대한 인기도 증가, 점수: {}", parkId, score);
        } catch (Exception e) {
            log.error("공원 {}에 대한 인기도 증가 불가", parkId, e);
        }
    }

    /**
     * 인접한 인기공원 찾기
     *
     * @param latitude  사용자 위치
     * @param longitude 사용자 위치
     * @return 인기 공원 결과
     */
    @Override
    @Transactional(readOnly = true)
    public List<ParkResponseDto> getPopularParksNearby(double latitude, double longitude) {
        Map<String, ParkDistanceProjection> nearbyParks = new HashMap<>();
        double radius = MIN_RADIUS;

        // 주변 공원 정보 수집
        while (nearbyParks.size() < MAX_RESULTS && radius <= MAX_RADIUS) {
            List<ParkDistanceProjection> parks = parkRepository.findNearParks(latitude, longitude,
                radius);
            log.info("공원 찾는중... {}개의 공원 발견, 반지름: {}", parks.size(), radius);

            for (ParkDistanceProjection park : parks) {
                nearbyParks.putIfAbsent(park.getId().toString(), park);
            }
            log.info("공원 찾는중... 총 공원 개수: {}", nearbyParks.size());
            if (nearbyParks.size() < MAX_RESULTS) {
                radius *= 2;
            } else {
                break;
            }
        }

        // ZSET에서 인기도 순으로 정렬된 근처 공원 가져오기
        Set<ZSetOperations.TypedTuple<String>> popularParks = stringRedisTemplate.opsForZSet()
            .reverseRangeByScoreWithScores(POPULARITY_KEY, 0, Double.MAX_VALUE, 0,
                nearbyParks.size());
        log.info(" {} 개의 공원 발견", popularParks.size());

        List<ParkResponseDto> result = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> parkTuple : popularParks) {
            String parkId = parkTuple.getValue();
            ParkDistanceProjection parkInfo = nearbyParks.get(parkId);

            if (parkInfo != null) {
                result.add(new ParkResponseDto(
                    parkInfo.getId(),
                    parkInfo.getName(),
                    parkInfo.getDescription(),
                    (int) Math.round(parkInfo.getDistance()),
                    presignedUrlService.getPresignedUrl(parkInfo.getImageUrl())
                ));
                log.info("추가된 공원: id={}, name={}, 거리={}, 점수={}",
                    parkInfo.getId(), parkInfo.getName(), parkInfo.getDistance(),
                    parkTuple.getScore());
                if (result.size() == MAX_RESULTS) {
                    break;
                }
            } else {
                log.warn("근처 공원에서 상세정보 찾을 수 없음: id={}, 점수={}", parkId,
                    parkTuple.getScore());
            }
        }
        log.info("{} 개의 인기 공원 반환", result.size());
        return result;
    }


    /**
     * 인기도 감소
     */
    @Override
    @Scheduled(cron = "0 0 2 * * ?")
    public void decreaseParkPopularity() {
        log.info("공원 인기도 감소");
        ZSetOperations<String, String> zSetOps = stringRedisTemplate.opsForZSet();
        Set<TypedTuple<String>> parks = zSetOps.rangeWithScores(POPULARITY_KEY, 0, -1);

        for (ZSetOperations.TypedTuple<String> park : parks) {
            String parkId = park.getValue();
            Double score = park.getScore();
            if (score != null) {
                // 인기도 점수를 10% 감소
                double newScore = Math.max(0, score * 0.9);
                zSetOps.add(POPULARITY_KEY, parkId, newScore);
            }
        }
        log.info("공원 인기도 점수 감소 완료");
    }

}
