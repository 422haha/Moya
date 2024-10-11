package com.e22e.moya.park.service;

import com.e22e.moya.park.dto.ParkResponseDto;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;

public interface PopularParkService {

    void increaseParkPopularity(Long parkId);

    List<ParkResponseDto> getPopularParksNearby(double latitude, double longitude);

    @Scheduled(cron = "0 0 2 * * ?")
    void decreaseParkPopularity();
}
