package com.e22e.moya.park.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkResponseDto {

    private Long parkId;
    private String parkName;
    private int distance;
    private String imageUrl;

    public ParkResponseDto(Long parkId, String parkName, int distance, String imageUrl) {
        this.parkId = parkId;
        this.parkName = parkName;
        this.distance = distance;
        this.imageUrl = imageUrl;
    }
}
