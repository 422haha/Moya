package com.e22e.moya.park.service;

import com.e22e.moya.park.dto.ParkDetailResponseDto;
import com.e22e.moya.park.dto.ParkListResponseDto;
import com.e22e.moya.park.dto.ParkResponseDto;

public interface ParkService {

    ParkResponseDto getNearestPark(Long userId, double latitude, double longitude);

    ParkListResponseDto getParks(Long userId, double latitude, double longitude, int page, int size);

    ParkDetailResponseDto getParkDetail(Long parkId);
}
