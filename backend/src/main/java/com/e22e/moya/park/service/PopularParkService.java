package com.e22e.moya.park.service;

import com.e22e.moya.park.dto.PopularParkDto;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;

public interface PopularParkService {

    void incrementParkPopularity(Long parkId);

    // 사용자 근처의 인기 있는 공원 10개 찾기
    List<PopularParkDto> getPopularParksNearby(double latitude, double longitude);

    // 매일 새벽 2시에 인기도 점수 감소
    @Scheduled(cron = "0 0 2 * * ?")
    void decreasePopularityScores();
}
