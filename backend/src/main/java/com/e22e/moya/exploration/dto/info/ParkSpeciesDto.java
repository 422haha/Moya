package com.e22e.moya.exploration.dto.info;

import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Getter
@Setter
public class ParkSpeciesDto {

    private Long speciesId;
    private String speciesName;
    private String scientificName;
    private String description;
    private String imageUrl;
    private Point position;

}
