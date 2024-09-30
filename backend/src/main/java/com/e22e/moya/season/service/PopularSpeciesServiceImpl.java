package com.e22e.moya.season.service;

import com.e22e.moya.common.entity.species.Season;
import com.e22e.moya.common.entity.species.Species;
import com.e22e.moya.season.dto.PopularSpeciesDto;
import com.e22e.moya.park.dto.ParkDistanceDto;
import com.e22e.moya.park.repository.ParkDistanceProjection;
import com.e22e.moya.park.repository.ParkRepositoryPark;
import com.e22e.moya.season.repository.SpeciesSeasonRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PopularSpeciesServiceImpl implements PopularSpeciesService {

    private final StringRedisTemplate stringRedisTemplate;
    private final SpeciesSeasonRepository speciesRepository;
    private final ParkRepositoryPark parkRepository;

    public PopularSpeciesServiceImpl(StringRedisTemplate stringRedisTemplate,
        SpeciesSeasonRepository speciesRepository,
        ParkRepositoryPark parkRepository) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.speciesRepository = speciesRepository;
        this.parkRepository = parkRepository;
    }

    /**
     * 현재 계절의 인기 동식물을 조회
     *
     * @param limit 반환할 인기 동식물의 수
     * @return 인기 동식물 목록
     */
    @Override
    public List<PopularSpeciesDto> getPopularSpecies(int limit) {
        Season currentSeason = getCurrentSeason();
        String key = "popular_species:season:" + currentSeason.name().toLowerCase();

        // Redis에서 캐시된 데이터를 먼저 조회
        Set<ZSetOperations.TypedTuple<String>> popularSpeciesSet = stringRedisTemplate.opsForZSet()
            .reverseRangeWithScores(key, 0, limit - 1);

        // 캐시된 데이터가 없을 때, DB에서 데이터를 조회 후 캐싱
        if (popularSpeciesSet == null || popularSpeciesSet.isEmpty()) {
            return getAndCachePopularSpecies(currentSeason, limit, key);
        } else {
            return getPopularSpeciesFromCache(popularSpeciesSet);
        }
    }

    /**
     * 캐시에 없을 경우 DB에서 데이터를 조회하고 Redis에 캐싱
     *
     * @param currentSeason 현재 계절
     * @param limit         반환할 데이터 수
     * @param key           Redis 키
     * @return 캐싱된 인기 동식물 목록
     */
    private List<PopularSpeciesDto> getAndCachePopularSpecies(Season currentSeason, int limit,
        String key) {
        List<Species> speciesList = speciesRepository.findPopularSpeciesBySeason(
            currentSeason); // Season 타입 전달

        // Redis에 캐싱
        speciesList.forEach(
            species -> stringRedisTemplate.opsForZSet().add(key, species.getId().toString(), 1.0));

        return speciesList.stream()
            .limit(limit)
            .map(species -> {
                PopularSpeciesDto dto = new PopularSpeciesDto();
                dto.setSpeciesId(species.getId());
                dto.setName(species.getName());
                dto.setImageUrl(species.getImageUrl());
                dto.setScore(1.0);  // 초기 점수
                return dto;
            }).collect(Collectors.toList());
    }

    /**
     * 캐시에 저장된 데이터를 가져옴
     *
     * @param popularSpeciesSet Redis에서 가져온 ZSet 데이터
     * @return 캐시된 인기 동식물 목록
     */
    private List<PopularSpeciesDto> getPopularSpeciesFromCache(
        Set<ZSetOperations.TypedTuple<String>> popularSpeciesSet) {
        List<Long> speciesIds = popularSpeciesSet.stream()
            .filter(tuple -> tuple.getValue() != null)
            .map(tuple -> Long.parseLong(tuple.getValue()))
            .collect(Collectors.toList());

        // speciesIds로 동식물 조회
        List<Species> speciesList = speciesRepository.findAllById(speciesIds);

        return speciesList.stream().map(species -> {
            Double score = popularSpeciesSet.stream()
                .filter(tuple -> tuple.getValue() != null && tuple.getValue()
                    .equals(species.getId().toString()))
                .map(ZSetOperations.TypedTuple::getScore)
                .findFirst().orElse(0.0);

            PopularSpeciesDto dto = new PopularSpeciesDto();
            dto.setSpeciesId(species.getId());
            dto.setName(species.getName());
            dto.setImageUrl(species.getImageUrl());
            dto.setScore(score);
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 특정 동식물이 존재하는 공원 목록을 조회
     *
     * @param speciesId     동식물 ID
     * @param userLatitude  사용자 위치의 위도
     * @param userLongitude 사용자 위치의 경도
     * @return 해당 동식물이 존재하는 공원 목록
     */
    @Override
    public List<ParkDistanceDto> getParksBySpecies(Long speciesId, double userLatitude,
        double userLongitude) {
        // 특정 동식물이 있는 공원 목록 조회
        List<Long> parkIds = speciesRepository.findParkIdsBySpeciesId(speciesId);

        // 공원과 사용자 위치 사이의 거리를 계산한 후, 공원 목록을 가져옴
        List<ParkDistanceProjection> parks = parkRepository.findParksByIdsWithDistance(parkIds,
            userLatitude, userLongitude);

        return parks.stream()
            .map(park -> {
                ParkDistanceDto dto = new ParkDistanceDto();
                dto.setParkId(park.getId());
                dto.setParkName(park.getName());
                dto.setImageUrl(park.getImageUrl());
                dto.setDistance(park.getDistance());
                return dto;
            })
            .sorted(Comparator.comparingDouble(ParkDistanceDto::getDistance)) // 가까운 순서로 정렬
            .collect(Collectors.toList());
    }

    /**
     * 동식물 인기도 증가
     *
     * @param speciesId 동식물 ID
     */
    @Override
    public void incrementSpeciesPopularity(Long speciesId) {
        Season currentSeason = getCurrentSeason();
        String key = "popular_species:season:" + currentSeason.name().toLowerCase();
        stringRedisTemplate.opsForZSet().incrementScore(key, speciesId.toString(), 1);
    }

    /**
     * 동식물 인기도 감소
     */
    @Override
    @Scheduled(cron = "0 0 2 * * ?")  // 매일 새벽 2시에 실행
    public void decreaseSpeciesPopularity() {
        Season currentSeason = getCurrentSeason();
        String key = "popular_species:season:" + currentSeason.name().toLowerCase();

        // 인기 동식물의 점수를 10%씩 감소
        Set<ZSetOperations.TypedTuple<String>> popularSpeciesSet = stringRedisTemplate.opsForZSet()
            .rangeWithScores(key, 0, -1);

        if (popularSpeciesSet != null) {
            for (ZSetOperations.TypedTuple<String> species : popularSpeciesSet) {
                Double score = species.getScore();
                if (score != null) {
                    double newScore = Math.max(0, score * 0.9);  // 인기도 점수를 10% 감소
                    stringRedisTemplate.opsForZSet().add(key, species.getValue(), newScore);
                }
            }
        }
    }

    /**
     * 현재 계절을 반환
     *
     * @return 현재 계절
     */
    private Season getCurrentSeason() {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();

        if (month >= 3 && month <= 5) {
            return Season.SPRING;
        } else if (month >= 6 && month <= 8) {
            return Season.SUMMER;
        } else if (month >= 9 && month <= 11) {
            return Season.AUTUMN;
        } else {
            return Season.WINTER;
        }
    }
}
