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

    public ParkDetailResponseDto(Long parkId, String name, String description, String imageUrl,
        List<SpeciesDto> species) {
        this.parkId = parkId;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.species = species;
    }
}
