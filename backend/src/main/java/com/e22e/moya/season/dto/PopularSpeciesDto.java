package com.e22e.moya.season.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PopularSpeciesDto {

    private Long speciesId;
    private String name;
    private String imageUrl;
    private Double score;
}
