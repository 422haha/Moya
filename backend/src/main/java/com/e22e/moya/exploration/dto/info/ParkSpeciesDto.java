package com.e22e.moya.exploration.dto.info;

import org.geolatte.geom.Point;

public interface ParkSpeciesDto {

    Long getSpeciesId();

    String getSpeciesName();

    String getScientificName();

    String getDescription();

    String getImageUrl();

    Point getPosition();
}
