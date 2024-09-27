package com.e22e.moya.season.service;

import com.e22e.moya.season.dto.PopularSpeciesDto;
import com.e22e.moya.park.dto.ParkDistanceDto;

import java.util.List;

public interface PopularSpeciesService {

    List<PopularSpeciesDto> getPopularSpecies(int limit);

    List<ParkDistanceDto> getParksBySpecies(Long speciesId, double latitude, double longitude);

    void incrementSpeciesPopularity(Long speciesId);
}
