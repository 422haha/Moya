package com.e22e.moya.park.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkDistanceDto {
    private Long parkId;
    private String parkName;
    private String imageUrl;
    private Double distance;
}
