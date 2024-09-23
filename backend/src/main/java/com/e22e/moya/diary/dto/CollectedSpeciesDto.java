package com.e22e.moya.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CollectedSpeciesDto {
    private Long speciesId;
    private String speciesName;
    private String imageUrl;
    private double latitude;
    private double longitude;
}
