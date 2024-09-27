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
}
