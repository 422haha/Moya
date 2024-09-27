package com.e22e.moya.exploration.repository;

import org.geolatte.geom.Point;

public interface ParkSpeciesProjection {

    Long getSpeciesId();

    String getSpeciesName();

    String getScientificName();

    String getDescription();

    String getImageUrl();

    Point getPosition();
}
