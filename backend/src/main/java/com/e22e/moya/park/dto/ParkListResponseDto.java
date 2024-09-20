package com.e22e.moya.park.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ParkListResponseDto {

    private List<ParkResponseDto> parks;

    public ParkListResponseDto(List<ParkResponseDto> parks) {
        this.parks = parks;
    }
}