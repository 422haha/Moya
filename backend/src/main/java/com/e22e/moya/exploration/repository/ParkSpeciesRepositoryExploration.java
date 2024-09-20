package com.e22e.moya.exploration.repository;

import com.e22e.moya.common.entity.park.Park;
import com.e22e.moya.common.entity.species.ParkSpecies;
import com.e22e.moya.common.entity.species.Species;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkSpeciesRepositoryExploration extends JpaRepository<ParkSpecies, Long> {

    Optional<ParkSpecies> findByParkAndSpecies(Park park, Species species);
}
