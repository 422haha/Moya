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
}
