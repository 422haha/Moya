package com.e22e.moya.park.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpeciesDto {

    private Long speciesId;
    private String name;
    private String imageUrl;
    private boolean discovered;

    public SpeciesDto(Long speciesId, String name, String imageUrl, boolean discovered) {
        this.speciesId = speciesId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.discovered = discovered;
    }
}
