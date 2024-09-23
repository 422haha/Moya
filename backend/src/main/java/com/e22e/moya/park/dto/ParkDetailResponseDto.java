package com.e22e.moya.park.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ParkDetailResponseDto {

    private Long parkId;
    private String name;
    private String description;
    private String imageUrl;
    private List<SpeciesDto> species;
}
