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
    private double latitude;
    private double longitude;
    private String imageUrl;
    private List<SpeciesDto> species;

    public ParkDetailResponseDto(Long parkId, String name, String description, double latitude,
        double longitude, String imageUrl, List<SpeciesDto> species) {
        this.parkId = parkId;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUrl = imageUrl;
        this.species = species;
    }
}
