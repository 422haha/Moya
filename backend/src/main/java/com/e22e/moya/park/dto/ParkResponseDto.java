package com.e22e.moya.park.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ParkResponseDto {

    private Long parkId;
    private String parkName;
    private int distance;
    private String imageUrl;
}
