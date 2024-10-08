package com.e22e.moya.collection.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpeciesDto {
    private Long speciesId;
    private String speciesName;
    private Boolean discovered;
    private String imageUrl;
}
