package com.e22e.moya.exploration.repository;

import com.e22e.moya.common.entity.npc.Npc;
import com.e22e.moya.common.entity.npc.ParkNpcs;
import com.e22e.moya.common.entity.park.Park;
import com.e22e.moya.common.entity.species.ParkSpecies;
import com.e22e.moya.common.entity.species.Species;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkRepository extends JpaRepository<Park, Long> {

    Optional<Park> findById(Long id);

    @Query("SELECT DISTINCT p FROM Park p " +
        "LEFT JOIN FETCH p.parkSpecies ps " +
        "LEFT JOIN FETCH ps.species s " +
        "WHERE p.id = :parkId")
    Optional<Park> findByIdWithSpecies(@Param("parkId") Long parkId);

    @Query("SELECT DISTINCT p FROM Park p " +
        "LEFT JOIN FETCH p.parkNpcs pn " +
        "LEFT JOIN FETCH pn.npc n " +
        "WHERE p.id = :parkId")
    Optional<Park> findByIdWithNpcs(@Param("parkId") Long parkId);

    @Query("SELECT DISTINCT ps FROM ParkSpecies ps " +
        "LEFT JOIN FETCH ps.positions " +
        "WHERE ps.park.id = :parkId")
    List<ParkSpecies> findParkSpeciesWithPositions(@Param("parkId") Long parkId);

    @Query("SELECT DISTINCT pn FROM ParkNpcs pn " +
        "LEFT JOIN FETCH pn.positions " +
        "WHERE pn.park.id = :parkId")
    List<ParkNpcs> findParkNpcsWithPositions(@Param("parkId") Long parkId);

    @Query("SELECT DISTINCT s FROM Park p JOIN p.parkSpecies ps JOIN ps.species s WHERE p.id = :parkId")
    List<Species> findSpeciesInPark(@Param("parkId") Long parkId);

    @Query("SELECT DISTINCT n FROM Park p JOIN p.parkNpcs pn JOIN pn.npc n WHERE p.id = :parkId")
    List<Npc> findNpcsInPark(@Param("parkId") Long parkId);

}
