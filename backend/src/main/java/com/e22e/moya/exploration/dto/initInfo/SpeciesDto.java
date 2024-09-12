package com.e22e.moya.exploration.dto.initInfo;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SpeciesDto {

    private Long id;
    private String name;
    private String scientificName;
    private String description;
    private String imageUrl;
    private List<PositionDto> positions;
}
