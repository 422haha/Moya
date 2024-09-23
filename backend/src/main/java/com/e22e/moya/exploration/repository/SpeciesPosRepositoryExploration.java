package com.e22e.moya.exploration.repository;

import com.e22e.moya.common.entity.species.ParkSpecies;
import com.e22e.moya.common.entity.species.SpeciesPos;
import java.util.Optional;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpeciesPosRepositoryExploration extends JpaRepository<SpeciesPos, Long> {

    @Query(value = "SELECT sp.*, " +
        "ST_Distance(CAST(sp.pos AS geography), CAST(ST_SetSRID(:point, 4326) AS geography)) as distance " +
        "FROM species_pos sp " +
        "WHERE sp.park_species_id = :#{#parkSpecies.id} " +
        "AND ST_DWithin(CAST(sp.pos AS geography), CAST(ST_SetSRID(:point, 4326) AS geography), 5) " +
        "ORDER BY distance " +
        "LIMIT 1",
        nativeQuery = true)
    Optional<SpeciesPos> findByParkSpeciesAndPos(@Param("parkSpecies") ParkSpecies parkSpecies, @Param("point") Point<G2D> point);
}
